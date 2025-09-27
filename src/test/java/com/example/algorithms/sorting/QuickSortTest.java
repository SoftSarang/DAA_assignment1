package com.example.algorithms.sorting;

import сom.example.algorithms.sorting.QuickSort;
import сom.example.algorithms.util.MetricsTracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class QuickSortTest {

    private QuickSort quickSort;
    private MetricsTracker tracker;

    @BeforeEach
    void setUp() {
        quickSort = new QuickSort();
        tracker = new MetricsTracker();
    }

    @Test
    void testSortRandomArray() {
        Integer[] arr = generateRandomArray(100);
        Integer[] expected = Arrays.copyOf(arr, arr.length);
        Arrays.sort(expected);

        quickSort.sort(arr, tracker);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testSortSortedArray() {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Integer[] expected = Arrays.copyOf(arr, arr.length);

        quickSort.sort(arr, tracker);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testSortReverseSortedArray() {
        Integer[] arr = new Integer[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        Integer[] expected = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        quickSort.sort(arr, tracker);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testSortWithDuplicates() {
        Integer[] arr = new Integer[]{5, 3, 5, 1, 4, 4, 2};
        Integer[] expected = new Integer[]{1, 2, 3, 4, 4, 5, 5};

        quickSort.sort(arr, tracker);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testEmptyAndSingleElement() {
        Integer[] empty = new Integer[0];
        quickSort.sort(empty, tracker);
        assertArrayEquals(new Integer[0], empty);

        Integer[] single = new Integer[]{42};
        quickSort.sort(single, tracker);
        assertArrayEquals(new Integer[]{42}, single);
    }

    @Test
    void testDepthBound() {
        Integer[] arr = generateRandomArray(1000);
        quickSort.sort(arr, tracker);
        // Expected depth ≈ 2 * log2(n) + O(1) with randomized pivot
        double log2n = Math.log(1000) / Math.log(2);
        assertTrue(tracker.getMaxDepth() <= 2 * log2n + 10); // Allow some slack
    }

    @Test
    void testRecursionDepth() {
        Integer[] arr = generateRandomArray(1000);
        quickSort.sort(arr, tracker);
        int expectedMaxDepth = (int) (2 * Math.log(arr.length) / Math.log(2) + 10); // Loose bound
        assertTrue(tracker.getMaxDepth() <= expectedMaxDepth, "Depth: " + tracker.getMaxDepth());
    }

    @Test
    void testAllDuplicatesTiny() {
        Integer[] arr = new Integer[]{5, 5, 5, 5}; // Tiny with duplicates
        Integer[] expected = new Integer[]{5, 5, 5, 5};
        quickSort.sort(arr, tracker);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testTinyArray() {
        Integer[] arr = new Integer[]{3, 1, 2};
        Integer[] expected = new Integer[]{1, 2, 3};
        quickSort.sort(arr, tracker);
        assertArrayEquals(expected, arr);
    }

    private Integer[] generateRandomArray(int size) {
        Random rand = new Random();
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(1000);
        }
        return arr;
    }
}