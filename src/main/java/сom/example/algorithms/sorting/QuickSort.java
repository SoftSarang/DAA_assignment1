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
    private static final int CUTOFF = 16; // Threshold for switching to insertion sort


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
     * Average T(n) = 2T(n/2) + O(n) -> Master Case 2 (a=2, b=2, f(n)=O(n), n^log_b(a)=n),
     * where a*f(n/b) = O(n) = f(n), thus average Θ(n log n). Worst case O(n^2) with bad pivot.
     */
    private <T extends Comparable<T>> void sortHelper(T[] arr, int low, int high, MetricsTracker tracker) {
        try {
            tracker.enterRecursion();
            while (low < high) {
                if (high - low < CUTOFF) {
                    insertionSort(arr, low, high, tracker); // Реализуй insertionSort аналогично MergeSort
                    return;
                }
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