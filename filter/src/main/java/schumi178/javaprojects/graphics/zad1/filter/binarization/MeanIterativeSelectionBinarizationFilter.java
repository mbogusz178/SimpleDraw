package schumi178.javaprojects.graphics.zad1.filter.binarization;

import schumi178.javaprojects.graphics.zad1.filter.GreyscaleFilter;
import schumi178.javaprojects.graphics.zad1.filter.NoParamFilter;
import schumi178.javaprojects.graphics.zad1.filter.util.Color;
import schumi178.javaprojects.graphics.zad1.filter.util.HistogramUtils;
import schumi178.javaprojects.graphics.zad1.filter.util.SimpleColor;

import java.util.HashMap;
import java.util.Map;

public class MeanIterativeSelectionBinarizationFilter implements NoParamFilter {

    private final Map<Integer, Integer> tCache = new HashMap<>();

    private int T(Color[][] image, int value) {
        if(tCache.containsKey(value)) {
            return tCache.get(value);
        }
        if(value == 0) {
            return 0;
        }
        Map<Integer, Integer> histogram = HistogramUtils.getHistogram(image);

        int upperLeft = 0;
        int tlower = T(image, value - 1);
        for(int i = 0; i <= tlower; i++) {
            upperLeft += i * histogram.get(i);
        }

        int lowerLeft = 0;
        for(int i = 0; i <= tlower; i++) {
            lowerLeft += histogram.get(i);
        }
        lowerLeft *= 2;

        int upperRight = 0;
        for(int i = tlower + 1; i < 256; i++) {
            upperRight += i * histogram.get(i);
        }

        int lowerRight = 0;
        for(int i = tlower + 1; i < 256; i++) {
            lowerRight += histogram.get(i);
        }
        lowerRight *= 2;
        if(lowerLeft == 0) {
            lowerLeft = 1;
        }
        if(lowerRight == 0) {
            lowerRight = 1;
        }

        int result = (upperLeft / lowerLeft) + (upperRight / lowerRight);
        tCache.put(value, result);
        return result;
    }

    @Override
    public Color[][] transform(Color[][] image) {
        Color[][] result = new Color[image.length][image[0].length];
        Color[][] grey = new GreyscaleFilter().transform(image);
        int threshold = 0;
        for(int i = 1; i < 256; i++) {
            if(T(image, i) == T(image, i - 1)) {
                threshold = T(image, i);
            }
        }
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
