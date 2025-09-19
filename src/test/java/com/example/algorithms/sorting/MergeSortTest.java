package com.example.algorithms.sorting;

import сom.example.algorithms.sorting.MergeSort;
import сom.example.algorithms.util.MetricsTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MergeSortTest {

    private MergeSort mergeSort;
    private MetricsTracker tracker;

    @BeforeEach
    void setUp() {
        mergeSort = new MergeSort();
        tracker = new MetricsTracker();
    }

    @Test
    void testSortRandomArray() {
        Integer[] arr = generateRandomArray(100);
        Integer[] expected = Arrays.copyOf(arr, arr.length);
        Arrays.sort(expected);

        mergeSort.sort(arr, tracker);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testSortSortedArray() {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Integer[] expected = Arrays.copyOf(arr, arr.length);

        mergeSort.sort(arr, tracker);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testSortReverseSortedArray() {
        Integer[] arr = new Integer[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        Integer[] expected = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        mergeSort.sort(arr, tracker);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testSortWithDuplicates() {
        Integer[] arr = new Integer[]{5, 3, 5, 1, 4, 4, 2};
        Integer[] expected = new Integer[]{1, 2, 3, 4, 4, 5, 5};

        mergeSort.sort(arr, tracker);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testEmptyAndSingleElement() {
        Integer[] empty = new Integer[0];
        mergeSort.sort(empty, tracker);
        assertArrayEquals(new Integer[0], empty);

        Integer[] single = new Integer[]{42};
        mergeSort.sort(single, tracker);
        assertArrayEquals(new Integer[]{42}, single);
    }

    @Test
    void testMetricsCollection() {
        Integer[] arr = generateRandomArray(100);
        mergeSort.sort(arr, tracker);

        assertTrue(tracker.getComparisons() > 0);
        assertTrue(tracker.getMaxDepth() > 0);
        assertTrue(tracker.getExecutionTimeNs() > 0);
        // Allocations: at least 1 for buffer
        assertTrue(tracker.getAllocations() >= 1);
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