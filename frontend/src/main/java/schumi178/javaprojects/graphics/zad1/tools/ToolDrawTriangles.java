package schumi178.javaprojects.graphics.zad1.tools;

import javafx.scene.input.MouseEvent;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape.Edge;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableTriangle;
import schumi178.javaprojects.graphics.zad1.util.ToolUtils;

public class ToolDrawTriangles implements Tool {

    private DrawableTriangle newTriangle;

    @Override
    public void onLeftPressed(ToolUseEvent event) {
        MouseEvent mouseEvent = event.getMouseEvent();
        newTriangle = new DrawableTriangle(mouseEvent.getX(), mouseEvent.getY(), 0, 0);
        newTriangle.setColor(event.getColor());
        event.getShapeList().add(0, newTriangle);
    }

    @Override
    public boolean onLeftDragged(ToolUseEvent event) {
        return ToolUtils.resizeShape(event, newTriangle, Edge.BOTTOM_RIGHT);
    }

    @Override
    public void onLeftReleased(ToolUseEvent event) {
        newTriangle = null;
    }
}
