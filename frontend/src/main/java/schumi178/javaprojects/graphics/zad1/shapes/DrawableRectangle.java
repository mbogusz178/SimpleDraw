package schumi178.javaprojects.graphics.zad1.shapes;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Point2D;
import javafx.geometry.VerticalDirection;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import schumi178.javaprojects.graphics.zad1.util.SerializeUtils;

import java.util.List;

public class DrawableRectangle implements DrawableShape {

    private final Rectangle rect;
    private Color color;
    private double theoreticalWidth;
    private double theoreticalHeight;
    private double startingPointX;
    private double startingPointY;
    private ChangeListener<Bounds> updateListener;

    public DrawableRectangle(Rectangle rect) {
        this.rect = rect;
        theoreticalWidth = rect.getWidth();
        theoreticalHeight = rect.getHeight();
        startingPointX = rect.getX();
        startingPointY = rect.getY();
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setTheoreticalWidth(double width) {
        theoreticalWidth = width;
    }

    public void setTheoreticalHeight(double height) {
        theoreticalHeight = height;
    }

    @Override
    public void updateStartingPoint() {
        startingPointX = rect.getX();
        startingPointY = rect.getY();
        theoreticalWidth = Math.abs(theoreticalWidth);
        theoreticalHeight = Math.abs(theoreticalHeight);
    }

    @Override
    public void setColor(Color color) {
        rect.setFill(color);
        this.color = color;
    }

    @Override
    public byte[] serialize() {
        byte[] startCode = new byte[1];
        startCode[0] = 2;
        byte[] rectX = SerializeUtils.intToByte((int)rect.getX());
        byte[] rectY = SerializeUtils.intToByte((int)rect.getY());
        byte[] rectWidth = SerializeUtils.intToByte((int)rect.getWidth());
        byte[] rectHeight = SerializeUtils.intToByte((int)rect.getHeight());
        byte[] theoreticalWidthBytes = SerializeUtils.intToByte((int)theoreticalWidth);
        byte[] theoreticalHeightBytes = SerializeUtils.intToByte((int)theoreticalHeight);
        byte[] startingPointXBytes = SerializeUtils.intToByte((int)startingPointX);
        byte[] startingPointYBytes = SerializeUtils.intToByte((int)startingPointY);
        byte[] colorBytes = SerializeUtils.colorToByte(color);
        return SerializeUtils.joinBytes(List.of(startCode, rectX, rectY, rectWidth, rectHeight, theoreticalWidthBytes, theoreticalHeightBytes, startingPointXBytes, startingPointYBytes, colorBytes));
    }

    @Override
    public Bounds getBounds() {
        return rect.getBoundsInParent();
    }

    @Override
    public boolean containsPoint(double x, double y) {
        return rect.contains(x, y);
    }

    @Override
    public void translate(double x, double y) {
        rect.setX(rect.getX() + x);
        rect.setY(rect.getY() + y);
    }

    @Override
    public void resize(double x, double y, double newMouseX, double newMouseY, Edge edge) {
        int factorX = edge.getHorizontal() == HorizontalDirection.RIGHT ? 1 : -1;
        int factorY = edge.getVertical() == VerticalDirection.DOWN ? 1 : -1;
        if(edge.getHorizontal() != null) {
            theoreticalWidth = theoreticalWidth + x * factorX;
            rect.setWidth(Math.abs(theoreticalWidth));
        }
        if(edge.getVertical() != null) {
            theoreticalHeight = theoreticalHeight + y * factorY;
            rect.setHeight(Math.abs(theoreticalHeight));
        }
        boolean isNegativeX = theoreticalWidth < 0;
        boolean isNegativeY = theoreticalHeight < 0;
        double newX, newY;
        if(edge.getHorizontal() == HorizontalDirection.RIGHT)
            newX = isNegativeX ? startingPointX + theoreticalWidth : startingPointX;
        else newX = isNegativeX ? newMouseX + theoreticalWidth : newMouseX;
        if(edge.getVertical() == VerticalDirection.DOWN)
            newY = isNegativeY ? startingPointY + theoreticalHeight : startingPointY;
        else newY = isNegativeY ? newMouseY + theoreticalHeight : newMouseY;
        if(edge.getHorizontal() != null)
            rect.setX(newX);
        if(edge.getVertical() != null)
            rect.setY(newY);
    }

    @Override
    public void scaleByFactor(double factor, Point2D origin) {
        theoreticalWidth *= factor;
        rect.setWidth(Math.abs(theoreticalWidth));
        theoreticalHeight *= factor;
        rect.setHeight(Math.abs(theoreticalHeight));
    }

    @Override
    public void rotate(double angle) {
        Rotate rotate = new Rotate();
        rotate.setAngle(angle);
        rect.setFill(color);
        rotate.setPivotX(rect.getX());
        rotate.setPivotY(rect.getY() + theoreticalHeight);
        rect.getTransforms().addAll(rotate);
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(rect.getFill());
        context.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }
}
