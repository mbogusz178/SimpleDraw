package schumi178.javaprojects.graphics.zad1.shapes;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import schumi178.javaprojects.graphics.zad1.util.SerializeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FreeDrawing implements DrawableShape {

    private final ObservableList<Line> lines;
    private final ObjectProperty<Color> color;
    private Consumer<DrawableShape> boundsListener;
    private double currentX;
    private double currentY;

    public FreeDrawing(double startX, double startY, Color color) {
        lines = FXCollections.observableArrayList();
        this.currentX = startX;
        this.currentY = startY;
        this.color = new SimpleObjectProperty<>(color);
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public Color getColor() {
        return color.get();
    }

    public void addStroke(double newX, double newY) {
        lines.add(new Line(currentX, currentY, newX, newY));
        currentX = newX;
        currentY = newY;
        if(boundsListener != null) boundsListener.accept(this);
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setLineWidth(2.0);
        context.setStroke(color.get());
        for(Line line: lines) {
            context.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
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

    }

    @Override
    public void rotate(double angle) {

    }

    @Override
    public void scaleByFactor(double factor, Point2D origin) {

    }

    @Override
    public void setColor(Color color) {
        this.color.set(color);
    }

    @Override
    public void updateStartingPoint() {

    }

    @Override
    public byte[] serialize() {
        byte[] startCode = new byte[1];
        startCode[0] = 4;
        byte[] colorBytes = SerializeUtils.colorToByte(color.get());
        byte[] lineSize = SerializeUtils.intToByte(lines.size());
        byte[] startX = SerializeUtils.intToByte((int)lines.get(0).getStartX());
        byte[] startY = SerializeUtils.intToByte((int)lines.get(1).getStartY());
        List<byte[]> lineBytes = new ArrayList<>(List.of(startX, startY));
        for(Line line: lines) {
            byte[] endX = SerializeUtils.intToByte((int)line.getEndX());
            byte[] endY = SerializeUtils.intToByte((int)line.getEndY());
            lineBytes.add(endX);
            lineBytes.add(endY);
        }
        byte[] lineBytesJoined = SerializeUtils.joinBytes(lineBytes);
        return SerializeUtils.joinBytes(List.of(startCode, colorBytes, lineSize, lineBytesJoined));
    }

    @Override
    public boolean edgeContainsPoint(double x, double y, Edge edge) {
        return false;
    }

    @Override
    public Bounds getBounds() {
        if(lines.size() < 1) return null;

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for(Line line: lines) {
            List<Double> pointsConsideredX = List.of(line.getStartX(), line.getEndX());
            List<Double> pointsConsideredY = List.of(line.getStartY(), line.getEndY());
            for(double x: pointsConsideredX) {
                if (x < minX) minX = x;
                if (x > maxX) maxX = x;
            }
            for(double y: pointsConsideredY) {
                if (y < minY) minY = y;
                if (y > maxY) maxY = y;
            }
        }
        return new BoundingBox(minX, minY, maxX - minX, maxY - minY);
    }
}
