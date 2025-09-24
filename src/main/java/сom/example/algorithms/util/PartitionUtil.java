package сom.example.algorithms.util;

import java.util.Random;

/**
 * Utility class providing common operations for partitioning and array manipulation.
 */
public class PartitionUtil {

    /**
     * Swaps two elements in the array.
     * @param arr The array.
     * @param i First index.
     * @param j Second index.
     * @param <T> Type of array elements.
     */
    public static <T> void swap(T[] arr, int i, int j) {
        if (arr == null) throw new IllegalArgumentArrayException("Array cannot be null");
        if (i < 0 || j < 0 || i >= arr.length || j >= arr.length) {
            throw new IllegalArgumentArrayException("Index out of bounds");
        }
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Shuffles the array randomly using Fisher-Yates algorithm.
     * @param arr The array to shuffle.
     * @param <T> Type of array elements.
     */
    public static <T> void shuffle(T[] arr) {
        if (arr == null) throw new IllegalArgumentArrayException("Array cannot be null");
        Random rand = new Random();
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            swap(arr, i, j);
        }
    }

    /**
     * Guards against null or empty arrays.
     * @param arr The array to check.
     * @param <T> Type of array elements.
     * @throws IllegalArgumentArrayException If array is null or empty.
     */
    public static <T> void checkNotNullOrEmpty(T[] arr) {
        if (arr == null) throw new IllegalArgumentArrayException("Array cannot be null");
        if (arr.length == 0) throw new IllegalArgumentArrayException("Array cannot be empty");
    }

    // Custom exception for array-related errors
    public static class IllegalArgumentArrayException extends IllegalArgumentException {
        public IllegalArgumentArrayException(String message) {
            super(message);
        }
    }
}