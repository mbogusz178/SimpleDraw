package schumi178.javaprojects.graphics.zad1.tools;

import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;

public interface Tool {
    void onLeftPressed(ToolUseEvent event);
    boolean onLeftDragged(ToolUseEvent event);
    void onLeftReleased(ToolUseEvent event);
    default void onRightPressed(ToolUseEvent event) {

    }
    default void onLeftMoved(ToolUseEvent event) {

    }
    default void onKeyPressed(ToolUseEvent event) {

    }
    default void onKeyTyped(ToolUseEvent event) {

    }
}
