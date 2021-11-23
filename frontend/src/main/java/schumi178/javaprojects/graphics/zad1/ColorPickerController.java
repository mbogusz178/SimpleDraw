package schumi178.javaprojects.graphics.zad1;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import schumi178.javaprojects.graphics.zad1.util.CMYKColor;

import java.net.URL;
import java.util.ResourceBundle;

public class ColorPickerController implements Initializable {
    @FXML
    private Stage window;
    @FXML
    private Canvas saturationValueRect;
    @FXML
    private Canvas hueSlider;
    @FXML
    private Slider sliderHue;
    @FXML
    private Slider sliderSaturation;
    @FXML
    private Slider sliderValue;
    @FXML
    private Canvas currentColorCanvas;
    @FXML
    private Label hueValueLabel;
    @FXML
    private Label saturationValueLabel;
    @FXML
    private Label valueValueLabel;

    @FXML
    private Canvas redGreenRect;
    @FXML
    private Canvas blueSlider;
    @FXML
    private Slider sliderRed;
    @FXML
    private Slider sliderGreen;
    @FXML
    private Slider sliderBlue;
    @FXML
    private Canvas currentColorCanvas1;
    @FXML
    private Label redValueLabel;
    @FXML
    private Label greenValueLabel;
    @FXML
    private Label blueValueLabel;

    @FXML
    private Canvas cyanMagentaRect;
    @FXML
    private Canvas yellowBlackRect;
    @FXML
    private Slider sliderCyan;
    @FXML
    private Slider sliderMagenta;
    @FXML
    private Slider sliderYellow;
    @FXML
    private Slider sliderBlack;
    @FXML
    private Canvas currentColorCanvas2;
    @FXML
    private Label cyanValueLabel;
    @FXML
    private Label magentaValueLabel;
    @FXML
    private Label yellowValueLabel;
    @FXML
    private Label blackValueLabel;

    @FXML
    private TabPane colorSystemTabs;
    @FXML
    private Tab hsvTab;
    @FXML
    private Tab rgbTab;
    @FXML
    private Tab cmykTab;

    private static final double CROSSHAIR_SIZE = 5;

    private final DoubleProperty hue = new SimpleDoubleProperty();
    private final DoubleProperty saturation = new SimpleDoubleProperty();
    private final DoubleProperty brightness = new SimpleDoubleProperty();

    private final DoubleProperty red = new SimpleDoubleProperty();
    private final DoubleProperty green = new SimpleDoubleProperty();
    private final DoubleProperty blue = new SimpleDoubleProperty();

    private final DoubleProperty cyan = new SimpleDoubleProperty();
    private final DoubleProperty magenta = new SimpleDoubleProperty();
    private final DoubleProperty yellow = new SimpleDoubleProperty();
    private final DoubleProperty black = new SimpleDoubleProperty();

    private ChangeListener<? super Number> hueListener;
    private ChangeListener<? super Number> saturationListener;
    private ChangeListener<? super Number> valueListener;

    private ChangeListener<? super Number> redListener;
    private ChangeListener<? super Number> greenListener;
    private ChangeListener<? super Number> blueListener;

    private ChangeListener<? super Number> cyanListener;
    private ChangeListener<? super Number> magentaListener;
    private ChangeListener<? super Number> yellowListener;
    private ChangeListener<? super Number> blackListener;

    private Color chosenColor = Color.BLACK;
    private ButtonType buttonType = ButtonType.CANCEL;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hueListener = (observable, oldValue, newValue) -> {
            chosenColor = Color.hsb(newValue.doubleValue() / 255 * 360, saturation.get() / 255, brightness.get() / 255);
            updateSaturationValueRect();
            updateCurrentColorRect();
        };
        saturationListener = (observable, oldValue, newValue) -> {
            chosenColor = Color.hsb(hue.get() / 255 * 360, newValue.doubleValue() / 255, brightness.get() / 255);
            updateSaturationValueRect();
            updateCurrentColorRect();
        };
        valueListener = (observable, oldValue, newValue) -> {
            chosenColor = Color.hsb(hue.get() / 255 * 360, saturation.get() / 255, newValue.doubleValue() / 255);
            updateSaturationValueRect();
            updateCurrentColorRect();
        };
        hue.addListener(hueListener);
        saturation.addListener(saturationListener);
        brightness.addListener(valueListener);
        initHueSlider();

