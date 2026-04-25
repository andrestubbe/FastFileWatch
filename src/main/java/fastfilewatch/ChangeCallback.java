package fastfilewatch;

/**
 * Callback interface for receiving file system change notifications.
 */
public interface ChangeCallback {
    /**
     * Called when a file is created.
     * @param path Full path to the created file
     */
    void onFileCreated(String path);
    
    /**
     * Called when a file is modified.
     * @param path Full path to the modified file
     */
    void onFileModified(String path);
    
    /**
     * Called when a file is deleted.
     * @param path Full path to the deleted file
     */
    void onFileDeleted(String path);
    
    /**
     * Called when a file is renamed.
     * @param oldPath Old path of the file
     * @param newPath New path of the file
     */
    void onFileRenamed(String oldPath, String newPath);
}
