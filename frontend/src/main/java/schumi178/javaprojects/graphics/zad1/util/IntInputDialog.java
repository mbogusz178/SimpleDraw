package schumi178.javaprojects.graphics.zad1.util;

import javafx.scene.control.*;

public class IntInputDialog extends TextInputDialog {

    public static class PositiveIntTextFormatter extends TextFormatter<String> {

        public PositiveIntTextFormatter() {
            super(value -> {
                String text = value.getText();
                if(text.isEmpty()) {
                    return value;
                }
                try {
                    Integer.parseInt(value.getText());
                } catch (NumberFormatException e) {
                    return null;
                }
                return value;
            });
        }
    }

    public static class IntTextFormatter extends TextFormatter<String> {

        public IntTextFormatter() {
            super(value -> {
               String text = value.getText();
               if(text.isEmpty()) {
                   return value;
               }
               try {
                   if(value.getText().equals("-"))
                       return value;
                   Integer.parseInt(text);
               } catch (NumberFormatException e) {
                   return null;
               }

               return value;
            });
        }
    }

    public IntInputDialog() {
        super();
        DialogPane pane = getDialogPane();
        TextField input = getEditor();
        input.setTextFormatter(new PositiveIntTextFormatter());
        pane.lookupButton(ButtonType.OK).setDisable(true);
        input.textProperty().addListener((observable, oldValue, newValue) -> {
            pane.lookupButton(ButtonType.OK).setDisable(newValue.isEmpty());
        });
    }

    public IntInputDialog(int range) {
        super();
        DialogPane pane = getDialogPane();
        TextField input = getEditor();
        input.setTextFormatter(new PositiveIntTextFormatter());
        pane.lookupButton(ButtonType.OK).setDisable(true);
        input.textProperty().addListener((observable, oldValue, newValue) -> {
            pane.lookupButton(ButtonType.OK).setDisable(newValue.isEmpty() || Integer.parseInt(newValue) > range);
        });
    }
}
