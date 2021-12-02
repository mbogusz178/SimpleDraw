package schumi178.javaprojects.graphics.zad1.tools;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.shapes.BezierCurve;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape;
import schumi178.javaprojects.graphics.zad1.tools.bezier.BezierState;
import schumi178.javaprojects.graphics.zad1.tools.bezier.BezierStateNotCreated;
import schumi178.javaprojects.graphics.zad1.util.IntInputDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToolBezierCurve implements Tool {

    private BezierCurve bezierCurve = null;
    private BezierState state = new BezierStateNotCreated(this);
    private final Runnable update;

    public ToolBezierCurve(Runnable update) {
        this.update = update;
    }

    public void setState(BezierState state) {
        this.state = state;
    }

    @Override
    public void onLeftPressed(ToolUseEvent event) {
        if(bezierCurve == null) {
            bezierCurve = new BezierCurve();
        }
        bezierCurve = state.leftClick(event, bezierCurve);
    }

    @Override
    public boolean onLeftDragged(ToolUseEvent event) {
        return false;
    }

    @Override
    public void onLeftMoved(ToolUseEvent event) {
        if(bezierCurve != null) {
            MouseEvent mouseEvent = event.getMouseEvent();
            bezierCurve.setLastWaypoint(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
            update.run();
        }
    }

    @Override
    public void onLeftReleased(ToolUseEvent event) {

    }

    @Override
    public void onRightPressed(ToolUseEvent event) {
        if(state.allowTextBoxMethod()) {
            int bezierDegree = event.getSpinnerValue();
            Dialog<List<Point2D>> dialog = new Dialog<>();
            dialog.setTitle("Tworzenie krzywej Beziera");
            dialog.setHeaderText("Podaj punkty charakterystyczne");
            DialogPane pane = dialog.getDialogPane();
            pane.getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);
            VBox vBox = new VBox(10);
            vBox.setPadding(new Insets(10));
            vBox.setPrefWidth(300);
            vBox.setPrefHeight(Region.USE_COMPUTED_SIZE);

            Label label = new Label("Podaj punkty charakterystyczne krzywej:");
            vBox.getChildren().add(label);
            List<TextField> textFieldsX = new ArrayList<>();
            List<TextField> textFieldsY = new ArrayList<>();
            for(int i = 0; i < bezierDegree + 1; i++) {
                HBox hBox = new HBox(10);

                Label xLabel = new Label("X:");
                xLabel.setPrefWidth(40);
                hBox.getChildren().add(xLabel);

                TextField xField = new TextField();
                xField.setPrefWidth(134);
                xField.setTextFormatter(new IntInputDialog.IntTextFormatter());
                textFieldsX.add(xField);
                hBox.getChildren().add(xField);

                Label yLabel = new Label("Y:");
                yLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);
                hBox.getChildren().add(yLabel);

                TextField yField = new TextField();
                yField.setPrefWidth(134);
                yField.setTextFormatter(new IntInputDialog.IntTextFormatter());
                textFieldsY.add(yField);
                hBox.getChildren().add(yField);
                vBox.getChildren().add(hBox);
            }
            pane.setContent(vBox);
            dialog.setResultConverter(buttonType -> {
                if(buttonType == ButtonType.CANCEL) {
                    return null;
                } else {
                    List<Point2D> result = new ArrayList<>();
                    try {
                        for (int i = 0; i < textFieldsX.size(); i++) {
                            result.add(new Point2D(Integer.parseInt(textFieldsX.get(i).getText()),
                                    Integer.parseInt(textFieldsY.get(i).getText())));
                        }
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Nieprawidłowe dane wejściowe.", ButtonType.OK);
                        alert.setTitle("Błąd");
                        alert.setHeaderText("Błąd");
                        alert.showAndWait();
                        return null;
                    }
                    return result;
                }
            });
            Optional<List<Point2D>> result = dialog.showAndWait();

            result.ifPresent(s -> {
                ObservableList<DrawableShape> shapes = event.getShapeList();
                BezierCurve curve = new BezierCurve();
                for(Point2D point: s) {
                    curve.addWaypoint(point.getX(), point.getY());
                }
                curve.setColor(event.getColor());
                shapes.add(curve);
                update.run();
            });
        }
    }
}
