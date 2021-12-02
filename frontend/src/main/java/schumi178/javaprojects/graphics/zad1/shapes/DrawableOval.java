package schumi178.javaprojects.graphics.zad1.shapes;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Point2D;
import javafx.geometry.VerticalDirection;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Rotate;
import schumi178.javaprojects.graphics.zad1.util.SerializeUtils;

import java.util.List;

public class DrawableOval implements DrawableShape {

    private final Ellipse oval;
    private Color color;
    private double theoreticalRadiusX;
    private double theoreticalRadiusY;
    private ChangeListener<Bounds> updateListener;

    public DrawableOval(Ellipse oval) {
        this.oval = oval;
        theoreticalRadiusX = oval.getRadiusX();
        theoreticalRadiusY = oval.getRadiusY();
    }

    public void setTheoreticalRadiusX(double radius) {
        theoreticalRadiusX = radius;
    }

    public void setTheoreticalRadiusY(double radius) {
        theoreticalRadiusY = radius;
    }

    public Ellipse getOval() {
        return oval;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(oval.getFill());
        context.fillOval(oval.getCenterX() - oval.getRadiusX(), oval.getCenterY() - oval.getRadiusY(), oval.getRadiusX() * 2, oval.getRadiusY() * 2);
    }

    @Override
    public boolean containsPoint(double x, double y) {
        return oval.contains(x, y);
    }

    @Override
    public void translate(double x, double y) {
        oval.setCenterX(oval.getCenterX() + x);
        oval.setCenterY(oval.getCenterY() + y);
    }

    @Override
    public void resize(double x, double y, double newMouseX, double newMouseY, Edge edge) {
        int factorX = edge.getHorizontal() == HorizontalDirection.RIGHT ? 1 : -1;
        int factorY = edge.getVertical() == VerticalDirection.DOWN ? 1 : -1;
        if(edge.getHorizontal() != null) {
            theoreticalRadiusX = theoreticalRadiusX + x * factorX;
            oval.setRadiusX(Math.abs(theoreticalRadiusX) / 2.0);
            oval.setCenterX(oval.getCenterX() + x / 2.0);
        }
        if(edge.getVertical() != null) {
            theoreticalRadiusY = theoreticalRadiusY + y * factorY;
            oval.setRadiusY(Math.abs(theoreticalRadiusY) / 2.0);
            oval.setCenterY(oval.getCenterY() + y / 2.0);
        }
    }

    @Override
    public void scaleByFactor(double factor, Point2D origin) {
        theoreticalRadiusX *= factor;
        oval.setRadiusX(Math.abs(theoreticalRadiusX) / 2.0);
        theoreticalRadiusY *= factor;
        oval.setRadiusY(Math.abs(theoreticalRadiusY) / 2.0);
        oval.setCenterX(oval.getCenterX());
        oval.setCenterY(oval.getCenterY());
    }

    @Override
    public void rotate(double angle) {
        Rotate rotate = new Rotate();
        rotate.setAngle(angle);
//        rotate.setPivotX(line.getStartX());
//        rotate.setPivotY(line.getStartY());
        oval.getTransforms().addAll(rotate);
    }

    @Override
    public void setColor(Color color) {
        oval.setFill(color);
        this.color = color;
    }

    @Override
    public void updateStartingPoint() {
        theoreticalRadiusX = Math.abs(theoreticalRadiusX);
        theoreticalRadiusY = Math.abs(theoreticalRadiusY);
    }

    @Override
    public byte[] serialize() {
        byte[] startCode = new byte[1];
        startCode[0] = 1;
        byte[] ovalCenterX = SerializeUtils.intToByte((int)oval.getCenterX());
        byte[] ovalCenterY = SerializeUtils.intToByte((int)oval.getCenterY());
        byte[] ovalRadiusX = SerializeUtils.intToByte((int)oval.getRadiusX());
        byte[] ovalRadiusY = SerializeUtils.intToByte((int)oval.getRadiusY());
        byte[] theoreticalRadiusXBytes = SerializeUtils.intToByte((int)theoreticalRadiusX);
        byte[] theoreticalRadiusYBytes = SerializeUtils.intToByte((int)theoreticalRadiusY);
        byte[] colorBytes = SerializeUtils.colorToByte(color);
        return SerializeUtils.joinBytes(List.of(startCode, ovalCenterX, ovalCenterY, ovalRadiusX, ovalRadiusY, theoreticalRadiusXBytes, theoreticalRadiusYBytes, colorBytes));
    }

    @Override
    public Bounds getBounds() {
        return oval.getBoundsInParent();
    }
}
