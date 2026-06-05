package fastfilewatch;

import fastfilesearch.FileUpdate;
import fastfilesearch.FileUpdateType;

/**
 * Native bridge for fastfilewatch.dll
 * This matches the exact JNI signatures exported by FastFileWatch.cpp
 */
final class FastFileWatch {
    static {
        try {
            fastcore.FastCore.loadLibrary("fastfilewatch");
        } catch (Throwable e) {
            System.err.println("CRITICAL: FastCore failed to load native DLL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // JNI exports in FastFileWatch.cpp
    static native boolean isUSNJournalAvailable();
    static native String getUSNJournalStatus();
    static native void start(String[] paths, NativeCallback callback);
    static native void stop();

    /**
     * Adapter to translate native string callbacks into FileUpdate records
     */
    static class NativeCallback {
        private final WatchCallback delegate;

        NativeCallback(WatchCallback delegate) {
            this.delegate = delegate;
        }

        public void onFileCreated(String path) {
            delegate.onUpdate(new FileUpdate(FileUpdateType.ADD, null, path));
        }

        public void onFileModified(String path) {
            delegate.onUpdate(new FileUpdate(FileUpdateType.MODIFY, null, path));
        }

        public void onFileDeleted(String path) {
            delegate.onUpdate(new FileUpdate(FileUpdateType.DELETE, path, null));
        }

        public void onFileRenamed(String oldPath, String newPath) {
            delegate.onUpdate(new FileUpdate(FileUpdateType.RENAME, oldPath, newPath));
        }
    }
}
