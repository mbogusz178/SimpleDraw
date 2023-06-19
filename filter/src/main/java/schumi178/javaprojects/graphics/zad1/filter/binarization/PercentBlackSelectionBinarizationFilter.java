package schumi178.javaprojects.graphics.zad1.filter.binarization;

import schumi178.javaprojects.graphics.zad1.filter.GreyscaleFilter;
import schumi178.javaprojects.graphics.zad1.filter.SingleParamFilter;
import schumi178.javaprojects.graphics.zad1.filter.util.Color;
import schumi178.javaprojects.graphics.zad1.filter.util.HistogramUtils;
import schumi178.javaprojects.graphics.zad1.filter.util.SimpleColor;

import java.util.Map;

public class PercentBlackSelectionBinarizationFilter implements SingleParamFilter {
    @Override
    public Color[][] transform(Color[][] image, double value) {
        Color[][] result = new Color[image.length][image[0].length];
        Color[][] grey = new GreyscaleFilter().transform(image);
        Map<Integer, Integer> histogram = HistogramUtils.getHistogram(grey);
        Map<Integer, Integer> cumulative = HistogramUtils.getCumulativeHistogram(histogram);
        int threshold = 0;
        for(int i = 0; i < 256; i++) {
            if(cumulative.get(i) >= image.length * image[0].length * value / 100) {
                threshold = i;
                break;
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
