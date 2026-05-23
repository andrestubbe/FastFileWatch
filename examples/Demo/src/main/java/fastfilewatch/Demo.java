package fastfilewatch;

import fastfilesearch.FileUpdate;
import fastfilesearch.FileUpdateType;

/**
 * Demo - Demonstrates USN Journal-based file monitoring.
 */
public class Demo {
    public static void main(String[] args) {
        System.out.println("=== WatchService Demo ===");
        System.out.println("USN Journal Status: " + WatchService.usnStatus("C:"));
        System.out.println();
        
        // Check if USN Journal is available
        if (!WatchService.isUSNAvailable("C:")) {
            System.out.println("USN Journal not available - monitoring will not receive events");
            System.out.println("Enable it with: fsutil usn createjournal C: 64000 4096");
            System.out.println();
        }
        
        System.out.println("Starting file monitoring on C:\\");
        System.out.println("Press CTRL+C to stop");
        System.out.println();
        
        // Start monitoring
        String[] roots = { "C:\\" };
        WatchService service = WatchService.start(roots, new WatchCallback() {
            @Override
            public void onUpdate(FileUpdate update) {
                switch (update.type()) {
                    case ADD:
                        System.out.println("[CREATED] " + update.newPath());
                        break;
                    case MODIFY:
                        System.out.println("[MODIFIED] " + update.newPath());
                        break;
                    case DELETE:
                        System.out.println("[DELETED] " + update.oldPath());
                        break;
                    case RENAME:
                        System.out.println("[RENAMED] " + update.oldPath() + " -> " + update.newPath());
                        break;
                }
            }
        });
        
        // Keep running
        try {
            Thread.sleep(60000); // Run for 60 seconds
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        
        // Stop monitoring
        service.stop();
        System.out.println("Monitoring stopped");
    }
}
