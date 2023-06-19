package schumi178.javaprojects.graphics.zad1.tools;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.BezierCurve;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape;
import schumi178.javaprojects.graphics.zad1.util.MoveWaypointDialog;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class ToolModifyBezier implements Tool {

    private BezierCurve currentCurve;
    private int currentWaypointIndex;
    private final Consumer<BezierCurve> updateHovering;
    private final IntConsumer updateWaypointIndex;

    public ToolModifyBezier(Consumer<BezierCurve> updateHovering, IntConsumer updateWaypointIndex) {
        this.updateHovering = updateHovering;
        this.updateWaypointIndex = updateWaypointIndex;
    }

    @Override
    public void onLeftPressed(ToolUseEvent event) {

    }

    @Override
    public boolean onLeftDragged(ToolUseEvent event) {
        MouseEvent mouseEvent = event.getMouseEvent();
        if(currentWaypointIndex >= 0 && currentCurve != null) {
            currentCurve.setWaypoint(currentWaypointIndex, new Point2D(mouseEvent.getX(), mouseEvent.getY()));
            return true;
        }
        return false;
    }

    @Override
    public void onLeftMoved(ToolUseEvent event) {
        ObservableList<DrawableShape> shapes = event.getShapeList();
        MouseEvent mouseEvent = event.getMouseEvent();
        for(DrawableShape shape: shapes) {
            if(shape.containsPoint(mouseEvent.getX(), mouseEvent.getY())) {
                if(shape instanceof BezierCurve) {
                    BezierCurve curve = (BezierCurve) shape;
                    updateHovering.accept(curve);
                    currentCurve = curve;
                    List<Point2D> waypoints = currentCurve.getWayPoints();
                    for(int i = 0; i < waypoints.size(); i++) {
                        if(Math.abs(mouseEvent.getX() - waypoints.get(i).getX()) <= 10 &&
                        Math.abs(mouseEvent.getY() - waypoints.get(i).getY()) <= 10) {
                            currentWaypointIndex = i;
                            updateWaypointIndex.accept(i);
                            return;
                        }
                    }
                    currentWaypointIndex = -1;
                    updateWaypointIndex.accept(-1);
                    return;
                }
            }
        }
        currentCurve = null;
        updateHovering.accept(null);
    }

    @Override
    public void onLeftReleased(ToolUseEvent event) {

    }

    @Override
    public void onRightPressed(ToolUseEvent event) {
        if(currentWaypointIndex >= 0 && currentCurve != null) {
            MoveWaypointDialog dialog = new MoveWaypointDialog();
            dialog.setTitle("Przesuń punkt charakterystyczny");
            dialog.setHeaderText("Przesuwanie punktu charakterystycznego");

            Optional<Point2D> result = dialog.showAndWait();
            result.ifPresent(s -> currentCurve.setWaypoint(currentWaypointIndex, s));
        }
    }
}
