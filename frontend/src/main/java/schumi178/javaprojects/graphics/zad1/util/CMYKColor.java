package schumi178.javaprojects.graphics.zad1.util;

import javafx.scene.paint.Color;

public class CMYKColor {

    private final double cyan;
    private final double magenta;
    private final double yellow;
    private final double black;

    public CMYKColor(double cyan, double magenta, double yellow, double black) {
        this.cyan = cyan;
        this.magenta = magenta;
        this.yellow = yellow;
        this.black = black;
    }

    public CMYKColor(Color rgb) {
        double red = rgb.getRed() * 255;
        double green = rgb.getGreen() * 255;
        double blue = rgb.getBlue() * 255;
        black = Math.min(Math.min(255 - red, 255 - green), 255 - blue);

        if(black != 255) {
            cyan = (255 - red - black) / (255 - black) * 255;
            magenta = (255 - green - black) / (255 - black) * 255;
            yellow = (255 - blue - black) / (255 - black) * 255;
        } else {
            cyan = 255 - red;
            magenta = 255 - green;
            yellow = 255 - blue;
        }
    }

    public Color getRGB() {
        double red;
        double green;
        double blue;
        if(black != 255) {
            red = ((255 - cyan) * (255 - black)) / 255;
            green = ((255 - magenta) * (255 - black)) / 255;
            blue = ((255 - yellow) * (255 - black)) / 255;
        } else {
            red = 0;
            green = 0;
            blue = 0;
        }
        return Color.rgb((int)red, (int)green, (int)blue);
    }

    public double getCyan() {
        return cyan;
    }

    public double getMagenta() {
        return magenta;
    }

    public double getYellow() {
        return yellow;
    }

    public double getBlack() {
        return black;
    }
}
