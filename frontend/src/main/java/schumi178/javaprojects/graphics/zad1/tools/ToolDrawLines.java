package schumi178.javaprojects.graphics.zad1.tools;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableLine;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape.Edge;
import schumi178.javaprojects.graphics.zad1.util.ToolUtils;

public class ToolDrawLines implements Tool {

    private DrawableLine newLine;

    @Override
    public void onLeftPressed(ToolUseEvent event) {
        MouseEvent mouseEvent = event.getMouseEvent();
        newLine = new DrawableLine(new Line(mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getX(), mouseEvent.getY()));
        newLine.setColor(event.getColor());
        event.getShapeList().add(0, newLine);
    }

    @Override
    public boolean onLeftDragged(ToolUseEvent event) {
        return ToolUtils.resizeShape(event, newLine, Edge.BOTTOM_RIGHT);
    }

    @Override
    public void onLeftReleased(ToolUseEvent event) {
        newLine = null;
    }
}
