package schumi178.javaprojects.graphics.zad1.filter;

import schumi178.javaprojects.graphics.zad1.filter.util.Color;
import schumi178.javaprojects.graphics.zad1.filter.util.SimpleColor;

public class GreyscaleFilter implements NoParamFilter {
    @Override
    public Color[][] transform(Color[][] image) {
        Color[][] result = new SimpleColor[image.length][image[0].length];
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                Color current = image[i][j];
                double newGray = (current.getRed() + current.getGreen() + current.getBlue()) / 3.0;
                result[i][j] = new SimpleColor(newGray, newGray, newGray);
            }
        }
        return result;
    }
}
