package сom.example.algorithms;

import сom.example.algorithms.closest.ClosestPair;
import сom.example.algorithms.select.DeterministicSelect;
import сom.example.algorithms.sorting.MergeSort;
import сom.example.algorithms.sorting.QuickSort;
import сom.example.algorithms.util.MetricsTracker;

import java.io.IOException;
import java.util.Random;

/**
 * Main class to parse command-line arguments or run algorithms programmatically,
 * and emit performance metrics to a CSV file.
 */
public class Main {

    /**
     * Entry point for CLI application.
     * Args: <size> <output.csv>
     * @param args Command-line arguments: size of array and CSV file path.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java Main <size> <output.csv>");
            System.exit(1);
        }

        int size = 0;
        try {
            size = Integer.parseInt(args[0]);
            if (size <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.err.println("Size must be a positive integer");
            System.exit(1);
        }
        String csvPath = args[1];

        runAlgorithms(size, csvPath);
    }

    /**
     * Programmatic entry point to run algorithms with specified size and CSV path.
     * @param size Size of the array to generate.
     * @param csvPath Path to the CSV file for metrics.
     */
    public static void runAlgorithms(int size, String csvPath) {
        Random rand = new Random();
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(10000); // Random integers for sorting/selecting
        }

        MetricsTracker tracker = new MetricsTracker();
        runAlgorithms(arr, csvPath, tracker, rand);
    }

    private static void runAlgorithms(Integer[] arr, String csvPath, MetricsTracker tracker, Random rand) {
        // Run MergeSort
        Integer[] mergeArr = arr.clone();
        tracker.reset();
        new MergeSort().sort(mergeArr, tracker);
        try {
            tracker.writeToCSV(csvPath, arr.length, "MergeSort");
        } catch (IOException e) {
            System.err.println("Failed to write MergeSort metrics: " + e.getMessage());
        }

        // Run QuickSort
        Integer[] quickArr = arr.clone();
        tracker.reset();
        new QuickSort().sort(quickArr, tracker);
        try {
            tracker.writeToCSV(csvPath, arr.length, "QuickSort");
        } catch (IOException e) {
            System.err.println("Failed to write QuickSort metrics: " + e.getMessage());
        }

        // Run DeterministicSelect (e.g., median)
        Integer[] selectArr = arr.clone();
        tracker.reset();
        int k = arr.length / 2; // Select median for example
        new DeterministicSelect().select(selectArr, k, tracker);
        try {
            tracker.writeToCSV(csvPath, arr.length, "DeterministicSelect");
        } catch (IOException e) {
            System.err.println("Failed to write Select metrics: " + e.getMessage());
        }

        // Run ClosestPair (example with random 2D points)
        ClosestPair closestPair = new ClosestPair();
        ClosestPair.Point[] points = new ClosestPair.Point[arr.length];
        for (int i = 0; i < arr.length; i++) {
            points[i] = new ClosestPair.Point(arr[i] / 100.0, rand.nextDouble() * 100);
        }
        // Debug: Check for nulls in points
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                System.err.println("Null found at index " + i + " in points array");
            }
        }
        tracker.reset();
        closestPair.findClosestPair(points, tracker);
        try {
            tracker.writeToCSV(csvPath, arr.length, "ClosestPair");
        } catch (IOException e) {
            System.err.println("Failed to write ClosestPair metrics: " + e.getMessage());
        }
    }
}
