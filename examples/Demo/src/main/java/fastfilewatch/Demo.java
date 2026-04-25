package fastfilewatch;

import fastfilewatch.FastFileWatch;
import fastfilewatch.ChangeCallback;

/**
 * Demo - Demonstrates USN Journal-based file monitoring.
 */
public class Demo {
    public static void main(String[] args) {
        System.out.println("=== FastFileWatch Demo ===");
        
        // Check if USN Journal is available
        if (!FastFileWatch.isUSNJournalAvailable()) {
            System.out.println("USN Journal not available on this system");
            System.out.println("Enable it with: fsutil usn createjournal C: 64000 4096");
            return;
        }
        
        System.out.println("USN Journal is available");
        System.out.println("Starting file monitoring on C:\\");
        System.out.println("Press CTRL+C to stop");
        System.out.println();
        
        // Start monitoring
        String[] paths = { "C:\\" };
        FastFileWatch.start(paths, new ChangeCallback() {
            @Override
            public void onFileCreated(String path) {
                System.out.println("[CREATED] " + path);
            }
            
            @Override
            public void onFileModified(String path) {
                System.out.println("[MODIFIED] " + path);
            }
            
            @Override
            public void onFileDeleted(String path) {
                System.out.println("[DELETED] " + path);
            }
            
            @Override
            public void onFileRenamed(String oldPath, String newPath) {
                System.out.println("[RENAMED] " + oldPath + " -> " + newPath);
            }
        });
        
        // Keep running
        try {
            Thread.sleep(60000); // Run for 60 seconds
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        
        // Stop monitoring
        FastFileWatch.stop();
        System.out.println("Monitoring stopped");
    }
}
