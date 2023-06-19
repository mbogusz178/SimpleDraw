package schumi178.javaprojects.graphics.zad1.tools;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape;
import schumi178.javaprojects.graphics.zad1.shapes.WritableText;

import java.util.function.Consumer;

public class ToolText implements Tool {

    private final ObjectProperty<WritableText> newText;
    private final Runnable update;

    public ToolText(Consumer<DrawableShape> updateSelection, Runnable update) {
        newText = new SimpleObjectProperty<>();
        this.update = update;
        newText.addListener((observable, oldValue, newValue) -> updateSelection.accept(newValue));
    }

    @Override
    public void onLeftPressed(ToolUseEvent event) {
        MouseEvent mouseEvent = event.getMouseEvent();
        WritableText text = new WritableText("", event.getColor(), mouseEvent.getX(), mouseEvent.getY(), 10, 10);
        text.textProperty().addListener((observable, oldValue, newValue) -> update.run());
        newText.set(text);
        event.getShapeList().add(0, newText.get());
    }

    @Override
    public boolean onLeftDragged(ToolUseEvent event) {
        return false;
    }

    @Override
    public void onLeftReleased(ToolUseEvent event) {

    }

    @Override
    public void onKeyPressed(ToolUseEvent event) {
        KeyEvent keyEvent = event.getKeyEvent();
        if(keyEvent.getCode() == KeyCode.ESCAPE) {
            newText.set(null);
        } else if(keyEvent.getCode() == KeyCode.BACK_SPACE) {
            newText.get().backspace();
        }
    }

    @Override
    public void onKeyTyped(ToolUseEvent event) {
        KeyEvent keyEvent = event.getKeyEvent();
        WritableText text = newText.get();
        if(text != null) {
            String c = keyEvent.getCharacter();
            if(!c.equals("\b"))
                text.appendText(c);
        }
    }
}
