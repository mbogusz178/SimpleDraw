package schumi178.javaprojects.graphics.zad1.shapes;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import schumi178.javaprojects.graphics.zad1.util.SerializeUtils;

import java.util.List;

public class DrawableLine implements DrawableShape {

    private final Line line;
    private Color color;
    private ChangeListener<Bounds> updateListener;

    public DrawableLine(Line line) {
        this.line = line;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setLineWidth(2.0);
        context.setStroke(line.getStroke());
        context.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
    }

    @Override
    public boolean containsPoint(double x, double y) {
        return line.contains(x, y);
    }

    @Override
    public void translate(double x, double y) {
        line.setStartX(line.getStartX() + x);
        line.setStartY(line.getStartY() + y);
        line.setEndX(line.getEndX() + x);
        line.setEndY(line.getEndY() + y);
    }

    @Override
    public void resize(double x, double y, double newMouseX, double newMouseY, Edge edge) {
        line.setEndX(newMouseX);
        line.setEndY(newMouseY);
    }

    @Override
    public void scaleByFactor(double factor, Point2D origin) {
        line.setEndX(line.getEndX() * factor);
        line.setEndY(line.getEndY() * factor);
    }

    @Override
    public void rotate(double angle) {
        Rotate rotate = new Rotate();
        rotate.setAngle(angle);
        rotate.setPivotX(line.getStartX());
        rotate.setPivotY(line.getStartY());
        line.getTransforms().add(rotate);
    }

    @Override
    public void setColor(Color color) {
        line.setStroke(color);
        this.color = color;
    }

    @Override
    public void updateStartingPoint() {

    }

    @Override
    public byte[] serialize() {
        byte[] startCode = new byte[1];
        byte[] lineStartX = SerializeUtils.intToByte((int)line.getStartX());
        byte[] lineStartY = SerializeUtils.intToByte((int)line.getStartY());
        byte[] lineEndX = SerializeUtils.intToByte((int)line.getEndX());
        byte[] lineEndY = SerializeUtils.intToByte((int)line.getEndY());
        byte[] colorBytes = SerializeUtils.colorToByte(color);
        return SerializeUtils.joinBytes(List.of(startCode, lineStartX, lineStartY, lineEndX, lineEndY, colorBytes));
    }

    @Override
    public boolean edgeContainsPoint(double x, double y, Edge edge) {
        return false;
    }

    @Override
    public Bounds getBounds() {
        return line.getBoundsInParent();
    }
}
