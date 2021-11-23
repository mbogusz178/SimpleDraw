package schumi178.javaprojects.graphics.zad1.filter;

import schumi178.javaprojects.graphics.zad1.filter.util.Color;
import schumi178.javaprojects.graphics.zad1.filter.util.SimpleColor;

public class MultiplicativeFilter implements SingleParamFilter {
    @Override
    public Color[][] transform(Color[][] image, double value) {
        Color[][] result = new SimpleColor[image.length][image[0].length];
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                Color current = image[i][j];
                double newRed = current.getRed() * value;
                if(newRed > 255)
                    newRed = 255;
                double newGreen = current.getGreen() * value;
                if(newGreen > 255)
                    newGreen = 255;
                double newBlue = current.getBlue() * value;
                if(newBlue > 255)
                    newBlue = 255;
                Color newColor = new SimpleColor(newRed, newGreen, newBlue);
                result[i][j] = newColor;
            }
        }
        return result;
    }
}
