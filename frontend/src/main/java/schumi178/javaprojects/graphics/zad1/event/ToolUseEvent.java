package schumi178.javaprojects.graphics.zad1.event;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape;

public class ToolUseEvent {
    private final Color color;
    private final ObservableList<DrawableShape> shapeList;
    private final Point2D oldMousePos;
    private final MouseEvent mouseEvent;
    private final KeyEvent keyEvent;
    private final int spinnerValue;
    private final ObjectProperty<Cursor> cursor;

    public Color getColor() {
        return color;
    }

    public ObservableList<DrawableShape> getShapeList() {
        return shapeList;
    }

    public Point2D getOldMousePos() {
        return oldMousePos;
    }

    public ObjectProperty<Cursor> getCursor() {
        return cursor;
    }

    public MouseEvent getMouseEvent() {
        return mouseEvent;
    }

    public KeyEvent getKeyEvent() {
        return keyEvent;
    }

    public int getSpinnerValue() {
        return spinnerValue;
    }

    public ToolUseEvent(KeyEvent keyEvent, Color color, ObservableList<DrawableShape> shapeList, double oldMouseX, double oldMouseY, ObjectProperty<Cursor> cursor) {
        this.keyEvent = keyEvent;
        this.color = color;
        this.shapeList = shapeList;
        this.oldMousePos = new Point2D(oldMouseX, oldMouseY);
        this.cursor = cursor;
        mouseEvent = null;
        spinnerValue = 0;
    }

    public ToolUseEvent(MouseEvent mouseEvent, Color color, ObservableList<DrawableShape> shapeList, double oldMouseX, double oldMouseY, ObjectProperty<Cursor> cursor, int spinnerValue) {
        this.mouseEvent = mouseEvent;
        this.color = color;
        this.shapeList = shapeList;
        this.oldMousePos = new Point2D(oldMouseX, oldMouseY);
        this.cursor = cursor;
        this.spinnerValue = spinnerValue;
        keyEvent = null;
    }
}
