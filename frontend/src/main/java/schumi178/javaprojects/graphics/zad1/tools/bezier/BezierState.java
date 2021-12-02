package schumi178.javaprojects.graphics.zad1.tools.bezier;

import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.BezierCurve;

public interface BezierState {
    BezierCurve leftClick(ToolUseEvent event, BezierCurve currentCurve);
    boolean allowTextBoxMethod();
}
