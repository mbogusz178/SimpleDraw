package schumi178.javaprojects.graphics.zad1.util;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape.Edge;

public class ToolUtils {
    public static boolean resizeShape(ToolUseEvent event, DrawableShape shape, Edge edge) {
        if(shape != null) {
            MouseEvent mouseEvent = event.getMouseEvent();
            Point2D oldMouse = event.getOldMousePos();
            shape.resize(mouseEvent.getX() - oldMouse.getX(), mouseEvent.getY() - oldMouse.getY(), mouseEvent.getX(), mouseEvent.getY(), edge);
            return true;
        }
        return false;
    }
}
