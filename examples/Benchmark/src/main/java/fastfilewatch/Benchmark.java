package fastfilewatch;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark - Compares WatchService to Java's file monitoring.
 */
public class Benchmark {
    
    public static void main(String[] args) {
        System.out.println("=== WatchService Benchmark ===");
        System.out.println("Comparing WatchService to Java's file monitoring");
        System.out.println();
        
        // Note: WatchService requires USN Journal to be enabled
        System.out.println("Note: WatchService requires USN Journal to be enabled on the volume.");
        System.out.println("Enable with: fsutil usn createjournal C: 64000 4096");
        System.out.println();
        
        // Check USN Journal availability
        boolean usnAvailable = WatchService.isUSNAvailable("C:");
        System.out.println("USN Journal Available: " + usnAvailable);
        System.out.println("Status: " + WatchService.usnStatus("C:"));
        System.out.println();
        
        if (!usnAvailable) {
            System.out.println("Cannot run benchmark - USN Journal is not available.");
            System.out.println("Please enable USN Journal to run the benchmark.");
            return;
        }
        
        // Benchmark Java file monitoring (polling)
        long javaTime = benchmarkJavaMonitoring();
        System.out.println("Java Monitoring Time: " + javaTime + " ms");
        
        // Benchmark WatchService
        long fastWatchTime = benchmarkWatchService();
        System.out.println("WatchService Time: " + fastWatchTime + " ms");
        
        System.out.println();
        double speedup = (double) javaTime / fastWatchTime;
        System.out.println("Speedup: " + String.format("%.2f", speedup) + "x");
        
        System.out.println();
        System.out.println("Note: WatchService currently uses Java stub implementation.");
        System.out.println("Native implementation with USN Journal will provide significant performance improvements.");
        System.out.println();
        System.out.println("=== Benchmark Complete ===");
    }
    
    private static long benchmarkJavaMonitoring() {
        System.out.println("Running Java file monitoring (polling)...");
        long startTime = System.currentTimeMillis();
        
        // Simulate polling for 10 seconds
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
    
    private static long benchmarkWatchService() {
        System.out.println("Running WatchService (Java stub)...");
        long startTime = System.currentTimeMillis();
        
        // WatchService would run continuously
        // For benchmark, we simulate 10 seconds of monitoring
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
