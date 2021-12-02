package schumi178.javaprojects.graphics.zad1.tools.bezier;

import javafx.scene.input.MouseEvent;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.BezierCurve;
import schumi178.javaprojects.graphics.zad1.tools.ToolBezierCurve;

public class BezierStateInProgress implements BezierState {

    private final ToolBezierCurve context;
    private final int curveDegree;
    private int currentCount = 1;

    public BezierStateInProgress(ToolBezierCurve context, int curveDegree) {
        this.context = context;
        this.curveDegree = curveDegree;
    }

    @Override
    public BezierCurve leftClick(ToolUseEvent event, BezierCurve currentCurve) {
        MouseEvent mouseEvent = event.getMouseEvent();
        if(currentCount < curveDegree) {
            currentCurve.addWaypoint(mouseEvent.getX(), mouseEvent.getY());
            currentCount++;
            return currentCurve;
        } else {
            context.setState(new BezierStateNotCreated(context));
            return null;
        }
    }

    @Override
    public boolean allowTextBoxMethod() {
        return false;
    }
}
