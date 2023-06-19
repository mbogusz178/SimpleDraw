package schumi178.javaprojects.graphics.zad1.filter;

import schumi178.javaprojects.graphics.zad1.filter.util.Color;
import schumi178.javaprojects.graphics.zad1.filter.util.SimpleColor;

public class DarkenFilter implements SingleParamFilter {
    @Override
    public Color[][] transform(Color[][] image, double value) {
        Color[][] result = new SimpleColor[image.length][image[0].length];
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                Color current = image[i][j];
                double newRed = Math.max(0, current.getRed() * (1.0 - value * 0.01));
                double newGreen = Math.max(0, current.getGreen() * (1.0 - value * 0.01));
                double newBlue = Math.max(0, current.getBlue() * (1.0 - value * 0.01));
                Color newColor = new SimpleColor(newRed, newGreen, newBlue);
                result[i][j] = newColor;
            }
        }
        return result;
    }
}
