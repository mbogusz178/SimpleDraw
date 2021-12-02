package schumi178.javaprojects.graphics.zad1.util;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MoveWaypointContent implements Initializable {

    @FXML
    private TextField xField;
    @FXML
    private TextField yField;

    public void setXFieldListener(ChangeListener<String> listener) {
        xField.textProperty().addListener(listener);
    }

    public void setYFieldListener(ChangeListener<String> listener) {
        yField.textProperty().addListener(listener);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        xField.setTextFormatter(new IntInputDialog.IntTextFormatter());
        yField.setTextFormatter(new IntInputDialog.IntTextFormatter());
    }
}