        redListener = (observable, oldValue, newValue) -> {
            chosenColor = Color.rgb(newValue.intValue(), (int)green.get(), (int)blue.get());
            updateRedGreenRect();
            updateCurrentColorRect();
        };
        greenListener = (observable, oldValue, newValue) -> {
            chosenColor = Color.rgb((int)red.get(), newValue.intValue(), (int)blue.get());
            updateRedGreenRect();
            updateCurrentColorRect();
        };
        blueListener = (observable, oldValue, newValue) -> {
            chosenColor = Color.rgb((int)red.get(), (int)green.get(), newValue.intValue());
            updateRedGreenRect();
            updateCurrentColorRect();
        };
        red.addListener(redListener);
        green.addListener(greenListener);
        blue.addListener(blueListener);
        initBlueSlider();

        cyanListener = (observable, oldValue, newValue) -> {
            chosenColor = new CMYKColor(newValue.doubleValue(), magenta.get(), yellow.get(), black.get()).getRGB();
            updateCyanMagentaRect();
            updateYellowBlackRect();
            updateCurrentColorRect();
        };
        magentaListener = (observable, oldValue, newValue) -> {
            chosenColor = new CMYKColor(cyan.get(), newValue.doubleValue(), yellow.get(), black.get()).getRGB();
            updateCyanMagentaRect();
            updateYellowBlackRect();
            updateCurrentColorRect();
        };
        yellowListener = (observable, oldValue, newValue) -> {
            chosenColor = new CMYKColor(cyan.get(), magenta.get(), newValue.doubleValue(), black.get()).getRGB();
            updateCyanMagentaRect();
            updateYellowBlackRect();
            updateCurrentColorRect();
        };
        blackListener = (observable, oldValue, newValue) -> {
            chosenColor = new CMYKColor(cyan.get(), magenta.get(), yellow.get(), newValue.doubleValue()).getRGB();
            updateCyanMagentaRect();
            updateYellowBlackRect();
            updateCurrentColorRect();
        };
        cyan.addListener(cyanListener);
        magenta.addListener(magentaListener);
        yellow.addListener(yellowListener);
        black.addListener(blackListener);

        hue.addListener((observable, oldValue, newValue) -> {
            sliderHue.setValue((double)newValue / 255 * 100);
        });
        saturation.addListener((observable, oldValue, newValue) -> {
            sliderSaturation.setValue((double)newValue / 255 * 100);
        });
        brightness.addListener((observable, oldValue, newValue) -> {
            sliderValue.setValue((double)newValue / 255 * 100);
        });

        red.addListener((observable, oldValue, newValue) -> {
            sliderRed.setValue((double)newValue / 255 * 100);
        });
        green.addListener((observable, oldValue, newValue) -> {
            sliderGreen.setValue((double)newValue / 255 * 100);
        });
        blue.addListener((observable, oldValue, newValue) -> {
            sliderBlue.setValue((double)newValue / 255 * 100);
        });

        cyan.addListener((observable, oldValue, newValue) -> {
            sliderCyan.setValue((double) newValue / 255 * 100);
        });
        magenta.addListener((observable, oldValue, newValue) -> {
            sliderMagenta.setValue((double) newValue / 255 * 100);
        });
        yellow.addListener((observable, oldValue, newValue) -> {
            sliderYellow.setValue((double) newValue / 255 * 100);
        });
        black.addListener((observable, oldValue, newValue) -> {
            sliderBlack.setValue((double) newValue / 255 * 100);
        });

        sliderHue.valueProperty().addListener((observable, oldValue, newValue) ->
                hue.set((double)newValue / 100 * 255));
        sliderSaturation.valueProperty().addListener((observable, oldValue, newValue) ->
                saturation.set((double)newValue / 100 * 255));
        sliderValue.valueProperty().addListener((observable, oldValue, newValue) ->
                brightness.set((double)newValue / 100 * 255));

        sliderRed.valueProperty().addListener((observable, oldValue, newValue) ->
                red.set((double)newValue / 100 * 255));
        sliderGreen.valueProperty().addListener((observable, oldValue, newValue) ->
                green.set((double)newValue / 100 * 255));
        sliderBlue.valueProperty().addListener((observable, oldValue, newValue) ->
                blue.set((double)newValue / 100 * 255));

