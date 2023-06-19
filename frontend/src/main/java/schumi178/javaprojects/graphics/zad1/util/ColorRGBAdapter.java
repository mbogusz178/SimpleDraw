package schumi178.javaprojects.graphics.zad1.util;

import schumi178.javaprojects.graphics.zad1.filter.util.Color;

public class ColorRGBAdapter implements Color {

    private final javafx.scene.paint.Color color;

    public ColorRGBAdapter(javafx.scene.paint.Color color) {
        this.color = color;
    }

    @Override
    public double getRed() {
        return color.getRed() * 255;
    }

    @Override
    public double getGreen() {
        return color.getGreen() * 255;
    }

    @Override
    public double getBlue() {
        return color.getBlue() * 255;
    }
}
