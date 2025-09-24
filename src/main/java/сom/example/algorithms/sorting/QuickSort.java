package сom.example.algorithms.sorting;

import сom.example.algorithms.util.MetricsTracker;
import сom.example.algorithms.util.PartitionUtil;

import java.util.Random;

/**
 * QuickSort implementation with randomized pivot, smaller-first recursion,
 * and bounded stack depth (typically O(log n)).
 */
public class QuickSort {

    private static final Random RANDOM = new Random();

    /**
     * Sorts the array using QuickSort.
     * @param arr The array to sort.
     * @param tracker Metrics tracker for collecting performance data.
     * @param <T> Type extending Comparable.
     */
    public <T extends Comparable<T>> void sort(T[] arr, MetricsTracker tracker) {
        if (arr == null || arr.length <= 1) return;
        PartitionUtil.checkNotNullOrEmpty(arr); // Guard
        tracker.start();
        sortHelper(arr, 0, arr.length - 1, tracker);
        tracker.stop();
    }

    /**
     * Recursive helper for sorting subarray from low to high.
     */
    private <T extends Comparable<T>> void sortHelper(T[] arr, int low, int high, MetricsTracker tracker) {
        try {
            tracker.enterRecursion();
            while (low < high) {
                int pivotIndex = partition(arr, low, high, tracker);
                // Recurse on smaller partition, iterate over larger
                if (pivotIndex - low < high - pivotIndex) {
                    sortHelper(arr, low, pivotIndex - 1, tracker);
                    low = pivotIndex + 1;
                } else {
                    sortHelper(arr, pivotIndex + 1, high, tracker);
                    high = pivotIndex - 1;
                }
            }
        } finally {
            tracker.exitRecursion();
        }
    }

    /**
     * Partitions the subarray around a random pivot and returns its final position.
     */
    private <T extends Comparable<T>> int partition(T[] arr, int low, int high, MetricsTracker tracker) {
        // Select random pivot
        int randomIndex = low + RANDOM.nextInt(high - low + 1);
        PartitionUtil.swap(arr, low, randomIndex);

        T pivot = arr[low];
        int i = low, j = high + 1;

        while (true) {
            while (i < high) {
                tracker.incrementComparison();
                if (arr[++i].compareTo(pivot) > 0) break;
            }
            while (j > low) {
                tracker.incrementComparison();
                if (arr[--j].compareTo(pivot) <= 0) break;
            }
            if (i >= j) break;
            PartitionUtil.swap(arr, i, j);
        }
        PartitionUtil.swap(arr, low, j);
        return j;
    }
}