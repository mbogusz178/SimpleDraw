package schumi178.javaprojects.graphics.zad1.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import java.io.IOException;

public class RotateDialog extends Dialog<RotateData> {
    private double angle;
    private double originX;
    private double originY;

    public RotateDialog() {
        DialogPane pane = getDialogPane();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialog/rotateDialog.fxml"));
        try {
            pane.setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        RotateDialogContent controller = loader.getController();
        controller.setAngleListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()) {
                angle = 0;
                return;
            }
            if(newValue.charAt(0) == '-') {
                if(newValue.length() < 2) {
                    angle = 0;
                    return;
                }
            }

            try {
                angle = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                angle = 0;
            }
        });
        controller.setOriginXListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()) {
                originX = 0;
                return;
            }
            if(newValue.charAt(0) == '-') {
                if(newValue.length() < 2) {
                    originX = 0;
                    return;
                }
            }

            try {
                originX = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                originX = 0;
            }
        });
        controller.setOriginYListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()) {
                originY = 0;
                return;
            }
            if(newValue.charAt(0) == '-') {
                if(newValue.length() < 2) {
                    originY = 0;
                    return;
                }
            }

            try {
                originY = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                originY = 0;
            }
        });
        controller.setInputValueLabel("Podaj kąt obrotu:");
        getDialogPane().getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);
        setResultConverter(resultType -> {
            if(resultType == ButtonType.CANCEL) {
                return null;
            } else {
                return new RotateData(angle, originX, originY);
            }
        });
    }
}
