package сom.example.algorithms.select;

import сom.example.algorithms.util.MetricsTracker;
import сom.example.algorithms.util.PartitionUtil;

import java.util.Arrays;

/**
 * DeterministicSelect implementation using Median-of-Medians for O(n) worst-case selection.
 * Groups elements by 5, finds median of medians as pivot, performs in-place partition,
 * and recurses only on the needed side, preferring the smaller side for recursion to bound stack depth.
 */
public class DeterministicSelect {

    /**
     * Finds the k-th smallest element (0-based) in the array.
     * The array is modified in-place.
     * @param arr The array.
     * @param k The order statistic (0 for smallest, arr.length-1 for largest).
     * @param tracker Metrics tracker for performance data.
     * @param <T> Type extending Comparable.
     * @return The k-th smallest element.
     */
    public <T extends Comparable<T>> T select(T[] arr, int k, MetricsTracker tracker) {
        PartitionUtil.checkNotNullOrEmpty(arr);
        if (k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("k out of bounds");
        }
        tracker.start();
        T result = selectHelper(arr, 0, arr.length - 1, k, tracker);
        tracker.stop();
        return result;
    }

    /**
     * Recursive helper for finding the k-th smallest element.
     * T(n) = T(n/5) + T(7n/10) + O(n) -> Akra-Bazzi method,
     * where p=1 (from integration), β≈0.794 < 1, thus Θ(n) worst-case complexity.
     */
    private <T extends Comparable<T>> T selectHelper(T[] arr, int low, int high, int k, MetricsTracker tracker) {
        try {
            tracker.enterRecursion();
            while (low < high) {
                T pivot = medianOfMedians(arr, low, high, tracker);
                int[] bounds = threeWayPartition(arr, low, high, pivot, tracker);
                int lt = bounds[0];
                int gt = bounds[1];

                int lessSize = lt - low;
                int eqSize = gt - lt + 1;
                int lessEqSize = lessSize + eqSize;

                if (k < lessSize) {
                    // Go left (< pivot)
                    int discardedSize = (high - low + 1) - lessSize;
                    if (lessSize <= discardedSize) {
                        // Recurse on smaller
                        return selectHelper(arr, low, lt - 1, k, tracker);
                    } else {
                        // Iterate on larger
                        high = lt - 1;
                    }
                } else if (k < lessEqSize) {
                    // In equals range
                    return pivot;
                } else {
                    // Go right (> pivot)
                    int greaterSize = high - gt;
                    int discardedSize = (high - low + 1) - greaterSize;
                    if (greaterSize <= discardedSize) {
                        // Recurse on smaller
                        return selectHelper(arr, gt + 1, high, k - lessEqSize, tracker);
                    } else {
                        // Iterate on larger
                        low = gt + 1;
                        k -= lessEqSize;
                    }
                }
            }
            return arr[low];
        } finally {
            tracker.exitRecursion();
        }
    }

    /**
     * Computes the median of medians by grouping into 5s and recursing.
     */
    private <T extends Comparable<T>> T medianOfMedians(T[] arr, int low, int high, MetricsTracker tracker) {
        int n = high - low + 1;
        if (n <= 5) {
            insertionSort(arr, low, high, tracker);
            return arr[low + n / 2];
        }

        int numGroups = (n + 4) / 5;
        @SuppressWarnings("unchecked")
        T[] medians = (T[]) new Comparable[numGroups];
        tracker.incrementAllocation();  // For medians array

        for (int i = 0; i < numGroups; i++) {
            int groupLow = low + i * 5;
            int groupHigh = Math.min(groupLow + 4, high);
            insertionSort(arr, groupLow, groupHigh, tracker);
            medians[i] = arr[(groupLow + groupHigh) / 2];
        }

        // Recurse to find median of medians
        return selectHelper(medians, 0, numGroups - 1, numGroups / 2, tracker);
    }

    /**
     * Performs a three-way partition: < pivot | == pivot | > pivot.
     * Returns [lt, gt] where lt is the start of ==, gt is the end of ==.
     */
    private <T extends Comparable<T>> int[] threeWayPartition(T[] arr, int low, int high, T pivot, MetricsTracker tracker) {
        // Find and swap one occurrence of pivot to high
        boolean found = false;
        for (int j = low; j <= high; j++) {
            tracker.incrementComparison();
            if (arr[j].compareTo(pivot) == 0) {
                PartitionUtil.swap(arr, j, high);
                found = true;
                break;
            }
        }
        if (!found) {
            // This shouldn't happen, but for safety
            throw new RuntimeException("Pivot not found in array");
        }

        T pivotVal = arr[high];
        int lt = low;
        int gt = high;
        int i = low;

        while (i <= gt) {
            tracker.incrementComparison();
            int cmp = arr[i].compareTo(pivotVal);
            if (cmp < 0) {
                PartitionUtil.swap(arr, lt, i);
                lt++;
                i++;
            } else if (cmp > 0) {
                PartitionUtil.swap(arr, i, gt);
                gt--;
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }

    /**
     * Insertion sort for small groups or base cases.
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