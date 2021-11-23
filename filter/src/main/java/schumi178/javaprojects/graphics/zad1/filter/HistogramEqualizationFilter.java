package schumi178.javaprojects.graphics.zad1.filter;

import schumi178.javaprojects.graphics.zad1.filter.util.Color;
import schumi178.javaprojects.graphics.zad1.filter.util.HistogramUtils;
import schumi178.javaprojects.graphics.zad1.filter.util.SimpleColor;

import java.util.Map;

public class HistogramEqualizationFilter implements NoParamFilter {
    @Override
    public Color[][] transform(Color[][] image) {
        Color[][] result = new Color[image.length][image[0].length];
        Color[][] greyed = new GreyscaleFilter().transform(image);
        Map<Integer, Integer> histogram = HistogramUtils.getHistogram(greyed);
        Map<Integer, Integer> cumulative = HistogramUtils.getCumulativeHistogram(histogram);
        final int area = image.length * image[0].length;
        float[] array = new float[256];
        for(int i = 0; i < 256; i++) {
            array[i] = (float)((cumulative.get(i) * 255.0)/(float)area);
        }
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[0].length; j++) {
                int nVal = (int)array[(int)(greyed[i][j].getRed())];
                result[i][j] = new SimpleColor(nVal, nVal, nVal);
            }
        }
        return result;
    }
}
