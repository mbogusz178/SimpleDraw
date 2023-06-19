package schumi178.javaprojects.graphics.zad1.tools;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableRectangle;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape.Edge;
import schumi178.javaprojects.graphics.zad1.util.ToolUtils;

public class ToolDrawRectangles implements Tool {

    private DrawableRectangle newRect;

    @Override
    public void onLeftPressed(ToolUseEvent event) {
        MouseEvent mouseEvent = event.getMouseEvent();
        newRect = new DrawableRectangle(new Rectangle(mouseEvent.getX(), mouseEvent.getY(), 0, 0));
        newRect.setColor(event.getColor());
        event.getShapeList().add(0, newRect);
    }

    @Override
    public boolean onLeftDragged(ToolUseEvent event) {
        return ToolUtils.resizeShape(event, newRect, Edge.BOTTOM_RIGHT);
    }

    @Override
    public void onLeftReleased(ToolUseEvent event) {
        newRect = null;
    }
}
