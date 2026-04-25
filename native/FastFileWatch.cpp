#include <jni.h>
#include <windows.h>
#include <winioctl.h>
#include <string>
#include <vector>
#include <thread>
#include <atomic>
#include <mutex>

// Global state
std::atomic<bool> g_running(false);
std::thread g_monitorThread;
jobject g_callback = nullptr;
JavaVM* g_jvm = nullptr;

// JNI method signatures
jmethodID g_onFileCreated = nullptr;
jmethodID g_onFileModified = nullptr;
jmethodID g_onFileDeleted = nullptr;
jmethodID g_onFileRenamed = nullptr;

// USN Journal structures - use Windows SDK definitions

// Convert wide string to UTF-8
std::string WideToUTF8(const std::wstring& wstr) {
    if (wstr.empty()) return std::string();
    int size_needed = WideCharToMultiByte(CP_UTF8, 0, &wstr[0], (int)wstr.size(), NULL, 0, NULL, NULL);
    std::string strTo(size_needed, 0);
    WideCharToMultiByte(CP_UTF8, 0, &wstr[0], (int)wstr.size(), &strTo[0], size_needed, NULL, NULL);
    return strTo;
}

// Monitor thread function
void MonitorThread(std::vector<std::wstring> paths) {
    JNIEnv* env = nullptr;
    g_jvm->AttachCurrentThread((void**)&env, nullptr);
    
    while (g_running) {
        for (const auto& path : paths) {
            HANDLE hVolume = CreateFileW(
                path.c_str(),
                GENERIC_READ,
                FILE_SHARE_READ | FILE_SHARE_WRITE,
                NULL,
                OPEN_EXISTING,
                0,
                NULL
            );
            
            if (hVolume == INVALID_HANDLE_VALUE) {
                continue;
            }
            
            // Query USN Journal
            USN_JOURNAL_DATA journalData;
            DWORD bytesReturned;
            if (DeviceIoControl(
                hVolume,
                FSCTL_QUERY_USN_JOURNAL,
                NULL,
                0,
                &journalData,
                sizeof(journalData),
                &bytesReturned,
                NULL
            )) {
                // Read USN records
                READ_USN_JOURNAL_DATA readData = {0};
                readData.StartUsn = journalData.NextUsn;
                readData.ReasonMask = USN_REASON_FILE_CREATE | USN_REASON_FILE_DELETE | 
                                     USN_REASON_RENAME_NEW_NAME | USN_REASON_RENAME_OLD_NAME |
                                     USN_REASON_DATA_EXTEND | USN_REASON_DATA_OVERWRITE |
                                     USN_REASON_DATA_TRUNCATION;
                readData.ReturnOnlyOnClose = FALSE;
                readData.Timeout = 0;
                readData.BytesToWaitFor = 0;
                readData.UsnJournalID = journalData.UsnJournalID;
                
                BYTE buffer[65536];
                if (DeviceIoControl(
                    hVolume,
                    FSCTL_READ_USN_JOURNAL,
                    &readData,
                    sizeof(readData),
                    buffer,
                    sizeof(buffer),
                    &bytesReturned,
                    NULL
                )) {
                    DWORD offset = sizeof(USN);
                    while (offset < bytesReturned) {
                        USN_RECORD_V2* record = (USN_RECORD_V2*)(buffer + offset);
                        
                        if (record->RecordLength == 0 || offset + record->RecordLength > bytesReturned) {
                            break;
                        }
                        
                        std::wstring filename(record->FileName, record->FileNameLength / sizeof(WCHAR));
                        std::string utf8Path = WideToUTF8(filename);
                        
                        jstring jPath = env->NewStringUTF(utf8Path.c_str());
                        
                        if (record->Reason & USN_REASON_FILE_CREATE) {
                            env->CallVoidMethod(g_callback, g_onFileCreated, jPath);
                        } else if (record->Reason & USN_REASON_FILE_DELETE) {
                            env->CallVoidMethod(g_callback, g_onFileDeleted, jPath);
                        } else if (record->Reason & (USN_REASON_DATA_EXTEND | USN_REASON_DATA_OVERWRITE | USN_REASON_DATA_TRUNCATION)) {
                            env->CallVoidMethod(g_callback, g_onFileModified, jPath);
                        } else if (record->Reason & USN_REASON_RENAME_NEW_NAME) {
                            // For rename, we'd need to track old name - simplified for now
                            env->CallVoidMethod(g_callback, g_onFileRenamed, jPath, jPath);
                        }
                        
                        env->DeleteLocalRef(jPath);
                        
                        offset += record->RecordLength;
                    }
                }
            }
            
            CloseHandle(hVolume);
        }
        
        Sleep(100); // Polling interval
    }
    
    g_jvm->DetachCurrentThread();
}

