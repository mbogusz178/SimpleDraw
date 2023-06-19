package schumi178.javaprojects.graphics.zad1.shapes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Point2D;
import javafx.geometry.VerticalDirection;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import schumi178.javaprojects.graphics.zad1.util.Matrix3;
import schumi178.javaprojects.graphics.zad1.util.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DrawablePolygon implements DrawableShape {

    private final Polygon polygon = new Polygon();
    private Color color;
    private double startingPointX;
    private double startingPointY;
    private double maxX;
    private double maxY;
    private Point2D oldPointZeroCache;

    @Override
    public void draw(GraphicsContext context) {
        ObservableList<Double> points = polygon.getPoints();
        double[] xPoints = IntStream.range(0, points.size()).filter(n -> n % 2 == 0).mapToObj(points::get).mapToDouble(Double::doubleValue).toArray();
        double[] yPoints = IntStream.range(0, points.size()).filter(n -> n % 2 == 1).mapToObj(points::get).mapToDouble(Double::doubleValue).toArray();
        context.setFill(color);
        context.fillPolygon(xPoints, yPoints, points.size() / 2);
    }

    public void addPoint(Point2D point) {
        polygon.getPoints().addAll(point.getX(), point.getY());
        updateStartingPoint();
    }

    @Override
    public boolean containsPoint(double x, double y) {
        return polygon.contains(x, y);
    }

    private static ObservableList<Double> joinPoints(ObservableList<Double> xPoints, ObservableList<Double> yPoints) {
        ObservableList<Double> newPoints = FXCollections.observableArrayList();
        for(int i = 0; i < xPoints.size() + yPoints.size(); i++) {
            if(i % 2 == 0) newPoints.add(xPoints.get(i / 2));
            else newPoints.add(yPoints.get(i / 2));
        }
        return newPoints;
    }

    @Override
    public void translate(double x, double y) {
        ObservableList<Double> points = polygon.getPoints();
        ObservableList<Double> xPoints = IntStream.range(0, points.size()).filter(n -> n % 2 == 0).mapToObj(points::get).map(e -> e + x).collect(Collectors.toCollection(FXCollections::observableArrayList));
        ObservableList<Double> yPoints = IntStream.range(0, points.size()).filter(n -> n % 2 == 1).mapToObj(points::get).map(e -> e + y).collect(Collectors.toCollection(FXCollections::observableArrayList));
        ObservableList<Double> newPoints = joinPoints(xPoints, yPoints);
        polygon.getPoints().setAll(newPoints);
    }

    private ObservableList<Double> getXPoints() {
        ObservableList<Double> points = polygon.getPoints();
        return IntStream.range(0, points.size()).filter(n -> n % 2 == 0).mapToObj(points::get).collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    private ObservableList<Double> getYPoints() {
        ObservableList<Double> points = polygon.getPoints();
        return IntStream.range(0, points.size()).filter(n -> n % 2 == 1).mapToObj(points::get).collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @Override
    public void resize(double x, double y, double newMouseX, double newMouseY, Edge edge) {
        Point2D origin = new Point2D(edge.getHorizontal() == HorizontalDirection.RIGHT ? startingPointX : maxX,
                edge.getVertical() == VerticalDirection.DOWN ? startingPointY : maxY);
        Point2D oldPoint = Objects.requireNonNullElseGet(oldPointZeroCache, () -> new Point2D(newMouseX - x, newMouseY - y));
        double sx = 1, sy = 1;
        if (edge.getHorizontal() != null) {
            double denominator = oldPoint.getX() - origin.getX();
            if(denominator == 0)
                sx = 0;
            else
                sx = (newMouseX - origin.getX()) / denominator;
        }
        if(edge.getVertical() != null) {
            double denominator = oldPoint.getY() - origin.getY();
            if(denominator == 0)
                sy = 0;
            else
                sy = (newMouseY - origin.getY()) / denominator;
        }
        List<Double> polygonPoints = polygon.getPoints();
        boolean hasOnlyZeroes = true;
        List<Point2D> oldPolygonPoints = new ArrayList<>();
        for(int i = 0; i < polygonPoints.size(); i += 2) {
            oldPolygonPoints.add(new Point2D(polygonPoints.get(i), polygonPoints.get(i+1)));
            double newPointX = origin.getX() + (polygonPoints.get(i) - origin.getX()) * sx;
            double newPointY = origin.getY() + (polygonPoints.get(i+1) - origin.getY()) * sy;
            Point2D newPoint = new Point2D(newPointX, newPointY);
            polygonPoints.set(i, newPoint.getX());
            polygonPoints.set(i+1, newPoint.getY());
        }
        for (int i = 0; i < polygonPoints.size(); i += 2) {
            if (polygonPoints.get(i) - origin.getX() != 0 && polygonPoints.get(i+1) - origin.getY() != 0) {
                hasOnlyZeroes = false;
                break;
            }
        }
        if(hasOnlyZeroes) {
            oldPointZeroCache = oldPoint;
            for(int i = 0; i < polygonPoints.size(); i += 2) {
                polygonPoints.set(i, oldPolygonPoints.get(i/2).getX());
                polygonPoints.set(i+1, oldPolygonPoints.get(i/2).getY());
            }
            return;
        }
        oldPointZeroCache = null;
    }

    @Override
    public void scaleByFactor(double factor, Point2D origin) {
        ObservableList<Double> points = polygon.getPoints();
        for (int i = 0; i < points.size(); i += 2) {
            points.set(i, origin.getX() + (points.get(i) - origin.getX()) * factor);
            points.set(i+1, origin.getY() + (points.get(i+1) - origin.getY()) * factor);
        }
    }

    @Override
    public void rotate(double angle) {
        ObservableList<Double> points = polygon.getPoints();
        for(int i = 0; i < points.size(); i += 2) {
            Vector3 vecStart = Vector3.uniform(points.get(i), points.get(i+1));
            Matrix3 rotation = Matrix3.rotation(angle);
            Vector3 result = vecStart.multiplyByMatrix(rotation);
            points.set(i, result.getX());
            points.set(i+1, result.getY());
        }
    }

    @Override
    public void setColor(Color color) {
        polygon.setFill(color);
        this.color = color;
    }

    @Override
    public void updateStartingPoint() {
        Bounds bounds = getBounds();
        startingPointX = bounds.getMinX();
        startingPointY = bounds.getMinY();
        maxX = bounds.getMaxX();
        maxY = bounds.getMaxY();
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public void setStartingPointX(double startingPointX) {
        this.startingPointX = startingPointX;
    }

    public void setStartingPointY(double startingPointY) {
        this.startingPointY = startingPointY;
    }

    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public Bounds getBounds() {
        return polygon.getBoundsInParent();
    }
}
