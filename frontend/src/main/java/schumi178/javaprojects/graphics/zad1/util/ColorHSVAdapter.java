package schumi178.javaprojects.graphics.zad1.util;

import javafx.scene.paint.Color;
import schumi178.javaprojects.graphics.zad1.analysis.util.ColorHSV;

public class ColorHSVAdapter implements ColorHSV {

    private final Color color;

    public ColorHSVAdapter(Color color) {
        this.color = color;
    }

    @Override
    public double getHue() {
        return color.getHue();
    }

    @Override
    public double getSaturation() {
        return color.getSaturation();
    }

    @Override
    public double getValue() {
        return color.getBrightness();
    }
}
