package schumi178.javaprojects.graphics.zad1;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import schumi178.javaprojects.graphics.zad1.event.ToolUseEvent;
import schumi178.javaprojects.graphics.zad1.exception.BrokenFileException;
import schumi178.javaprojects.graphics.zad1.io.FileLoader;
import schumi178.javaprojects.graphics.zad1.io.PortableAnymapReader;
import schumi178.javaprojects.graphics.zad1.io.PortableAnymapWriter;
import schumi178.javaprojects.graphics.zad1.shapes.BezierCurve;
import schumi178.javaprojects.graphics.zad1.shapes.DrawableShape;
import schumi178.javaprojects.graphics.zad1.threedimensions.RGBCubeApp;
import schumi178.javaprojects.graphics.zad1.tools.*;
import schumi178.javaprojects.graphics.zad1.util.*;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.util.*;

public class SimpleDrawController implements Initializable, ListChangeListener<DrawableShape>, ChangeListener<Number> {

    @FXML
    private Stage window;
    @FXML
    private Canvas canvas;
    @FXML
    private Pane canvasArea;
    @FXML
    private Pane toolbarFiller;

    @FXML
    private RadioButton toggleToolDragDrop;
    @FXML
    private RadioButton toggleToolOval;
    @FXML
    private RadioButton toggleToolRectangle;
    @FXML
    private RadioButton toggleToolTriangle;
    @FXML
    private RadioButton toggleToolLine;
    @FXML
    private RadioButton toggleToolFree;
    @FXML
    private RadioButton toggleToolResize;
    @FXML
    private RadioButton toggleToolText;
    @FXML
    private RadioButton toggleToolBezier;
    @FXML
    private RadioButton toggleToolSelect;
    @FXML
    private RadioButton toggleToolModifyBezier;
    @FXML
    private RadioButton toggleToolPolygon;
    @FXML
    private RadioButton toggleToolRotate;
    @FXML
    private RadioButton toggleToolScale;

    @FXML
    private ColorPicker drawingColorPicker;

    @FXML
    private Spinner<Integer> curveDegreeSpinner;

    private Tool currentTool;
    private double mousePressedX;
    private double mousePressedY;
    private final ObservableList<DrawableShape> shapes = FXCollections.observableArrayList();
    private DrawableShape selectedResizeShape = null;
    private DrawableShape selectedShape = null;
    private double originX = -1;
    private double originY = -1;
    private BezierCurve hoveredModifyingBezierCurve = null;
    private int currentBezierCurveWaypointIndex = -1;

    @FXML
    private void onCanvasMoved(MouseEvent event) {
        ToolUseEvent toolEvent = new ToolUseEvent(event, drawingColorPicker.getValue(), shapes, mousePressedX, mousePressedY, canvas.cursorProperty(), curveDegreeSpinner.getValue());
        currentTool.onLeftMoved(toolEvent);
        mousePressedX = event.getX();
        mousePressedY = event.getY();
    }

    @FXML
    private void onCanvasDragged(MouseEvent event) {
        ToolUseEvent toolEvent = new ToolUseEvent(event, drawingColorPicker.getValue(), shapes, mousePressedX, mousePressedY, canvas.cursorProperty(), curveDegreeSpinner.getValue());
        if(currentTool.onLeftDragged(toolEvent)) updateCanvas();
        mousePressedX = event.getX();
        mousePressedY = event.getY();
    }

    @FXML
    private void onCanvasPressed(MouseEvent event) {
        ToolUseEvent toolEvent = new ToolUseEvent(event, drawingColorPicker.getValue(), shapes, mousePressedX, mousePressedY, canvas.cursorProperty(), curveDegreeSpinner.getValue());
        if(event.getButton() == MouseButton.PRIMARY) {
            mousePressedX = event.getX();
            mousePressedY = event.getY();
            currentTool.onLeftPressed(toolEvent);
        } else if(event.getButton() == MouseButton.SECONDARY) {
            mousePressedX = event.getX();
            mousePressedY = event.getY();
            currentTool.onRightPressed(toolEvent);
        }
    }

    @FXML
    private void onKeyPressed(KeyEvent event) {
        ToolUseEvent toolEvent = new ToolUseEvent(event, drawingColorPicker.getValue(), shapes, mousePressedX, mousePressedY, canvas.cursorProperty());
        currentTool.onKeyPressed(toolEvent);
    }

