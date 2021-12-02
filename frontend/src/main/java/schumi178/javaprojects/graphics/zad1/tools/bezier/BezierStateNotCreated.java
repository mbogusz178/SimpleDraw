package schumi178.javaprojects.graphics.zad1.tools.bezier;

import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.BezierCurve;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape;
import schumi178.javaprojects.graphics.zad1.tools.ToolBezierCurve;

public class BezierStateNotCreated implements BezierState {

    private final ToolBezierCurve context;

    public BezierStateNotCreated(ToolBezierCurve context) {
        this.context = context;
    }

    @Override
    public BezierCurve leftClick(ToolUseEvent event, BezierCurve currentCurve) {
        BezierCurve curve = new BezierCurve();
        MouseEvent mouseEvent = event.getMouseEvent();
        ObservableList<DrawableShape> shapes = event.getShapeList();
        for(int i = 0; i < 2; i++)
            curve.addWaypoint(mouseEvent.getX(), mouseEvent.getY());
        curve.setColor(event.getColor());
        shapes.add(curve);
        context.setState(new BezierStateInProgress(context, event.getSpinnerValue()));
        return curve;
    }

    @Override
    public boolean allowTextBoxMethod() {
        return true;
    }
}
