package fastfilewatch;

/**
 * FastFileWatch - Uses USN Journal to keep the index live-updated with zero rescans.
 * 
 * <p>FastFileWatch is the third module in the FastJava file search engine trilogy:
 * <ul>
 *   <li>FastFileIndex - Full filesystem scan → produces a binary, mmap-capable index of all files</li>
 *   <li>FastFileSearch - Builds Prefix Trie, N-Gram index, Exact Match map, and Ranking engine on top of the index</li>
 *   <li>FastFileWatch - Uses USN Journal to keep the index + search structures live-updated with zero rescans</li>
 * </ul>
 * 
 * <p>This architecture is similar to Everything, Spotlight, VSCode, and fsearch but modular and embeddable.
 * 
 * <p><b>Key Features:</b>
 * <ul>
 *   <li>USN Journal monitoring - Zero-filesystem-access change detection</li>
 *   <li>Real-time updates - Instant notification of file changes</li>
 *   <li>Event filtering - Filter by create, modify, delete, rename operations</li>
 *   <li>Path-based filtering - Monitor specific directories or entire drives</li>
 *   <li>Low overhead - Minimal CPU and memory usage</li>
 *   <li>Incremental updates - Only process changed files</li>
 * </ul>
 * 
 * @since 1.0.0
 * @version 1.0.0
 */
public class FastFileWatch {
    static {
        try {
            // Try absolute path first (relative to user.dir)
            String userDir = System.getProperty("user.dir");
            String dllPath = userDir + "\\fastfilewatch.dll";
            System.load(dllPath);
        } catch (UnsatisfiedLinkError e1) {
            try {
                // Fallback to System.loadLibrary
                System.loadLibrary("fastfilewatch");
            } catch (UnsatisfiedLinkError e2) {
                System.err.println("Failed to load fastfilewatch.dll: " + e2.getMessage());
                throw e2;
            }
        }
    }
    
    /**
     * Starts monitoring the specified paths for file system changes using USN Journal.
     * @param paths Array of root directory paths to monitor
     * @param callback Change callback interface
     */
    public static native void start(String[] paths, ChangeCallback callback);
    
    /**
     * Stops monitoring and releases resources.
     */
    public static native void stop();
    
    /**
     * Checks if USN Journal is available on the system.
     * @return true if USN Journal is available, false otherwise
     */
    public static native boolean isUSNJournalAvailable();
    
    /**
     * Gets the USN Journal status message.
     * @return Status message describing USN Journal availability
     */
    public static native String getUSNJournalStatus();
    
    public static void main(String[] args) {
        System.out.println("=== FastFileWatch ===");
        System.out.println("FastFileWatch - USN Journal-based live file monitoring");
        System.out.println("=== OK ===");
    }
}