    @FXML
    private void onKeyTyped(KeyEvent event) {
        ToolUseEvent toolEvent = new ToolUseEvent(event, drawingColorPicker.getValue(), shapes, mousePressedX, mousePressedY, canvas.cursorProperty());
        currentTool.onKeyTyped(toolEvent);
    }

    @FXML
    private void onCanvasReleased(MouseEvent event) {
        ToolUseEvent toolEvent = new ToolUseEvent(event, drawingColorPicker.getValue(), shapes, mousePressedX, mousePressedY, canvas.cursorProperty(), curveDegreeSpinner.getValue());
        if(event.getButton() == MouseButton.PRIMARY) {
            currentTool.onLeftReleased(toolEvent);
        }
    }

    private void updateCanvasWithResizeSelection(DrawableShape shape) {
        selectedResizeShape = shape;
        updateCanvas();
    }

    private void updateCanvasWithSelection(DrawableShape shape) {
        selectedShape = shape;
        updateCanvas();
    }

    private void updateWithHoveringBezierCurve(BezierCurve curve) {
        hoveredModifyingBezierCurve = curve;
        updateCanvas();
    }

    private void updateWithHoveringBezierWaypointIndex(int index) {
        currentBezierCurveWaypointIndex = index;
        updateCanvas();
    }

