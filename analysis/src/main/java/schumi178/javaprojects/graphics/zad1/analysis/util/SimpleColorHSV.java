package schumi178.javaprojects.graphics.zad1.analysis.util;

public class SimpleColorHSV implements ColorHSV {
    private final double hue;
    private final double saturation;
    private final double value;

    public SimpleColorHSV(double hue, double saturation, double value) {
        this.hue = hue;
        this.saturation = saturation;
        this.value = value;
    }

    @Override
    public double getHue() {
        return hue;
    }

    @Override
    public double getSaturation() {
        return saturation;
    }

    @Override
    public double getValue() {
        return value;
    }
}
