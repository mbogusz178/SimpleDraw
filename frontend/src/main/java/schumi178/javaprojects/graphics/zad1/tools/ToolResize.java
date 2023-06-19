package schumi178.javaprojects.graphics.zad1.tools;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape.Edge;
import schumi178.javaprojects.graphics.zad1.util.ToolUtils;

import java.util.Iterator;
import java.util.function.Consumer;

public class ToolResize implements Tool {

    private final ObjectProperty<DrawableShape> currentShape;
    private Edge currentEdge = null;

    public ToolResize(Consumer<DrawableShape> updateFunction) {
        currentShape = new SimpleObjectProperty<>();
        currentShape.addListener((observable, oldValue, newValue) -> updateFunction.accept(newValue));
    }

    @Override
    public void onLeftPressed(ToolUseEvent event) {

    }

    @Override
    public boolean onLeftDragged(ToolUseEvent event) {
        return ToolUtils.resizeShape(event, currentShape.get(), currentEdge == null ? Edge.BOTTOM_RIGHT : currentEdge);
    }

    @Override
    public void onLeftMoved(ToolUseEvent event) {
        ObservableList<DrawableShape> shapes = event.getShapeList();
        MouseEvent mouseEvent = event.getMouseEvent();
        boolean isAtEdge = false;
        for(DrawableShape shape: shapes) {
            // najpierw rogi (inaczej rogi nie będą miały własnych kursorów, tylko brzegowe)
            for(Edge edge: DrawableShape.ORDERED_EDGES) {
                if(shape.edgeContainsPoint(mouseEvent.getX(), mouseEvent.getY(), edge)) {
                    Platform.runLater(() -> event.getCursor().set(edge.getCursor()));
                    currentShape.set(shape);
                    currentEdge = edge;
                    return;
                }
            }
        }
        Platform.runLater(() -> event.getCursor().set(Cursor.DEFAULT));
        currentShape.set(null);
    }

    @Override
    public void onLeftReleased(ToolUseEvent event) {
        DrawableShape shape = currentShape.get();
        if(shape != null)
            shape.updateStartingPoint();
    }
}
