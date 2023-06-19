package schumi178.javaprojects.graphics.zad1.shapes;

import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import schumi178.javaprojects.graphics.zad1.util.Matrix3;
import schumi178.javaprojects.graphics.zad1.util.SerializeUtils;
import schumi178.javaprojects.graphics.zad1.util.Vector3;

import java.util.*;
import java.util.stream.Collectors;

public class BezierCurve implements DrawableShape {
    private static final Map<NewtonPair, Double> newtonCache = new HashMap<>();

    private final List<Point2D> wayPoints = new ArrayList<>();
    private Color color;
    private double startingPointX;
    private double startingPointY;
    private double maxX;
    private double maxY;
    private Point2D oldPointZeroCache;

    public Color getColor() {
        return color;
    }

    public List<Point2D> getWayPoints() {
        return wayPoints;
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
        for(List<Double> coords: bezier2D(wayPoints, 3000)) {
            context.fillRect(coords.get(0), coords.get(1), 1, 1);
        }
    }

    @Override
    public boolean containsPoint(double x, double y) {
        return getBounds().contains(x, y);
    }

    public void setWaypoint(int i, Point2D point) {
        wayPoints.set(i, point);
    }

    public void setLastWaypoint(Point2D point) {
        setWaypoint(wayPoints.size() - 1, point);
    }

    @Override
    public void translate(double x, double y) {
        for(int i = 0; i < wayPoints.size(); i++) {
            Point2D point = wayPoints.get(i);
            double newX = point.getX() + x;
            double newY = point.getY() + y;
            Point2D newPoint = new Point2D(newX, newY);
            wayPoints.set(i, newPoint);
        }
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
//                double numerator = oldPoint.getX() - bounds.getMinX();
//                double denominator = bounds.getMaxX() - bounds.getMinX();
//                double percentDistance;
//                if(denominator <= 1) {
//                    percentDistance = 0;
//                } else {
//                    percentDistance = edge.getHorizontal() == HorizontalDirection.LEFT ? 1 - numerator / denominator : numerator / denominator;
//                }
//                Point2D newPoint = new Point2D(oldPoint.getX() + percentDistance * x, oldPoint.getY());
//                wayPoints.set(i, newPoint);
        }
        if(edge.getVertical() != null) {
            double denominator = oldPoint.getY() - origin.getY();
            if(denominator == 0)
                sy = 0;
            else
                sy = (newMouseY - origin.getY()) / denominator;
        }
        boolean hasOnlyZeroes = true;
        List<Point2D> oldWaypoints = new ArrayList<>();
        for(int i = 0; i < wayPoints.size(); i++) {
            oldWaypoints.add(new Point2D(wayPoints.get(i).getX(), wayPoints.get(i).getY()));
            double newPointX = origin.getX() + (wayPoints.get(i).getX() - origin.getX()) * sx;
            double newPointY = origin.getY() + (wayPoints.get(i).getY() - origin.getY()) * sy;
            Point2D newPoint = new Point2D(newPointX, newPointY);
            wayPoints.set(i, newPoint);
        }
        for (Point2D wayPoint : wayPoints) {
            if (wayPoint.getX() - origin.getX() != 0 && wayPoint.getY() - origin.getY() != 0) {
                hasOnlyZeroes = false;
                break;
            }
        }
        if(hasOnlyZeroes) {
            oldPointZeroCache = oldPoint;
            for(int i = 0; i < wayPoints.size(); i++) {
                wayPoints.set(i, oldWaypoints.get(i));
            }
            return;
        }
        oldPointZeroCache = null;
    }

    @Override
    public void scaleByFactor(double factor, Point2D origin) {
        for (int i = 0; i < wayPoints.size(); i++) {
            wayPoints.set(i, new Point2D(origin.getX() + (wayPoints.get(i).getX() - origin.getX()) * factor,
                    origin.getY() + (wayPoints.get(i).getY() - origin.getY()) * factor));
        }
    }

    @Override
    public void rotate(double angle) {
        for(int i = 0; i < wayPoints.size(); i++) {
            Vector3 vecStart = Vector3.uniform(wayPoints.get(i).getX(), wayPoints.get(i).getY());
            Matrix3 rotation = Matrix3.rotation(angle);
            Vector3 result = vecStart.multiplyByMatrix(rotation);
            wayPoints.set(i, new Point2D(result.getX(), result.getY()));
        }
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    public void setStartingPointX(double startingPointX) {
        this.startingPointX = startingPointX;
    }

    public void setStartingPointY(double startingPointY) {
        this.startingPointY = startingPointY;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public void setOldPointZeroCache(Point2D oldPointZeroCache) {
        this.oldPointZeroCache = oldPointZeroCache;
    }

    @Override
    public void updateStartingPoint() {
        Bounds bounds = getBounds();
        startingPointX = bounds.getMinX();
        startingPointY = bounds.getMinY();
        maxX = bounds.getMaxX();
        maxY = bounds.getMaxY();
    }

    public void addWaypoint(double x, double y) {
        wayPoints.add(new Point2D(x, y));
        updateStartingPoint();
    }

    @Override
    public byte[] serialize() {
        byte[] startCode = new byte[1];
        startCode[0] = 6;
        byte[] waypointsSize = SerializeUtils.intToByte(wayPoints.size());
        List<byte[]> waypointsBytes = new ArrayList<>();
        for(Point2D point: wayPoints) {
            byte[] waypointX = SerializeUtils.intToByte((int)point.getX());
            byte[] waypointY = SerializeUtils.intToByte((int)point.getY());
            waypointsBytes.add(waypointX);
            waypointsBytes.add(waypointY);
        }
        byte[] waypointsBytesJoined = SerializeUtils.joinBytes(waypointsBytes);
        byte[] startingPointXBytes = SerializeUtils.intToByte((int)startingPointX);
        byte[] startingPointYBytes = SerializeUtils.intToByte((int)startingPointY);
        byte[] colorBytes = SerializeUtils.colorToByte(color);
        byte[] maxXBytes = SerializeUtils.intToByte((int)maxX);
        byte[] maxYBytes = SerializeUtils.intToByte((int)maxY);
        return SerializeUtils.joinBytes(List.of(startCode, waypointsSize, waypointsBytesJoined, startingPointXBytes, startingPointYBytes, colorBytes, maxXBytes, maxYBytes));
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
