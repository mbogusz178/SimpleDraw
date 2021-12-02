package schumi178.javaprojects.graphics.zad1.tools;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape;

import java.util.function.BiConsumer;

public class ToolRotate implements Tool {

    private DrawableShape currentShape;
    private double originX;
    private double originY;
    private final BiConsumer<Double, Double> updateOriginPoint;
    private double lastMouseLocationX;
    private double currentMouseLocationX;

    public ToolRotate(BiConsumer<Double, Double> updateOriginPoint) {
        this.updateOriginPoint = updateOriginPoint;
    }

    @Override
    public void onLeftPressed(ToolUseEvent event) {
        ObservableList<DrawableShape> shapes = event.getShapeList();
        MouseEvent mouseEvent = event.getMouseEvent();
        for (DrawableShape shape : shapes) {
            if (shape.containsPoint(mouseEvent.getX(), mouseEvent.getY())) {
                currentShape = shape;
                break;
            }
        }
        if(currentShape != null) {
            shapes.remove(currentShape);
            shapes.add(0, currentShape);
        }
        lastMouseLocationX = mouseEvent.getX();
        currentMouseLocationX = mouseEvent.getX();
    }

    @Override
    public boolean onLeftDragged(ToolUseEvent event) {
        if(currentShape != null) {
            lastMouseLocationX = currentMouseLocationX;
            MouseEvent mouseEvent = event.getMouseEvent();
            currentMouseLocationX = mouseEvent.getX();
            double xDiff = currentMouseLocationX - lastMouseLocationX;
            currentShape.translate(-originX, -originY);
            currentShape.rotate(xDiff);
            currentShape.translate(originX, originY);
            return true;
        }
        return false;
    }

    @Override
    public void onLeftReleased(ToolUseEvent event) {
        currentShape = null;
    }

    @Override
    public void onRightPressed(ToolUseEvent event) {
        MouseEvent mouseEvent = event.getMouseEvent();
        originX = mouseEvent.getX();
        originY = mouseEvent.getY();
        updateOriginPoint.accept(originX, originY);
    }
}