    private void updateOriginPoint(double originX, double originY) {
        this.originX = originX;
        this.originY = originY;
        updateCanvas();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentTool = new ToolDragDrop();
        HBox.setHgrow(toolbarFiller, Priority.ALWAYS);
        canvas.widthProperty().bind(canvasArea.widthProperty());
        canvas.heightProperty().bind(canvasArea.heightProperty());
        canvas.setFocusTraversable(true);
        canvas.addEventFilter(MouseEvent.ANY, (e) -> canvas.requestFocus());
        canvas.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) updateCanvasWithResizeSelection(null);
        });
        drawingColorPicker.setValue(Color.BLACK);
        shapes.addListener(this);
        window.widthProperty().addListener(this);
        window.heightProperty().addListener(this);

        window.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeEvent);

        Image imageDragDrop = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/dragdrop.png")));
        ImageView iconDragDrop = new ImageView(imageDragDrop);
        ObservableList<String> toolDragDropStyleClass = toggleToolDragDrop.getStyleClass();
        toolDragDropStyleClass.remove("radio-button");
        toolDragDropStyleClass.add("toggle-button");
        toggleToolDragDrop.setGraphic(iconDragDrop);

        Image imageOval = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/oval.png")));
        ImageView iconOval = new ImageView(imageOval);
        ObservableList<String> toolOvalStyleClass = toggleToolOval.getStyleClass();
        toolOvalStyleClass.remove("radio-button");
        toolOvalStyleClass.add("toggle-button");
        toggleToolOval.setGraphic(iconOval);

        Image imageRectangle = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/rectangle.png")));
        ImageView iconRectangle = new ImageView(imageRectangle);
        ObservableList<String> toolRectangleStyleClass = toggleToolRectangle.getStyleClass();
        toolRectangleStyleClass.remove("radio-button");
        toolRectangleStyleClass.add("toggle-button");
        toggleToolRectangle.setGraphic(iconRectangle);

        Image imageTriangle = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/triangle.png")));
        ImageView iconTriangle = new ImageView(imageTriangle);
        ObservableList<String> toolTriangleStyleClass = toggleToolTriangle.getStyleClass();
        toolTriangleStyleClass.remove("radio-button");
        toolTriangleStyleClass.add("toggle-button");
        toggleToolTriangle.setGraphic(iconTriangle);

        Image imageLine = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/line.png")));
        ImageView iconLine = new ImageView(imageLine);
        ObservableList<String> toolLineStyleClass = toggleToolLine.getStyleClass();
        toolLineStyleClass.remove("radio-button");
        toolLineStyleClass.add("toggle-button");
        toggleToolLine.setGraphic(iconLine);

        Image imageFree = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/free.png")));
        ImageView iconFree = new ImageView(imageFree);
        ObservableList<String> toolFreeStyleClass = toggleToolFree.getStyleClass();
        toolFreeStyleClass.remove("radio-button");
        toolFreeStyleClass.add("toggle-button");
        toggleToolFree.setGraphic(iconFree);

        Image imageResize = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/resize.png")));
        ImageView iconResize = new ImageView(imageResize);
        ObservableList<String> toolResizeStyleClass = toggleToolResize.getStyleClass();
        toolResizeStyleClass.remove("radio-button");
        toolResizeStyleClass.add("toggle-button");
        toggleToolResize.setGraphic(iconResize);

        Image imageText = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/text.png")));
        ImageView iconText = new ImageView(imageText);
        ObservableList<String> toolTextStyleClass = toggleToolText.getStyleClass();
        toolTextStyleClass.remove("radio-button");
        toolTextStyleClass.add("toggle-button");
        toggleToolText.setGraphic(iconText);

        Image imageBezier = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/bezier.png")));
        ImageView iconBezier = new ImageView(imageBezier);
        ObservableList<String> toolBezierStyleClass = toggleToolBezier.getStyleClass();
        toolBezierStyleClass.remove("radio-button");
        toolBezierStyleClass.add("toggle-button");
        toggleToolBezier.setGraphic(iconBezier);

        Image imageSelect = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/select.png")));
        ImageView iconSelect = new ImageView(imageSelect);
        ObservableList<String> toolSelectStyleClass = toggleToolSelect.getStyleClass();
        toolSelectStyleClass.remove("radio-button");
        toolSelectStyleClass.add("toggle-button");
        toggleToolSelect.setGraphic(iconSelect);

        Image imageModifyBezier = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/modify_bezier.png")));
        ImageView iconModifyBezier = new ImageView(imageModifyBezier);
        ObservableList<String> toolModifyBezierStyleClass = toggleToolModifyBezier.getStyleClass();
        toolModifyBezierStyleClass.remove("radio-button");
        toolModifyBezierStyleClass.add("toggle-button");
        toggleToolModifyBezier.setGraphic(iconModifyBezier);

        Image imagePolygon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/polygon.png")));
        ImageView iconPolygon = new ImageView(imagePolygon);
        ObservableList<String> toolPolygonStyleClass = toggleToolPolygon.getStyleClass();
        toolPolygonStyleClass.remove("radio-button");
        toolPolygonStyleClass.add("toggle-button");
        toggleToolPolygon.setGraphic(iconPolygon);

        Image imageRotate = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/rotate.png")));
        ImageView iconRotate = new ImageView(imageRotate);
        ObservableList<String> toolRotateStyleClass = toggleToolRotate.getStyleClass();
        toolRotateStyleClass.remove("radio-button");
        toolRotateStyleClass.add("toggle-button");
        toggleToolRotate.setGraphic(iconRotate);

        Image imageScale = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/scale.png")));
        ImageView iconScale = new ImageView(imageScale);
        ObservableList<String> toolScaleStyleClass = toggleToolScale.getStyleClass();
        toolScaleStyleClass.remove("radio-button");
        toolScaleStyleClass.add("toggle-button");
        toggleToolScale.setGraphic(iconScale);

