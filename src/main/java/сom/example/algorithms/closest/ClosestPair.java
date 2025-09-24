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
        public double x;
        public double y;

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
        Point[] pointsByY = points.clone();
        tracker.incrementAllocation(); // For cloning arrays
        Arrays.sort(pointsByX); // Sort by x
        double minDistance = findClosestPairHelper(pointsByX, pointsByY, 0, pointsByX.length - 1, tracker);
        tracker.stop();
        return minDistance;
    }

    private double findClosestPairHelper(Point[] pointsByX, Point[] pointsByY, int low, int high, MetricsTracker tracker) {
        try {
            tracker.enterRecursion();
            if (high - low <= 3) {
                return bruteForce(pointsByX, low, high, tracker);
            }

            int mid = low + (high - low) / 2;
            Point midPoint = pointsByX[mid];

            // Recursively find closest pairs in left and right halves
            Point[] leftY = new Point[mid - low + 1];
            Point[] rightY = new Point[high - mid];
            tracker.incrementAllocation(); // For temporary y-arrays
            int li = 0, ri = 0;
            for (int i = 0; i < pointsByY.length; i++) {
                if (pointsByY[i].x <= midPoint.x && li < leftY.length) {
                    leftY[li++] = pointsByY[i];
                } else if (ri < rightY.length) {
                    rightY[ri++] = pointsByY[i];
                }
            }

            double leftMin = findClosestPairHelper(pointsByX, leftY, low, mid, tracker);
            double rightMin = findClosestPairHelper(pointsByX, rightY, mid + 1, high, tracker);
            double minDistance = Math.min(leftMin, rightMin);

            // Check strip
            double stripWidth = minDistance;
            Point[] strip = new Point[high - low + 1];
            tracker.incrementAllocation(); // For strip array
            int stripSize = 0;
            for (Point p : pointsByY) {
                if (Math.abs(p.x - midPoint.x) < stripWidth) {
                    strip[stripSize++] = p;
                }
            }
            Arrays.sort(strip, 0, stripSize, (a, b) -> Double.compare(a.y, b.y)); // Sort by y

            // 7-8 neighbor scan
            for (int i = 0; i < stripSize; i++) {
                for (int j = i + 1; j < stripSize && (strip[j].y - strip[i].y) < minDistance; j++) {
                    double distance = distance(strip[i], strip[j]);
                    minDistance = Math.min(minDistance, distance);
                    tracker.incrementComparison();
                }
            }

            return minDistance;
        } finally {
            tracker.exitRecursion();
        }
    }

    private double bruteForce(Point[] points, int low, int high, MetricsTracker tracker) {
        double minDistance = Double.POSITIVE_INFINITY;
        for (int i = low; i <= high; i++) {
            for (int j = i + 1; j <= high; j++) {
                double distance = distance(points[i], points[j]);
                minDistance = Math.min(minDistance, distance);
                tracker.incrementComparison();
            }
        }
        return minDistance;
    }

    private double distance(Point p1, Point p2) {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}