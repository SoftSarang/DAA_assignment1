package сom.example.algorithms.closest;

import сom.example.algorithms.util.MetricsTracker;

import java.util.Arrays;

/**
 * ClosestPair implementation for 2D points using divide-and-conquer with O(n log n) complexity.
 * Sorts points by x-coordinate, recursively splits, and checks a strip with y-order and 7-8 neighbor scan.
 */
public class ClosestPair {

    /**
     * Represents a 2D point with x and y coordinates.
     */
    public static class Point implements Comparable<Point> {
        public double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point other) {
            return Double.compare(this.x, other.x);
        }
    }

    /**
     * Finds the minimum distance between any pair of points.
     * @param points Array of 2D points.
     * @param tracker Metrics tracker for performance data.
     * @return Minimum distance between any two points.
     */
    public double findClosestPair(Point[] points, MetricsTracker tracker) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("At least 2 points required");
        }
        tracker.start();
        Point[] pointsByX = points.clone();
        tracker.incrementAllocation(); // For cloning array
        Arrays.sort(pointsByX); // Sort by x
        double minDistance = findClosestPairRecursive(pointsByX, 0, pointsByX.length - 1, tracker);
        tracker.stop();
        return minDistance;
    }

    /**
     * Recursive helper for finding the closest pair of points.
     * T(n) = 2T(n/2) + O(n) -> Master Case 2 (a=2, b=2, f(n)=O(n), n^log_b(a)=n),
     * where a*f(n/b) = O(n) = f(n), thus Θ(n log n).
     */
    private double findClosestPairRecursive(Point[] pts, int left, int right, MetricsTracker tracker) {
        try {
            tracker.enterRecursion();
            int n = right - left + 1;
            if (n <= 3) {
                return bruteForce(pts, left, right, tracker);
            }

            int mid = left + (right - left) / 2;
            double midX = pts[mid].x;
            double dLeft = findClosestPairRecursive(pts, left, mid, tracker);
            double dRight = findClosestPairRecursive(pts, mid + 1, right, tracker);
            double d = Math.min(dLeft, dRight);


            // Build strip
            tracker.incrementAllocation();
            Point[] strip = new Point[n];
            int stripSize = 0;
            for (int i = left; i <= right; i++) {
                if (Math.abs(pts[i].x - midX) < d) {
                    boolean isDuplicate = false;
                    for (int j = 0; j < stripSize; j++) {
                        if (pts[i].x == strip[j].x && pts[i].y == strip[j].y) {
                            isDuplicate = true;
                            break;
                        }
                    }
                    if (!isDuplicate) {
                        strip[stripSize++] = pts[i];
                    }
                }
            }
            tracker.incrementAllocation(); // For strip array
            Arrays.sort(strip, 0, stripSize, (a, b) -> Double.compare(a.y, b.y));

            // 7-8 neighbor scan
            for (int i = 0; i < stripSize; i++) {
                for (int j = i + 1; j < stripSize && (j - i) < 8 && (strip[j].y - strip[i].y) < d; j++) {
                    double distance = distance(strip[i], strip[j], tracker);
                    d = Math.min(d, distance);
                    tracker.incrementComparison();
                }
            }

            return d;
        } finally {
            tracker.exitRecursion();
        }
    }



    private double bruteForce(Point[] points, int low, int high, MetricsTracker tracker) {
        double minDistance = Double.POSITIVE_INFINITY;
        for (int i = low; i <= high; i++) {
            for (int j = i + 1; j <= high; j++) {
                if (points[i].x == points[j].x && points[i].y == points[j].y) {
                    continue;
                }
                double distance = distance(points[i], points[j], tracker);
                minDistance = Math.min(minDistance, distance);
                tracker.incrementComparison();
            }
        }
        return minDistance == Double.POSITIVE_INFINITY ? 0.0 : minDistance; // Если все дубликаты, возвращаем 0
    }

    private double distance(Point p1, Point p2, MetricsTracker tracker) {
        tracker.incrementComparison();
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}