package schumi178.javaprojects.graphics.zad1.tools;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.BezierCurve;
import schumi178.javaprojects.graphics.zad1.tools.bezier.BezierState;
import schumi178.javaprojects.graphics.zad1.tools.bezier.BezierStateNotCreated;

public class ToolBezierCurve implements Tool {

    private BezierCurve bezierCurve = null;
    private BezierState state = new BezierStateNotCreated(this);
    private final Runnable update;

    public ToolBezierCurve(Runnable update) {
        this.update = update;
    }

    public void setState(BezierState state) {
        this.state = state;
    }

    @Override
    public void onLeftPressed(ToolUseEvent event) {
        if(bezierCurve == null) {
            bezierCurve = new BezierCurve();
        }
        bezierCurve = state.leftClick(event, bezierCurve);
    }

    @Override
    public boolean onLeftDragged(ToolUseEvent event) {
        return false;
    }

    @Override
    public void onLeftMoved(ToolUseEvent event) {
        if(bezierCurve != null) {
            MouseEvent mouseEvent = event.getMouseEvent();
            bezierCurve.setLastWaypoint(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
            update.run();
        }
    }

    @Override
    public void onLeftReleased(ToolUseEvent event) {

    }
}