//        curveDegreeLabel.visibleProperty().bind(toggleToolBezier.selectedProperty());
        curveDegreeSpinner.setDisable(toggleToolBezier.selectedProperty().getValue());

        curveDegreeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        curveDegreeSpinner.getEditor().setTextFormatter(new IntInputDialog.PositiveIntTextFormatter());
    }

    public void updateCanvas() {
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ListIterator<DrawableShape> it = shapes.listIterator(shapes.size());
        while(it.hasPrevious()) {
            DrawableShape shape = it.previous();
            shape.draw(ctx);
        }
        if(selectedResizeShape != null) {
            Bounds selectedBounds = selectedResizeShape.getBounds();
            ctx.setLineWidth(1.0);
            ctx.setLineDashes(2.0);
            ctx.setStroke(Color.BLACK);
            ctx.strokeRect(selectedBounds.getMinX(), selectedBounds.getMinY(), selectedBounds.getWidth(), selectedBounds.getHeight());
        }
        if(selectedShape != null) {
            Bounds selectedBounds = selectedShape.getBounds();
            ctx.setLineWidth(1.0);
            ctx.setLineDashes(2.0);
            ctx.setStroke(Color.FORESTGREEN);
            ctx.strokeRect(selectedBounds.getMinX(), selectedBounds.getMinY(), selectedBounds.getWidth(), selectedBounds.getHeight());
        }
        if(hoveredModifyingBezierCurve != null) {
            Bounds selectedBounds = hoveredModifyingBezierCurve.getBounds();
            ctx.setLineWidth(1.0);
            ctx.setLineDashes(2.0);
            ctx.setStroke(Color.BLACK);
            ctx.strokeRect(selectedBounds.getMinX(), selectedBounds.getMinY(), selectedBounds.getWidth(), selectedBounds.getHeight());
            List<Point2D> waypoints = hoveredModifyingBezierCurve.getWayPoints();
            for(int i = 0; i < waypoints.size(); i++) {
                if(i != currentBezierCurveWaypointIndex) {
                    Point2D waypoint = waypoints.get(i);
                    ctx.setLineWidth(1.0);
                    ctx.setLineDashes(2.0);
                    ctx.setStroke(Color.BLACK);
                    ctx.strokeRect(waypoint.getX() - 10, waypoint.getY() - 10, 20, 20);
                }
            }
            if(currentBezierCurveWaypointIndex >= 0) {
                Point2D waypoint = hoveredModifyingBezierCurve.getWayPoints().get(currentBezierCurveWaypointIndex);
                ctx.setLineWidth(1.0);
                ctx.setLineDashes(2.0);
                ctx.setStroke(Color.GOLDENROD);
                ctx.strokeRect(waypoint.getX() - 10, waypoint.getY() - 10, 20, 20);
            }
        }
        if(originX > -1 && originY > -1) {
            ctx.setLineWidth(1.0);
            ctx.setLineDashes(2.0);
            ctx.setStroke(Color.BLACK);
            ctx.strokeRect(originX - 5, originY - 5, 10, 10);
        }
    }

    @Override
    public void onChanged(Change<? extends DrawableShape> change) {
        updateCanvas();
    }

    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
        updateCanvas();
    }

    @FXML
    private void onSelectDragDrop() {
        currentTool = new ToolDragDrop();
    }

    @FXML
    private void onSelectOval() {
        currentTool = new ToolDrawOvals();
    }

    @FXML
    private void onSelectRectangle() {
        currentTool = new ToolDrawRectangles();
    }

    @FXML
    private void onSelectTriangle() {
        currentTool = new ToolDrawTriangles();
    }

    @FXML
    private void onSelectLine() {
        currentTool = new ToolDrawLines();
    }

    @FXML
    private void onSelectBezier() { currentTool = new ToolBezierCurve(this::updateCanvas); }

    @FXML
    private void onSelectSelect() { currentTool = new ToolSelect(this::updateCanvasWithSelection); }

    @FXML
    private void onSelectModifyBezier() { currentTool = new ToolModifyBezier(this::updateWithHoveringBezierCurve, this::updateWithHoveringBezierWaypointIndex); }

    @FXML
    private void onSelectPolygon() { currentTool = new ToolDrawPolygons(this::updateCanvas); }

    @FXML
    private void onSelectScale() {
        currentTool = new ToolScale(this::updateOriginPoint);
    }

    @FXML
    private void onSelectFree() {
        currentTool = new ToolDrawFree();
    }

    @FXML
    private void onSelectRotate() { currentTool = new ToolRotate(this::updateOriginPoint); }

    @FXML
    private void onSelectText() {
        currentTool = new ToolText(this::updateCanvasWithResizeSelection, this::updateCanvas);
    }

    @FXML
    private void onSelectResize() {
        currentTool = new ToolResize(this::updateCanvasWithResizeSelection);
    }

    @FXML
    private void moveByCoordinates() {

        if(selectedShape == null) {
            showAlertWarning("Nie można wykonać przekształcenia", "Nie wybrano kształtu!");
            return;
        }
        Dialog<Pair<String, String>> moveByCoordinatesDialog = new Dialog<>();
        moveByCoordinatesDialog.setTitle("Przesuwanie do punktu");
        moveByCoordinatesDialog.setHeaderText("Przesuwanie do punktu");

        ButtonType moveByCoordinatesButtonType = new ButtonType("Przesuń", ButtonBar.ButtonData.OK_DONE);
        moveByCoordinatesDialog.getDialogPane().getButtonTypes().addAll(moveByCoordinatesButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        TextField xCoordinate = new TextField();
        TextField yCoordinate = new TextField();

        gridPane.add(new Label("Współrzędna X"), 0, 0);
        gridPane.add(xCoordinate, 1, 0);
        gridPane.add(new Label("Współrzędna Y"), 0, 1);
        gridPane.add(yCoordinate, 1, 1);

        Node moveByCoordinatesButton = moveByCoordinatesDialog.getDialogPane().lookupButton(moveByCoordinatesButtonType);
        moveByCoordinatesButton.setDisable(true);

        xCoordinate.textProperty().addListener((observableValue, oldValue, newValue) ->
                moveByCoordinatesButton.setDisable(newValue.trim().isEmpty()));

        moveByCoordinatesDialog.getDialogPane().setContent(gridPane);

        moveByCoordinatesDialog.setResultConverter(buttonType ->
                new Pair<>(xCoordinate.getText(), yCoordinate.getText()));

        Optional<Pair<String, String>> result = moveByCoordinatesDialog.showAndWait();
        result.ifPresent(pair -> {
            int x = Integer.parseInt(xCoordinate.getText());
            int y = Integer.parseInt(yCoordinate.getText());

            selectedShape.translate(x, y);
            updateCanvas();
        });
    }

    @FXML
    private void scaleByFactor() {
        if(selectedShape == null) {
            showAlertWarning("Nie można wykonać przekształcenia", "Nie wybrano kształtu!");
            return;
        }
        ScaleDialog dialog = new ScaleDialog();
        dialog.setTitle("Skalowanie");
        dialog.setHeaderText("Skaluj");
        dialog.setContentText("Podaj mnożnik o który chcesz przeskalować figurę:");

        Optional<ScaleData> result = dialog.showAndWait();
        result.ifPresent(s -> {
            double factor = s.getScale();
            double originX = s.getOriginX();
            double originY = s.getOriginY();

            selectedShape.scaleByFactor(factor, new Point2D(originX, originY));
            updateCanvas();
        });
    }

    @FXML
    private void rotateByAngle() {
        if(selectedShape == null) {
            showAlertWarning("Nie można wykonać przekształcenia", "Nie wybrano kształtu!");
            return;
        }

        RotateDialog dialog = new RotateDialog();

        dialog.setTitle("Obracanie");
        dialog.setHeaderText("Obróć o ilość stopni");
        dialog.setContentText("Podaj kąt o który chcesz obrócić figurę:");

        Optional<RotateData> result = dialog.showAndWait();
        result.ifPresent(s -> {
            double angle = s.getAngle();
            double originX = s.getOriginX();
            double originY = s.getOriginY();

            selectedShape.translate(-originX, -originY);
            selectedShape.rotate(angle);
            selectedShape.translate(originX, originY);
            updateCanvas();
        });
    }

    @FXML
    private void onClear() {
        shapes.clear();
        updateCanvas();
    }

    private static void showAlertError(String contentText) {
        Alert alert = new Alert(AlertType.ERROR, contentText, ButtonType.OK);
        alert.setTitle("Błąd");
        alert.setHeaderText("Nie udało się otworzyć pliku");
        alert.show();
    }

    private static void showAlertWarning(String headerText, String contentText) {
        Alert alert = new Alert(AlertType.WARNING, contentText, ButtonType.OK);
        alert.setTitle("Błąd");
        alert.setHeaderText(headerText);
        alert.show();
    }

    private void showAlertBrokenFile() {
        showAlertError("Plik jest uszkodzony.");
    }

    private void showAlertGenericError() {
        showAlertError("Błąd zapisu na dysk.");
    }

    private void showAlertFileNotFound() { showAlertError("Nie znaleziono pliku.");}

    @FXML
    private void onOpenFile() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new ExtensionFilter("Plik SimpleDraw (.sdr)", "*.sdr"));
        chooser.setTitle("Otwórz obraz");
        File file = chooser.showOpenDialog(window);
        if(file != null) {
            try {
                FileLoader loader = new FileLoader(file);
                ObservableList<DrawableShape> newList = loader.load();
                loader.close();
                if(newList != null) {
                    shapes.clear();
                    shapes.addAll(newList);
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlertGenericError();
            } catch (BrokenFileException e) {
                e.printStackTrace();
                showAlertBrokenFile();
            }
        }
    }

    @FXML
    private boolean onSaveFile() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new ExtensionFilter("Plik SimpleDraw (.sdr)", "*.sdr"));
        chooser.setTitle("Zapisz obraz");
        File file = chooser.showSaveDialog(window);
        if(file != null) {
            try {
                OutputStream outputStream = new FileOutputStream(file);
                outputStream.write(10);
                outputStream.write(20);
                outputStream.write(21);
                for(DrawableShape shape: shapes) {
                    outputStream.write(shape.serialize());
                }
                outputStream.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @FXML
    private void onExportFile() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new ExtensionFilter("Obraz PNG (.png)", "*.png"),
                new ExtensionFilter("Plik PBM (.pbm)", "*.pbm"),
                new ExtensionFilter("Plik PGM (.pgm)", "*.pgm"),
                new ExtensionFilter("Plik PPM (.ppm)", "*.ppm"));
        chooser.setTitle("Eksportuj obraz");
        File file = chooser.showSaveDialog(window);
        if(file != null) {
            if(file.getName().endsWith(".png")) {
                WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(null, image), null), "png", file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(AlertType.CONFIRMATION, "Czy chcesz zapisać plik w formacie binarnym? Wybranie opcji \"Nie\" spowoduje zapisanie pliku w formacie tekstowym.", ButtonType.YES,  ButtonType.NO, ButtonType.CANCEL);
                alert.setTitle("Wybierz format pliku");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.isEmpty())
                    return;
                ButtonType button = result.get();
                boolean isBinary;
                if(button == ButtonType.YES)
                    isBinary = true;
                else if(button == ButtonType.NO)
                    isBinary = false;
                else return;

                WritableImage image = canvas.snapshot(null, new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight()));
                try {
                    PortableAnymapWriter writer = PortableAnymapWriter.getInstance(file, isBinary);
                    writer.write(image);
                    writer.close();
                } catch (FileNotFoundException e) {
                    showAlertFileNotFound();
                } catch (IllegalArgumentException e) {
                    showAlertError(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void onImportFile() {
        FileChooser chooser = new FileChooser();
        ExtensionFilter pbm = new ExtensionFilter("Plik PBM (.pbm)", "*.pbm");
        ExtensionFilter pgm = new ExtensionFilter("Plik PGM (.pgm)", "*.pgm");
        ExtensionFilter ppm = new ExtensionFilter("Plik PPM (.ppm)", "*.ppm");
        chooser.getExtensionFilters().addAll(pbm, pgm, ppm);
        chooser.setTitle("Importuj obraz");
        File file = chooser.showOpenDialog(window);
        if(file != null) {
            try {
                PortableAnymapReader reader = PortableAnymapReader.getInstance(file);
                Image image = reader.read();
                reader.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/importfileview.fxml"));
                Stage stage = loader.load();
                ImportFileViewController controller = loader.getController();
                controller.setImportedImage(image);
                stage.show();
            } catch (FileNotFoundException e) {
                showAlertFileNotFound();
            } catch (BrokenFileException e) {
                showAlertBrokenFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeEvent(WindowEvent event) {
        Alert alert = new Alert(AlertType.WARNING, "Czy chcesz zapisać obraz przed wyjściem?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.setTitle("Wyjście");
        alert.setHeaderText("Zapisz zmiany");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isEmpty()) {
            event.consume();
            return;
        }
        ButtonType button = result.get();
        if(button == ButtonType.YES) {
            if(!onSaveFile()) {
                event.consume();
            }
        } else if(button == ButtonType.CANCEL) {
            event.consume();
        }
    }

    @FXML
    private void openRGBCube() {
        RGBCubeApp app = new RGBCubeApp();
        app.run();
    }

    @FXML
    private void onChooseColor() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/colorpicker.fxml"));
        Stage colorPickerWindow = loader.load();
        colorPickerWindow.initModality(Modality.APPLICATION_MODAL);
        ColorPickerController controller = loader.getController();
        controller.setChosenColor(drawingColorPicker.getValue());
        colorPickerWindow.showAndWait();
        if(controller.getResult() == ButtonType.OK)
            drawingColorPicker.setValue(controller.getChosenColor());
    }

    @FXML
    private void onQuit() {
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}