package schumi178.javaprojects.graphics.zad1.filter.util;

public class SimpleColor implements Color {
    private final double red;
    private final double green;
    private final double blue;

    public SimpleColor(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public double getRed() {
        return red;
    }

    @Override
    public double getGreen() {
        return green;
    }

    @Override
    public double getBlue() {
        return blue;
    }
}
