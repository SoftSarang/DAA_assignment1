package com.example.algorithms.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import сom.example.algorithms.select.DeterministicSelect;
import сom.example.algorithms.sorting.MergeSort;
import сom.example.algorithms.sorting.QuickSort;
import сom.example.algorithms.util.MetricsTracker;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1)
public class BenchmarkSelectVsSort {

    @Param({"100", "1000", "10000"})
    private int size;

    private Integer[] arr;

    @Setup
    public void setup() {
        arr = new Integer[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(10000); // Initialize with random values to avoid nulls
        }
    }

    @Benchmark
    public void benchmarkDeterministicSelect(Blackhole blackhole) {
        Integer[] copy = arr.clone();
        MetricsTracker tracker = new MetricsTracker(); // Initialize tracker to avoid null
        DeterministicSelect select = new DeterministicSelect();
        Integer result = select.select(copy, size / 2, tracker); // Median
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkMergeSort(Blackhole blackhole) {
        Integer[] copy = arr.clone();
        MetricsTracker tracker = new MetricsTracker(); // Initialize tracker to avoid null
        MergeSort mergeSort = new MergeSort();
        mergeSort.sort(copy, tracker);
        blackhole.consume(copy);
    }

    @Benchmark
    public void benchmarkQuickSort(Blackhole blackhole) {
        Integer[] copy = arr.clone();
        MetricsTracker tracker = new MetricsTracker(); // Initialize tracker to avoid null
        QuickSort quickSort = new QuickSort();
        quickSort.sort(copy, tracker);
        blackhole.consume(copy);
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}