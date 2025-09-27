package com.example.algorithms.select;

import сom.example.algorithms.select.DeterministicSelect;
import сom.example.algorithms.util.MetricsTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DeterministicSelectTest {

    private DeterministicSelect select;
    private MetricsTracker tracker;

    @BeforeEach
    void setUp() {
        select = new DeterministicSelect();
        tracker = new MetricsTracker();
    }

    @Test
    void testSelectRandomTrials() {
        Random rand = new Random();
        for (int trial = 0; trial < 100; trial++) {
            Integer[] arr = new Integer[100 + rand.nextInt(900)];  // Sizes 100-1000
            for (int i = 0; i < arr.length; i++) {
                arr[i] = rand.nextInt(10000);
            }
            int k = rand.nextInt(arr.length);
            Integer[] sorted = Arrays.copyOf(arr, arr.length);
            Arrays.sort(sorted);
            Integer expected = sorted[k];

            tracker.reset();
            Integer result = select.select(arr, k, tracker);

            assertEquals(expected, result);
        }
    }

    @Test
    void testSelectAdversarialDuplicates() {
        Integer[] arr = new Integer[100];
        Arrays.fill(arr, 42);  // All duplicates
        for (int k = 0; k < arr.length; k++) {
            assertEquals(42, select.select(arr, k, tracker));
        }
    }

    @Test
    void testSelectTinyArrays() {
        Integer[] arr = {3, 1, 4, 1, 5};
        assertEquals(1, select.select(arr, 0, tracker));  // Smallest
        assertEquals(5, select.select(arr, 4, tracker));  // Largest
        assertEquals(3, select.select(arr, 2, tracker));  // Median
    }

    @Test
    void testDepthBound() {
        Integer[] arr = new Integer[10000];
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt();
        }
        select.select(arr, arr.length / 2, tracker);
        double log5n = Math.log(arr.length) / Math.log(5);  // Rough bound for MoM depth
        assertTrue(tracker.getMaxDepth() <= 5 * log5n + 10);  // Generous bound
    }

    @Test
    void testInvalidInputs() {
        Integer[] arr = {1, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> select.select(arr, -1, tracker));
        assertThrows(IllegalArgumentException.class, () -> select.select(arr, 3, tracker));
    }

    @Test
    void testReverseSorted() {
        Integer[] arr = new Integer[100];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr.length - i;
        }
        Integer result = select.select(arr, arr.length / 2, tracker);
        assertEquals(arr.length / 2 + 1, result); // Median for reverse sorted
    }

    @Test
    void testAllDuplicatesTiny() {
        Integer[] arr = new Integer[]{5, 5, 5}; // Tiny with all duplicates
        for (int k = 0; k < arr.length; k++) {
            assertEquals(5, select.select(arr, k, tracker));
        }
    }

    @Test
    void testTinyArrays() {
        Integer[] arr = new Integer[]{3, 1, 4, 1, 5}; // n=5
        assertEquals(1, select.select(arr, 0, tracker)); // Min
        assertEquals(5, select.select(arr, 4, tracker)); // Max
        assertEquals(3, select.select(arr, 2, tracker)); // Median

        Integer[] two = new Integer[]{2, 1};
        assertEquals(1, select.select(two, 0, tracker));
    }
}