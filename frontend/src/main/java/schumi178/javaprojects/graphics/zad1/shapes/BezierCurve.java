package schumi178.javaprojects.graphics.zad1.shapes;

import javafx.geometry.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.stream.Collectors;

public class BezierCurve implements DrawableShape {
    private static final Map<NewtonPair, Double> newtonCache = new HashMap<>();

    private final List<Point2D> wayPoints = new ArrayList<>();
    private Color color;

    public Color getColor() {
        return color;
    }

    private static class NewtonPair {
        private final int n;
        private final int k;

        private NewtonPair(int n, int k) {
            this.n = n;
            this.k = k;
        }

        public int getN() {
            return n;
        }

        public int getK() {
            return k;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NewtonPair that = (NewtonPair) o;
            return n == that.n && k == that.k;
        }

        @Override
        public int hashCode() {
            return Objects.hash(n, k);
        }
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(color);
        for(List<Double> coords: bezier2D(wayPoints, 200)) {
            context.fillRect(coords.get(0), coords.get(1), 1, 1);
        }
    }

    @Override
    public boolean containsPoint(double x, double y) {
        return false;
    }

    @Override
    public void translate(double x, double y) {

    }

    @Override
    public void resize(double x, double y, double newMouseX, double newMouseY, Edge edge) {
        Bounds bounds = getBounds();
        for(int i = 0; i < wayPoints.size(); i++) {
            if (edge.getHorizontal() != null) {
                Point2D oldPoint = wayPoints.get(i);
                double numerator = oldPoint.getX() - bounds.getMinX();
                double denominator = bounds.getMaxX() - bounds.getMinX();
                double percentDistance;
                if(denominator <= 1) {
                    percentDistance = 0;
                } else {
                    percentDistance = edge.getHorizontal() == HorizontalDirection.LEFT ? 1 - numerator / denominator : numerator / denominator;
                }
                Point2D newPoint = new Point2D(oldPoint.getX() + percentDistance * x, oldPoint.getY());
                wayPoints.set(i, newPoint);
            }
        }
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void updateStartingPoint() {

    }

    public void addWaypoint(double x, double y) {
        wayPoints.add(new Point2D(x, y));
    }

    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public Bounds getBounds() {
        List<Double> xCoords = wayPoints.stream().map(Point2D::getX).collect(Collectors.toList());
        List<Double> yCoords = wayPoints.stream().map(Point2D::getY).collect(Collectors.toList());
        double minX = Collections.min(xCoords);
        double minY = Collections.min(yCoords);
        double maxX = Collections.max(xCoords);
        double maxY = Collections.max(yCoords);
        double width = maxX - minX;
        double height = maxY - minY;
        return new BoundingBox(minX, minY, width, height);
    }

    private double calculateNewton(int n, int k) {
        NewtonPair pair = new NewtonPair(n, k);
        if(newtonCache.containsKey(pair)) {
            return newtonCache.get(pair);
        }

        double numerator;
        double denominator;

        numerator = 1;
        for (int i = n - k + 1; i < n + 1; i++) {
            numerator *= i;
        }

        denominator = 1;
        for (int i = 1; i < k + 1; i++) {
            denominator *= i;
        }
        double newValue = numerator / denominator;
        newtonCache.put(pair, newValue);

        return newtonCache.get(pair);
    }

    private double bernstein(int n, int i, double t) {
        return calculateNewton(n, i) * Math.pow(t, i) * Math.pow((1.0 - t), (n - i));
    }

    private List<Double> calculateCoordinates(List<Point2D> points, int degree, double t) {
        double x = 0.0;
        double y = 0.0;

        for (int i = 0; i < degree + 1; i++) {
            x += points.get(i).getX() * bernstein(degree, i, t);
            y += points.get(i).getY() * bernstein(degree, i, t);
        }

        List<Double> coordinates = new ArrayList<>();
        coordinates.add(x);
        coordinates.add(y);

        return coordinates;
    }

    public List<List<Double>> bezier2D(List<Point2D> points, int noOfSegments) {
        List<List<Double>> curve = new ArrayList<>();
        int degree = points.size() - 1;

        double step = 1.0 / noOfSegments;

        for (int i = 0; i < noOfSegments + 1; i++) {
            List<Double> coordinates = calculateCoordinates(points, degree, i * step);
            curve.add(coordinates);
        }

        return curve;
    }
}
