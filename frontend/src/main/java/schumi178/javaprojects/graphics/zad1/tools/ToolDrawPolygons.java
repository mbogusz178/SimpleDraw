package schumi178.javaprojects.graphics.zad1.tools;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.DrawablePolygon;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape;

import java.util.function.Consumer;

public class ToolDrawPolygons implements Tool {

    private DrawablePolygon currentPolygon;
    private final Runnable updateFunction;

    public ToolDrawPolygons(Runnable updateFunction) {
        this.updateFunction = updateFunction;
    }

    @Override
    public void onLeftPressed(ToolUseEvent event) {
        MouseEvent mouseEvent = event.getMouseEvent();
        if(currentPolygon != null) {
            currentPolygon.addPoint(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
        } else {
            currentPolygon = new DrawablePolygon();
            currentPolygon.setColor(event.getColor());
            currentPolygon.addPoint(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
            event.getShapeList().add(currentPolygon);
        }
        Platform.runLater(() -> event.getCursor().set(Cursor.HAND));
        updateFunction.run();
    }

    @Override
    public boolean onLeftDragged(ToolUseEvent event) {
        return false;
    }

    @Override
    public void onLeftReleased(ToolUseEvent event) {

    }

    @Override
    public void onRightPressed(ToolUseEvent event) {
        currentPolygon = null;
        Platform.runLater(() -> event.getCursor().set(Cursor.DEFAULT));
    }
}
