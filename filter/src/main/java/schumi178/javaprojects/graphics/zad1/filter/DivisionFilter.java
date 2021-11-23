package schumi178.javaprojects.graphics.zad1.filter;

import schumi178.javaprojects.graphics.zad1.filter.util.Color;
import schumi178.javaprojects.graphics.zad1.filter.util.SimpleColor;

public class DivisionFilter implements SingleParamFilter {
    @Override
    public Color[][] transform(Color[][] image, double value) {
        if(value == 0) {
            return image;
        }
        Color[][] result = new SimpleColor[image.length][image[0].length];
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                Color current = image[i][j];
                double newRed = current.getRed() / value;
                if(newRed < 0)
                    newRed = 0;
                double newGreen = current.getGreen() / value;
                if(newGreen < 0)
                    newGreen = 0;
                double newBlue = current.getBlue() / value;
                if(newBlue < 0)
                    newBlue = 0;
                Color newColor = new SimpleColor(newRed, newGreen, newBlue);
                result[i][j] = newColor;
            }
        }
        return result;
    }
}
