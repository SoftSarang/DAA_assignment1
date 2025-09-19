package сom.example.algorithms.sorting;

import сom.example.algorithms.util.MetricsTracker;

import java.util.Arrays;

/**
 * MergeSort implementation with divide-and-conquer strategy.
 * Features: reusable buffer for merging, small-n cutoff to insertion sort,
 * and integration with MetricsTracker for performance metrics.
 */
public class MergeSort {

    private static final int CUTOFF = 16; // Threshold for switching to insertion sort

    /**
     * Sorts the array using MergeSort.
     * @param arr The array to sort.
     * @param tracker Metrics tracker for collecting performance data.
     * @param <T> Type extending Comparable.
     */
    public <T extends Comparable<T>> void sort(T[] arr, MetricsTracker tracker) {
        if (arr == null || arr.length <= 1) return;
        tracker.start();
        tracker.incrementAllocation(); // For the buffer
        T[] buffer = Arrays.copyOf(arr, arr.length); // Reusable buffer
        sortHelper(arr, buffer, 0, arr.length - 1, tracker);
        tracker.stop();
    }

    /**
     * Recursive helper for sorting subarray from low to high.
     */
    private <T extends Comparable<T>> void sortHelper(T[] arr, T[] buffer, int low, int high, MetricsTracker tracker) {
        try {
            tracker.enterRecursion();
            if (high - low < CUTOFF) {
                insertionSort(arr, low, high, tracker);
                return;
            }
            int mid = low + (high - low) / 2;
            sortHelper(arr, buffer, low, mid, tracker);
            sortHelper(arr, buffer, mid + 1, high, tracker);
            // Skip merge if already sorted (optimization)
            tracker.incrementComparison();
            if (arr[mid].compareTo(arr[mid + 1]) <= 0) {
                return;
            }
            merge(arr, buffer, low, mid, high, tracker);
        } finally {
            tracker.exitRecursion();
        }
    }

    /**
     * Merges two sorted halves using linear merge.
     */
    private <T extends Comparable<T>> void merge(T[] arr, T[] buffer, int low, int mid, int high, MetricsTracker tracker) {
        // Copy to buffer
        System.arraycopy(arr, low, buffer, low, high - low + 1);
        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) {
                arr[k] = buffer[j++];
            } else if (j > high) {
                arr[k] = buffer[i++];
            } else {
                tracker.incrementComparison();
                if (buffer[i].compareTo(buffer[j]) <= 0) {
                    arr[k] = buffer[i++];
                } else {
                    arr[k] = buffer[j++];
                }
            }
        }
    }

    /**
     * Insertion sort for small subarrays.
     */
    private <T extends Comparable<T>> void insertionSort(T[] arr, int low, int high, MetricsTracker tracker) {
        for (int i = low + 1; i <= high; i++) {
            T key = arr[i];
            int j = i - 1;
            while (j >= low) {
                tracker.incrementComparison();
                if (arr[j].compareTo(key) > 0) {
                    arr[j + 1] = arr[j];
                    j--;
                } else {
                    break;
                }
            }
            arr[j + 1] = key;
        }
    }
}