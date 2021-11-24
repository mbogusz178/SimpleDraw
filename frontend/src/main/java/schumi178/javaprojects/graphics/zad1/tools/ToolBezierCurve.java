package schumi178.javaprojects.graphics.zad1.tools;

import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.BezierCurve;

public class ToolBezierCurve implements Tool {
    private BezierCurve bezierCurve;

    @Override
    public void onLeftPressed(ToolUseEvent event) {

    }

    @Override
    public boolean onLeftDragged(ToolUseEvent event) {
        return false;
    }

    @Override
    public void onLeftReleased(ToolUseEvent event) {

    }
}
