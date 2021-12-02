package schumi178.javaprojects.graphics.zad1.shapes;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Point2D;
import javafx.geometry.VerticalDirection;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import schumi178.javaprojects.graphics.zad1.util.SerializeUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DrawableTriangle implements DrawableShape {

    private final Polygon triangle;
    private Color color;
    private double theoreticalWidth;
    private double theoreticalHeight;
    private double startingPointX;
    private double startingPointY;
    private ChangeListener<Bounds> updateListener;

    public void setStartingPointX(double startingPointX) {
        this.startingPointX = startingPointX;
    }

    public void setStartingPointY(double startingPointY) {
        this.startingPointY = startingPointY;
    }

    public void setTheoreticalWidth(double theoreticalWidth) {
        this.theoreticalWidth = theoreticalWidth;
    }

    public void setTheoreticalHeight(double theoreticalHeight) {
        this.theoreticalHeight = theoreticalHeight;
    }

    public DrawableTriangle(double x, double y, double w, double h) {
        triangle = new Polygon();
        triangle.getPoints().addAll(
                x + w / 2.0, y,
                x, y + h,
                x + w, y + h);
        startingPointX = x;
        startingPointY = y;
    }

    public Polygon getTriangle() {
        return triangle;
    }

    @Override
    public void draw(GraphicsContext context) {
        ObservableList<Double> points = triangle.getPoints();
        double[] xPoints = IntStream.range(0, points.size()).filter(n -> n % 2 == 0).mapToObj(points::get).mapToDouble(Double::doubleValue).toArray();
        double[] yPoints = IntStream.range(0, points.size()).filter(n -> n % 2 == 1).mapToObj(points::get).mapToDouble(Double::doubleValue).toArray();
        context.setFill(triangle.getFill());
        context.fillPolygon(xPoints, yPoints, 3);
    }

    @Override
    public boolean containsPoint(double x, double y) {
        return triangle.contains(x, y);
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
        ObservableList<Double> points = triangle.getPoints();
        ObservableList<Double> xPoints = IntStream.range(0, points.size()).filter(n -> n % 2 == 0).mapToObj(points::get).map(e -> e + x).collect(Collectors.toCollection(FXCollections::observableArrayList));
        ObservableList<Double> yPoints = IntStream.range(0, points.size()).filter(n -> n % 2 == 1).mapToObj(points::get).map(e -> e + y).collect(Collectors.toCollection(FXCollections::observableArrayList));
        ObservableList<Double> newPoints = joinPoints(xPoints, yPoints);
        triangle.getPoints().setAll(newPoints);
    }

    @Override
    public void rotate(double angle) {
        triangle.setRotate(angle);
    }

    @Override
    public void resize(double x, double y, double newMouseX, double newMouseY, Edge edge) {
        int factorX = edge.getHorizontal() == HorizontalDirection.RIGHT ? 1 : -1;
        int factorY = edge.getVertical() == VerticalDirection.DOWN ? 1 : -1;
        if(edge.getHorizontal() != null)
            theoreticalWidth = theoreticalWidth + x * factorX;
        if(edge.getVertical() != null)
            theoreticalHeight = theoreticalHeight + y * factorY;
        boolean isNegativeX = theoreticalWidth < 0;
        boolean isNegativeY = theoreticalHeight < 0;
        ObservableList<Double> points = triangle.getPoints();
        double newX, newY;
        if(edge.getHorizontal() == HorizontalDirection.RIGHT)
            newX = isNegativeX ? startingPointX + theoreticalWidth : startingPointX;
        else newX = isNegativeX ? newMouseX + theoreticalWidth : newMouseX;
        if(edge.getVertical() == VerticalDirection.DOWN)
            newY = isNegativeY ? startingPointY + theoreticalHeight : startingPointY;
        else newY = isNegativeY ? newMouseY + theoreticalHeight : newMouseY;
        double actualWidth = Math.abs(theoreticalWidth);
        double actualHeight = Math.abs(theoreticalHeight);
        if(edge.getHorizontal() != null) {
            points.set(0, newX + actualWidth / 2.0);
            points.set(2, newX);
            points.set(4, newX + actualWidth);
        }
        if(edge.getVertical() != null) {
            points.set(1, newY);
            points.set(3, newY + actualHeight);
            points.set(5, newY + actualHeight);
        }
    }

    @Override
    public void scaleByFactor(double factor, Point2D origin) {
        ObservableList<Double> points = triangle.getPoints();
        theoreticalWidth *= factor;
        double newX = startingPointX + theoreticalWidth;
        theoreticalHeight *= factor;
        double newY = startingPointY + theoreticalHeight;
        double actualWidth = Math.abs(theoreticalWidth);
        double actualHeight = Math.abs(theoreticalHeight);
        points.set(0, newX + actualWidth / 2.0);
        points.set(1, newY);
        points.set(2, newX);
        points.set(3, newY + actualHeight);
        points.set(4, newX + actualWidth);
        points.set(5, newY + actualHeight);
    }

    @Override
    public void setColor(Color color) {
        triangle.setFill(color);
        this.color = color;
    }

    @Override
    public void updateStartingPoint() {
        startingPointX = triangle.getPoints().get(2);
        startingPointY = triangle.getPoints().get(1);
        theoreticalWidth = Math.abs(theoreticalWidth);
        theoreticalHeight = Math.abs(theoreticalHeight);
    }

    @Override
    public byte[] serialize() {
        byte[] startCode = new byte[1];
        startCode[0] = 3;
        ObservableList<Double> points = triangle.getPoints();
        int triangleX = points.get(2).intValue();
        int triangleY = points.get(1).intValue();
        byte[] triangleXBytes = SerializeUtils.intToByte(triangleX);
        byte[] triangleYBytes = SerializeUtils.intToByte(triangleY);
        byte[] triangleWidth = SerializeUtils.intToByte(points.get(4).intValue() - triangleX);
        byte[] triangleHeight = SerializeUtils.intToByte(points.get(5).intValue() - triangleY);
        byte[] theoreticalWidthBytes = SerializeUtils.intToByte((int)theoreticalWidth);
        byte[] theoreticalHeightBytes = SerializeUtils.intToByte((int)theoreticalHeight);
        byte[] startingPointXBytes = SerializeUtils.intToByte((int)startingPointX);
        byte[] startingPointYBytes = SerializeUtils.intToByte((int)startingPointY);
        byte[] colorBytes = SerializeUtils.colorToByte(color);
        return SerializeUtils.joinBytes(List.of(startCode, triangleXBytes, triangleYBytes, triangleWidth, triangleHeight, theoreticalWidthBytes, theoreticalHeightBytes, startingPointXBytes, startingPointYBytes, colorBytes));
    }

    @Override
    public Bounds getBounds() {
        return triangle.getBoundsInParent();
    }
}