// JNI: Check if USN Journal is available
extern "C" JNIEXPORT jboolean JNICALL Java_fastfilewatch_FastFileWatch_isUSNJournalAvailable(JNIEnv* env, jclass) {
    HANDLE hVolume = CreateFileW(
        L"\\\\.\\C:",
        GENERIC_READ,
        FILE_SHARE_READ | FILE_SHARE_WRITE,
        NULL,
        OPEN_EXISTING,
        0,
        NULL
    );
    
    if (hVolume == INVALID_HANDLE_VALUE) {
        return JNI_FALSE;
    }
    
    USN_JOURNAL_DATA journalData;
    DWORD bytesReturned;
    BOOL result = DeviceIoControl(
        hVolume,
        FSCTL_QUERY_USN_JOURNAL,
        NULL,
        0,
        &journalData,
        sizeof(journalData),
        &bytesReturned,
        NULL
    );
    
    CloseHandle(hVolume);
    return result ? JNI_TRUE : JNI_FALSE;
}

// JNI: Get USN Journal status message
extern "C" JNIEXPORT jstring JNICALL Java_fastfilewatch_FastFileWatch_getUSNJournalStatus(JNIEnv* env, jclass) {
    HANDLE hVolume = CreateFileW(
        L"\\\\.\\C:",
        GENERIC_READ,
        FILE_SHARE_READ | FILE_SHARE_WRITE,
        NULL,
        OPEN_EXISTING,
        0,
        NULL
    );
    
    if (hVolume == INVALID_HANDLE_VALUE) {
        return env->NewStringUTF("ERROR: Cannot open C: volume (need admin privileges)");
    }
    
    USN_JOURNAL_DATA journalData;
    DWORD bytesReturned;
    BOOL result = DeviceIoControl(
        hVolume,
        FSCTL_QUERY_USN_JOURNAL,
        NULL,
        0,
        &journalData,
        sizeof(journalData),
        &bytesReturned,
        NULL
    );
    
    CloseHandle(hVolume);
    
    if (result) {
        char status[256];
        sprintf_s(status, sizeof(status), 
            "USN Journal ACTIVE - USN: %llu, Size: %llu bytes", 
            journalData.NextUsn, journalData.UsnJournalID);
        return env->NewStringUTF(status);
    } else {
        return env->NewStringUTF("USN Journal NOT ACTIVE - Enable with: fsutil usn createjournal C: 64000 4096");
    }
}

// JNI: Start monitoring
extern "C" JNIEXPORT void JNICALL Java_fastfilewatch_FastFileWatch_start(JNIEnv* env, jclass, jobjectArray jpaths, jobject jcallback) {
    if (g_running) {
        return;
    }
    
    // Store JVM reference
    env->GetJavaVM(&g_jvm);
    
    // Store callback reference
    g_callback = env->NewGlobalRef(jcallback);
    
    // Get method IDs
    jclass callbackClass = env->GetObjectClass(jcallback);
    g_onFileCreated = env->GetMethodID(callbackClass, "onFileCreated", "(Ljava/lang/String;)V");
    g_onFileModified = env->GetMethodID(callbackClass, "onFileModified", "(Ljava/lang/String;)V");
    g_onFileDeleted = env->GetMethodID(callbackClass, "onFileDeleted", "(Ljava/lang/String;)V");
    g_onFileRenamed = env->GetMethodID(callbackClass, "onFileRenamed", "(Ljava/lang/String;Ljava/lang/String;)V");
    
    // Convert paths
    std::vector<std::wstring> paths;
    jsize pathCount = env->GetArrayLength(jpaths);
    for (jsize i = 0; i < pathCount; i++) {
        jstring jpath = (jstring)env->GetObjectArrayElement(jpaths, i);
        const char* cpath = env->GetStringUTFChars(jpath, NULL);
        std::string path(cpath);
        // Append backslash if not present
        if (!path.empty() && path.back() != '\\') {
            path += '\\';
        }
        // Convert to wide string
        int size_needed = MultiByteToWideChar(CP_UTF8, 0, path.c_str(), -1, NULL, 0);
        std::wstring wpath(size_needed, 0);
        MultiByteToWideChar(CP_UTF8, 0, path.c_str(), -1, &wpath[0], size_needed);
        paths.push_back(wpath);
        env->ReleaseStringUTFChars(jpath, cpath);
        env->DeleteLocalRef(jpath);
    }
    
    // Start monitor thread
    g_running = true;
    g_monitorThread = std::thread(MonitorThread, paths);
}

// JNI: Stop monitoring
extern "C" JNIEXPORT void JNICALL Java_fastfilewatch_FastFileWatch_stop(JNIEnv* env, jclass) {
    if (!g_running) {
        return;
    }
    
    g_running = false;
    
    if (g_monitorThread.joinable()) {
        g_monitorThread.join();
    }
    
    if (g_callback) {
        env->DeleteGlobalRef(g_callback);
        g_callback = nullptr;
    }
}
