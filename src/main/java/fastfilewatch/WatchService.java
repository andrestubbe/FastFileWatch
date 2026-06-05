package fastfilewatch;

import fastfilesearch.FileUpdate;

/**
 * WatchService - USN Journal-based file monitoring service.
 * 
 * <p>WatchService is the third module in the FastJava file search engine trilogy:
 * <ul>
 *   <li>FileIndex - Full filesystem scan → produces a binary, mmap-capable index of all files</li>
 *   <li>SearchEngine - Builds Prefix Trie, N-Gram index, Exact Match map, and Ranking engine on top of the index</li>
 *   <li>WatchService - Uses USN Journal to keep the index + search structures live-updated with zero rescans</li>
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
public final class WatchService {
    private final long nativeHandle;

    private WatchService(long nativeHandle) {
        this.nativeHandle = nativeHandle;
    }

    public long handle() {
        return nativeHandle;
    }

    /**
     * Check if USN Journal is available for volume.
     */
    public static boolean isUSNAvailable(String volume) {
        return FastFileWatch.isUSNJournalAvailable();
    }

    /**
     * Get USN Journal status for volume.
     */
    public static String usnStatus(String volume) {
        return FastFileWatch.getUSNJournalStatus();
    }

    /**
     * Start watching roots with callback.
     */
    public static WatchService start(String[] roots, WatchCallback callback) {
        FastFileWatch.start(roots, new FastFileWatch.NativeCallback(callback));
        return new WatchService(1); // Dummy handle since native start returns void
    }

    /**
     * Stop watching.
     */
    public void stop() {
        FastFileWatch.stop();
    }

    public static void main(String[] args) {
        System.out.println("=== WatchService ===");
        System.out.println("WatchService - USN Journal-based live file monitoring");
        System.out.println("=== OK ===");
    }
}
