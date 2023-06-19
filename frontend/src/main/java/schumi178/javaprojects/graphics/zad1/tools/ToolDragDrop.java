package schumi178.javaprojects.graphics.zad1.tools;

import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape;

public class ToolDragDrop implements Tool {

    private DrawableShape currentShape;

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
    }

    @Override
    public boolean onLeftDragged(ToolUseEvent event) {
        MouseEvent mouseEvent = event.getMouseEvent();
        double startingX = event.getOldMousePos().getX();
        double startingY = event.getOldMousePos().getY();
        if(currentShape != null) {
            currentShape.translate(mouseEvent.getX() - startingX, mouseEvent.getY() - startingY);
            return true;
        }
        return false;
    }

    @Override
    public void onLeftReleased(ToolUseEvent event) {
        currentShape = null;
    }
}
