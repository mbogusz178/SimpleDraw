package schumi178.javaprojects.graphics.zad1.filter.binarization;

import schumi178.javaprojects.graphics.zad1.filter.GreyscaleFilter;
import schumi178.javaprojects.graphics.zad1.filter.SingleParamFilter;
import schumi178.javaprojects.graphics.zad1.filter.util.Color;
import schumi178.javaprojects.graphics.zad1.filter.util.SimpleColor;

public class ManualBinarizationFilter implements SingleParamFilter {
    @Override
    public Color[][] transform(Color[][] image, double value) {
        Color[][] result = new Color[image.length][image[0].length];
        Color[][] grey = new GreyscaleFilter().transform(image);
        int threshold = (int)(value / 100.0 * 255.0);
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                int current = (int)(grey[i][j].getRed());
                int newGrey = current < threshold ? 0 : 255;
                result[i][j] = new SimpleColor(newGrey, newGrey, newGrey);
            }
        }
        return result;
    }
}
