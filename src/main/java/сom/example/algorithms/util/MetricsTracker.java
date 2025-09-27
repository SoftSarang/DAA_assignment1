package сom.example.algorithms.util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * MetricsTracker is a utility class for tracking performance metrics in algorithms.
 * It measures execution time, recursion depth, number of comparisons, and allocations.
 * Metrics can be written to a CSV file for analysis and plotting.
 */
public class MetricsTracker {
    private long comparisons = 0;
    private long allocations = 0;
    private int currentDepth = 0;
    private int maxDepth = 0;
    private long startTime = 0;
    private long endTime = 0;

    /**
     * Starts the timer for measuring execution time.
     */
    public void start() {
        startTime = System.nanoTime();
    }

    /**
     * Stops the timer and records the end time.
     */
    public void stop() {
        endTime = System.nanoTime();
    }

    /**
     * Increments the comparison counter.
     */
    public void incrementComparison() {
        comparisons++;
    }

    /**
     * Increments the allocation counter (e.g., for new arrays or objects).
     */
    public void incrementAllocation() {
        allocations++;
    }

    /**
     * Enters a recursion level, updating the current and max depth.
     */
    public void enterRecursion() {
        currentDepth++;
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }
    }

    /**
     * Exits a recursion level, decreasing the current depth.
     */
    public void exitRecursion() {
        currentDepth--;
    }

    /**
     * Writes the collected metrics to a CSV file.
     * Format: n,time_ns,depth,comparisons,allocations
     * @param filePath Path to the CSV file.
     * @param n The input size (e.g., array length).
     * @throws IOException If file writing fails.
     */
    public void writeToCSV(String filePath, int n, String algorithm) throws IOException {
        boolean isNewFile = !Files.exists(Path.of(filePath));
        try (FileWriter writer = new FileWriter(filePath, true)) { // Append mode
            if (isNewFile) {
                writer.append("n,time_ns,depth,comparisons,allocations,algorithm\n");
            }
            long timeNs = endTime - startTime;
            writer.append(String.format("%d,%d,%d,%d,%d,%s\n", n, timeNs, maxDepth, comparisons, allocations, algorithm));
        }
    }

    // Getters for metrics (useful for tests or direct access)
    public long getComparisons() {
        return comparisons;
    }

    public long getAllocations() {
        return allocations;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public long getExecutionTimeNs() {
        return endTime - startTime;
    }

    /**
     * Resets all metrics to zero for reuse.
     */
    public void reset() {
        comparisons = 0;
        allocations = 0;
        currentDepth = 0;
        maxDepth = 0;
        startTime = 0;
        endTime = 0;
    }
}
