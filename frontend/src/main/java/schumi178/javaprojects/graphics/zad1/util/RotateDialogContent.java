package schumi178.javaprojects.graphics.zad1.util;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RotateDialogContent implements Initializable {

    @FXML
    private TextField angle;
    @FXML
    private TextField originX;
    @FXML
    private TextField originY;
    @FXML
    private Label inputValueLabel;

    public void setAngleListener(ChangeListener<String> listener) {
        angle.textProperty().addListener(listener);
    }

    public void setOriginXListener(ChangeListener<String> listener) {
        originX.textProperty().addListener(listener);
    }

    public void setOriginYListener(ChangeListener<String> listener) {
        originY.textProperty().addListener(listener);
    }

    public void setInputValueLabel(String text) {
        inputValueLabel.setText(text);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        angle.setTextFormatter(new IntInputDialog.IntTextFormatter());
        originX.setTextFormatter(new IntInputDialog.IntTextFormatter());
        originY.setTextFormatter(new IntInputDialog.IntTextFormatter());
    }
}
