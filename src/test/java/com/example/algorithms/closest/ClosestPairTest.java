package com.example.algorithms.closest;

import сom.example.algorithms.closest.ClosestPair;
import сom.example.algorithms.util.MetricsTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ClosestPairTest {

    private ClosestPair closestPair;
    private MetricsTracker tracker;

    @BeforeEach
    void setUp() {
        closestPair = new ClosestPair();
        tracker = new MetricsTracker();
    }

    @Test
    void testSmallArray() {
        ClosestPair.Point[] points = {
                new ClosestPair.Point(1, 1),
                new ClosestPair.Point(2, 2),
                new ClosestPair.Point(3, 3)
        };
        double expected = Math.sqrt(2); // Distance between (1,1) and (2,2)
        double result = closestPair.findClosestPair(points, tracker);
        assertEquals(expected, result, 1e-10);
    }

    @Test
    void testRandomPoints() {
        Random rand = new Random();
        int size = 100;
        ClosestPair.Point[] points = new ClosestPair.Point[size];
        for (int i = 0; i < size; i++) {
            points[i] = new ClosestPair.Point(rand.nextDouble() * 100, rand.nextDouble() * 100);
        }
        double result = closestPair.findClosestPair(points, tracker);
        assertTrue(result >= 0);
        // Validate against brute force for small subset if needed
        if (size <= 2000) {
            double bruteForceMin = bruteForceClosest(points);
            assertEquals(bruteForceMin, result, 1e-10);
        }
    }

    @Test
    void testInvalidInput() {
        ClosestPair.Point[] points = new ClosestPair.Point[0];
        assertThrows(IllegalArgumentException.class, () -> closestPair.findClosestPair(points, tracker));
        ClosestPair.Point[] single = {new ClosestPair.Point(1, 1)};
        assertThrows(IllegalArgumentException.class, () -> closestPair.findClosestPair(single, tracker));
    }

    private double bruteForceClosest(ClosestPair.Point[] points) {
        double minDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double distance = Math.sqrt(Math.pow(points[i].x - points[j].x, 2) + Math.pow(points[i].y - points[j].y, 2));
                minDistance = Math.min(minDistance, distance);
            }
        }
        return minDistance;
    }
}