        sliderCyan.valueProperty().addListener((observable, oldValue, newValue) ->
                cyan.set((double)newValue / 100 * 255));
        sliderMagenta.valueProperty().addListener((observable, oldValue, newValue) ->
                magenta.set((double)newValue / 100 * 255));
        sliderYellow.valueProperty().addListener((observable, oldValue, newValue) ->
                yellow.set((double)newValue / 100 * 255));
        sliderBlack.valueProperty().addListener((observable, oldValue, newValue) ->
                black.set((double)newValue / 100 * 255));

        hueValueLabel.textProperty().bind(hue.asString());
        saturationValueLabel.textProperty().bind(saturation.asString());
        valueValueLabel.textProperty().bind(brightness.asString());

        redValueLabel.textProperty().bind(red.asString());
        greenValueLabel.textProperty().bind(green.asString());
        blueValueLabel.textProperty().bind(blue.asString());

        cyanValueLabel.textProperty().bind(cyan.asString());
        magentaValueLabel.textProperty().bind(magenta.asString());
        yellowValueLabel.textProperty().bind(yellow.asString());
        blackValueLabel.textProperty().bind(black.asString());

        colorSystemTabs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == rgbTab)
                updateRGB(chosenColor);
            else if(newValue == hsvTab)
                updateHSV(chosenColor);
            else if(newValue == cmykTab) {
                updateCMYK(chosenColor);
            }
        });
    }

    private void updateRGB(Color color) {
        red.removeListener(redListener);
        green.removeListener(greenListener);
        blue.removeListener(blueListener);
        red.set(color.getRed() * 255);
        green.set(color.getGreen() * 255);
        blue.set(color.getBlue() * 255);
        red.addListener(redListener);
        green.addListener(greenListener);
        blue.addListener(blueListener);
        updateRedGreenRect();
        updateCurrentColorRect();
    }

    private void updateHSV(Color color) {
        hue.removeListener(hueListener);
        saturation.removeListener(saturationListener);
        brightness.removeListener(valueListener);
        hue.set(color.getHue() / 360 * 255);
        saturation.set(color.getSaturation() * 255);
        brightness.set(color.getBrightness() * 255);
        hue.addListener(hueListener);
        saturation.addListener(saturationListener);
        brightness.addListener(valueListener);
        updateSaturationValueRect();
        updateCurrentColorRect();
    }

    private void updateCMYK(Color color) {
        CMYKColor cmyk = new CMYKColor(color);
        cyan.removeListener(cyanListener);
        magenta.removeListener(magentaListener);
        yellow.removeListener(yellowListener);
        black.removeListener(blackListener);
        cyan.set(cmyk.getCyan());
        magenta.set(cmyk.getMagenta());
        yellow.set(cmyk.getYellow());
        black.set(cmyk.getBlack());
        cyan.addListener(cyanListener);
        magenta.addListener(magentaListener);
        yellow.addListener(yellowListener);
        black.addListener(blackListener);
        updateCyanMagentaRect();
        updateYellowBlackRect();
        updateCurrentColorRect();
    }

    private void setCurrentHue(double hue) {
        this.hue.set(hue);
    }

    private void setCurrentBlue(double blue) {
        this.blue.set(blue);
    }

    private void setCurrentYellowBlack(double yellow, double black) {
        this.yellow.set(yellow);
        this.black.set(black);
    }

    private void setCurrentSaturationValue(double saturation, double value) {
        this.saturation.set(saturation);
        this.brightness.set(value);
    }

    private void setCurrentRedGreen(double red, double green) {
        this.red.set(red);
        this.green.set(green);
    }

    private void setCurrentCyanMagenta(double cyan, double magenta) {
        this.cyan.set(cyan);
        this.magenta.set(magenta);
    }

    private void initHueSlider() {
        GraphicsContext ctx = hueSlider.getGraphicsContext2D();
        ctx.clearRect(0, 0, hueSlider.getWidth(), hueSlider.getHeight());
        for(double i = 0; i < 256; i++) {
            for(double j = 0; j < hueSlider.getHeight(); j++) {
                ctx.setFill(Color.hsb(i / 255 * 360, 1.0, 1.0));
                ctx.fillRect(i, j, 1, 1);
            }
        }
    }

    private void initBlueSlider() {
        GraphicsContext ctx = blueSlider.getGraphicsContext2D();
        ctx.clearRect(0, 0, blueSlider.getWidth(), blueSlider.getHeight());
        for(int i = 0; i < 256; i++) {
            for(int j = 0; j < blueSlider.getWidth(); j++) {
                ctx.setFill(Color.rgb(0, 0, i));
                ctx.fillRect(i, j, 1, 1);
            }
        }
    }

    private void updateRedGreenRect() {
        GraphicsContext ctx = redGreenRect.getGraphicsContext2D();
        ctx.clearRect(0, 0, redGreenRect.getWidth(), redGreenRect.getHeight());
        for(int i = 0; i < 256; i++) {
            for(int j = 0; j < 256; j++) {
                ctx.setFill(Color.rgb(i, j, (int)blue.get()));
                ctx.fillRect(i, j, 1, 1);
            }
        }
        ctx.setStroke(Color.rgb((int)red.get(), (int)green.get(), (int)blue.get()).invert());
        ctx.strokeLine(red.get() - CROSSHAIR_SIZE, green.get(), red.get() + CROSSHAIR_SIZE, green.get());
        ctx.strokeLine(red.get(), green.get() - CROSSHAIR_SIZE, red.get(), green.get() + CROSSHAIR_SIZE);
    }

    private void updateSaturationValueRect() {
        GraphicsContext ctx = saturationValueRect.getGraphicsContext2D();
        ctx.clearRect(0, 0, saturationValueRect.getWidth(), saturationValueRect.getHeight());
        for(double i = 0; i < 256; i++) {
            for(double j = 0; j < 256; j++) {
                ctx.setFill(Color.hsb(hue.get() / 255 * 360, i / 255, 1.0 - (j / 255)));
                ctx.fillRect(i, j, 1, 1);
            }
        }
        ctx.setStroke(Color.hsb(hue.get() / 255 * 360, saturation.get() / 255, brightness.get() / 255).invert());
        ctx.strokeLine(saturation.get() - CROSSHAIR_SIZE, 255 - brightness.get(), saturation.get() + CROSSHAIR_SIZE, 255 - brightness.get());
        ctx.strokeLine(saturation.get(), 255 - brightness.get() - CROSSHAIR_SIZE, saturation.get(), 255 - brightness.get() + CROSSHAIR_SIZE);
    }

    private void updateCyanMagentaRect() {
        GraphicsContext ctx = cyanMagentaRect.getGraphicsContext2D();
        ctx.clearRect(0, 0, cyanMagentaRect.getWidth(), cyanMagentaRect.getHeight());
        for(double i = 0; i < 256; i++) {
            for(double j = 0; j < 256; j++) {
                ctx.setFill(new CMYKColor(i, j, yellow.get(), black.get()).getRGB());
                ctx.fillRect(i, j, 1, 1);
            }
        }
        ctx.setStroke(new CMYKColor((int)cyan.get(), (int)magenta.get(), (int)yellow.get(), (int)black.get()).getRGB().invert());
        ctx.strokeLine(cyan.get() - CROSSHAIR_SIZE, magenta.get(), cyan.get() + CROSSHAIR_SIZE, magenta.get());
        ctx.strokeLine(cyan.get(), magenta.get() - CROSSHAIR_SIZE, cyan.get(), magenta.get() + CROSSHAIR_SIZE);
    }

    private void updateYellowBlackRect() {
        GraphicsContext ctx =  yellowBlackRect.getGraphicsContext2D();
        ctx.clearRect(0, 0, yellowBlackRect.getWidth(), yellowBlackRect.getHeight());
        for(double i = 0; i < 256; i++) {
            for(double j = 0; j < 256; j++) {
                ctx.setFill(new CMYKColor(cyan.get(), magenta.get(), i, j).getRGB());
                ctx.fillRect(i, j, 1, 1);
            }
        }
        ctx.setStroke(new CMYKColor((int)cyan.get(), (int)magenta.get(), (int)yellow.get(), (int)black.get()).getRGB().invert());
        ctx.strokeLine(yellow.get() - CROSSHAIR_SIZE, black.get(), yellow.get() + CROSSHAIR_SIZE, black.get());
        ctx.strokeLine(yellow.get(), black.get() - CROSSHAIR_SIZE, yellow.get(), black.get() + CROSSHAIR_SIZE);
    }

    public ButtonType getResult() {
        return buttonType;
    }

    @FXML
    private void onHueSliderInteraction(MouseEvent event) {
        double hue = event.getX();
        if(hue < 0)
            hue = 0;
        if(hue > 255)
            hue = 255;
        setCurrentHue(hue);
    }

    @FXML
    private void onSaturationValueRectInteraction(MouseEvent event) {
        double saturation = event.getX();
        double value = 255 - event.getY();
        if(saturation < 0)
            saturation = 0;
        if(saturation > 255)
            saturation = 255;
        if(value < 0)
            value = 0;
        if(value > 255)
            value = 255;
        setCurrentSaturationValue(saturation, value);
    }

    @FXML
    private void onBlueSliderInteraction(MouseEvent event) {
        double blue = event.getX();
        if(blue < 0)
            blue = 0;
        if(blue > 255)
            blue = 255;
        setCurrentBlue(blue);
    }

    @FXML
    private void onRedGreenRectInteraction(MouseEvent event) {
        double red = event.getX();
        double green = event.getY();
        if(red < 0)
            red = 0;
        if(red > 255)
            red = 255;
        if(green < 0)
            green = 0;
        if(green > 255)
            green = 255;
        setCurrentRedGreen(red, green);
    }

    @FXML
    private void onCyanMagentaRectInteraction(MouseEvent event) {
        double cyan = event.getX();
        double magenta = event.getY();
        if(cyan < 0)
            cyan = 0;
        if(cyan > 255)
            cyan = 255;
        if(magenta < 0)
            magenta = 0;
        if(magenta > 255)
            magenta = 255;
        setCurrentCyanMagenta(cyan, magenta);
    }

    @FXML
    private void onYellowBlackRectInteraction(MouseEvent event) {
        double yellow = event.getX();
        double black = event.getY();
        if(yellow < 0)
            yellow = 0;
        if(yellow > 255)
            yellow = 255;
        if(black < 0)
            black = 0;
        if(black > 255)
            black = 255;
        setCurrentYellowBlack(yellow, black);
    }

    public Color getChosenColor() {
        return chosenColor;
    }

    public void setChosenColor(Color color) {
        hue.set(color.getHue() / 360 * 255);
        saturation.set(color.getSaturation() * 255);
        brightness.set(color.getBrightness() * 255);
        red.set(color.getRed() * 255);
        green.set(color.getGreen() * 255);
        blue.set(color.getBlue() * 255);
        CMYKColor cmyk = new CMYKColor(color);
        cyan.set(cmyk.getCyan());
        magenta.set(cmyk.getMagenta());
        yellow.set(cmyk.getYellow());
        black.set(cmyk.getBlack());
        updateSaturationValueRect();
        updateRedGreenRect();
        updateCyanMagentaRect();
        updateYellowBlackRect();
        updateCurrentColorRect();
    }

    private void updateCurrentColorRect() {
        GraphicsContext ctx = currentColorCanvas.getGraphicsContext2D();
        ctx.setFill(chosenColor);
        ctx.fillRect(0, 0, currentColorCanvas.getWidth(), currentColorCanvas.getHeight());
        GraphicsContext ctx2 = currentColorCanvas1.getGraphicsContext2D();
        ctx2.setFill(chosenColor);
        ctx2.fillRect(0, 0, currentColorCanvas1.getWidth(), currentColorCanvas1.getHeight());
        GraphicsContext ctx3 = currentColorCanvas2.getGraphicsContext2D();
        ctx3.setFill(chosenColor);
        ctx3.fillRect(0, 0, currentColorCanvas2.getWidth(), currentColorCanvas2.getHeight());
    }

    @FXML
    private void onClickOK() {
        buttonType = ButtonType.OK;
        window.close();
    }

    @FXML
    private void onClickCancel() {
        buttonType = ButtonType.CANCEL;
        window.close();
    }
}
