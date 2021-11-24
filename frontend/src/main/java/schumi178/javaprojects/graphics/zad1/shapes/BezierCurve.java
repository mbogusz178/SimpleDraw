package schumi178.javaprojects.graphics.zad1.shapes;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BezierCurve implements DrawableShape {
    private Map<List<Integer>, Double> newtonCache;

    @Override
    public void draw(GraphicsContext context) {

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

    }

    @Override
    public void setColor(Color color) {

    }

    @Override
    public void updateStartingPoint() {

    }

    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public Bounds getBounds() {
        return null;
    }

    private double calculateNewton(int n, int k) {
        double numerator;
        double denominator;
        List<Integer> newKey;

        numerator = 1;
        for (int i = n - k + 1; i < n + 1; i++) {
            numerator *= i;
        }

        denominator = 1;
        for (int i = 1; i < k + 1; i++) {
            denominator *= i;
        }
        newKey = new ArrayList<>();
        newKey.add(n);
        newKey.add(k);
        double newValue = numerator / denominator;
        newtonCache.put(newKey, newValue);

        return newtonCache.get(newKey);
    }

    private double bernstein(int n, int i, double t) {
        return calculateNewton(n, i) * Math.pow(t, i) * Math.pow((1.0 - t), (n - i));
    }

    private List<Double> calculateCoordinates(double[][] points, int degree, double t) {
        double x = 0.0;
        double y = 0.0;

        for (int i = 0; i < degree + 1; i++) {
            x += points[i][0] * bernstein(degree, i, t);
            y += points[i][1] * bernstein(degree, i, t);
        }

        List<Double> coordinates = new ArrayList<>();
        coordinates.add(x);
        coordinates.add(y);

        return coordinates;
    }

    public List<List<Double>> bezier2D(double[][] points, int noOfSegments) {
        List<List<Double>> curve = new ArrayList<>();
        int degree = points.length - 1;

        double step = 1.0 / noOfSegments;

        for (int i = 0; i < noOfSegments + 1; i++) {
            List<Double> coordinates = calculateCoordinates(points, degree, i * step);
            curve.add(coordinates);
        }

        return curve;
    }
}
