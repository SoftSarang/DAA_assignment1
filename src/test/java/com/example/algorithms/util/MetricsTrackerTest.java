package com.example.algorithms.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import сom.example.algorithms.util.MetricsTracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MetricsTrackerTest {

    private MetricsTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new MetricsTracker();
    }

    @Test
    void testTiming() {
        tracker.start();
        // Simulate work
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // Ignore
        }
        tracker.stop();
        assertTrue(tracker.getExecutionTimeNs() > 0);
    }

    @Test
    void testCounters() {
        tracker.incrementComparison();
        tracker.incrementComparison();
        tracker.incrementAllocation();
        assertEquals(2, tracker.getComparisons());
        assertEquals(1, tracker.getAllocations());
    }

    @Test
    void testRecursionDepth() {
        tracker.enterRecursion();
        tracker.enterRecursion();
        assertEquals(2, tracker.getMaxDepth());
        tracker.exitRecursion();
        tracker.enterRecursion(); // Max stays 2
        assertEquals(2, tracker.getMaxDepth());
        tracker.exitRecursion();
        tracker.exitRecursion();
    }

    @Test
    void testWriteToCSV(@TempDir Path tempDir) throws IOException {
        Path csvPath = tempDir.resolve("metrics.csv");
        tracker.start();
        tracker.incrementComparison();
        tracker.enterRecursion();
        tracker.stop();
        tracker.writeToCSV(csvPath.toString(), 100, "Algorithm");

        List<String> lines = Files.readAllLines(csvPath);
        assertEquals(2, lines.size(), "CSV should contain header and one data line");

        // Проверка заголовка (первая строка)
        String header = lines.get(0);
        assertEquals("n,time_ns,depth,comparisons,allocations,algorithm", header.trim());

        // Проверка данных (вторая строка)
        String dataLine = lines.get(1);
        String[] parts = dataLine.split(",");
        assertEquals("100", parts[0].trim()); // n
        assertTrue(Long.parseLong(parts[1].trim()) > 0); // time
        assertEquals("1", parts[2].trim()); // depth
        assertEquals("1", parts[3].trim()); // comparisons
        assertEquals("0", parts[4].trim()); // allocations
        assertEquals("Algorithm", parts[5].trim()); // algorithm
    }

    @Test
    void testReset() {
        tracker.incrementComparison();
        tracker.enterRecursion();
        tracker.reset();
        assertEquals(0, tracker.getComparisons());
        assertEquals(0, tracker.getMaxDepth());
    }
}