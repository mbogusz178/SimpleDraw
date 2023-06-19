package schumi178.javaprojects.graphics.zad1.tools;

import javafx.scene.input.MouseEvent;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.FreeDrawing;

public class ToolDrawFree implements Tool {

    private FreeDrawing newDrawing;

    @Override
    public void onLeftPressed(ToolUseEvent event) {
        MouseEvent mouseEvent = event.getMouseEvent();
        newDrawing = new FreeDrawing(mouseEvent.getX(), mouseEvent.getY(), event.getColor());
        event.getShapeList().add(0, newDrawing);
    }

    @Override
    public boolean onLeftDragged(ToolUseEvent event) {
        if(newDrawing != null) {
            MouseEvent mouseEvent = event.getMouseEvent();
            newDrawing.addStroke(mouseEvent.getX(), mouseEvent.getY());
            return true;
        }
        return false;
    }

    @Override
    public void onLeftReleased(ToolUseEvent event) {
        newDrawing = null;
    }
}
