package schumi178.javaprojects.graphics.zad1.tools;

import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape;

import java.util.function.Consumer;

public class ToolSelect implements Tool {

    private final Consumer<DrawableShape> updateSelection;

    public ToolSelect(Consumer<DrawableShape> updateSelection) {
        this.updateSelection = updateSelection;
    }

    @Override
    public void onLeftPressed(ToolUseEvent event) {
        MouseEvent mouseEvent = event.getMouseEvent();
        ObservableList<DrawableShape> shapes = event.getShapeList();
        for(DrawableShape shape: shapes) {
            if(shape.containsPoint(mouseEvent.getX(), mouseEvent.getY())) {
                updateSelection.accept(shape);
                return;
            }
        }
        updateSelection.accept(null);
    }

    @Override
    public boolean onLeftDragged(ToolUseEvent event) {
        return false;
    }

    @Override
    public void onLeftReleased(ToolUseEvent event) {

    }
}
