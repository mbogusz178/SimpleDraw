package schumi178.javaprojects.graphics.zad1.util;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import java.io.IOException;

public class MoveWaypointDialog extends Dialog<Point2D> {
    private double xField;
    private double yField;

    public MoveWaypointDialog() {
        DialogPane pane = getDialogPane();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialog/moveWaypoint.fxml"));
        try {
            pane.setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        MoveWaypointContent controller = loader.getController();
        controller.setXFieldListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()) {
                xField = 0;
                return;
            }
            if(newValue.charAt(0) == '-') {
                if(newValue.length() < 2) {
                    xField = 0;
                    return;
                }
            }

            try {
                xField = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                xField = 0;
            }
        });
        controller.setYFieldListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()) {
                yField = 0;
                return;
            }
            if(newValue.charAt(0) == '-') {
                if(newValue.length() < 2) {
                    yField = 0;
                    return;
                }
            }

            try {
                yField = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                yField = 0;
            }
        });
        getDialogPane().getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);
        setResultConverter(resultType -> {
            if(resultType == ButtonType.CANCEL) {
                return null;
            } else {
                return new Point2D(xField, yField);
            }
        });
    }
}
