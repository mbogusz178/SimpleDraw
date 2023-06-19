package schumi178.javaprojects.graphics.zad1.tools;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Ellipse;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableOval;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape.Edge;
import schumi178.javaprojects.graphics.zad1.util.ToolUtils;

public class ToolDrawOvals implements Tool {

    private DrawableOval newOval;

    @Override
    public void onLeftPressed(ToolUseEvent event) {
        MouseEvent mouseEvent = event.getMouseEvent();
        newOval = new DrawableOval(new Ellipse(mouseEvent.getX(), mouseEvent.getY(), 0, 0));
        newOval.setColor(event.getColor());
        event.getShapeList().add(0, newOval);
    }

    @Override
    public boolean onLeftDragged(ToolUseEvent event) {
        return ToolUtils.resizeShape(event, newOval, Edge.BOTTOM_RIGHT);
    }

    @Override
    public void onLeftReleased(ToolUseEvent event) {
        newOval = null;
    }
}
