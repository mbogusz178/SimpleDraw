package schumi178.javaprojects.graphics.zad1.filter;

import schumi178.javaprojects.graphics.zad1.filter.util.Color;
import schumi178.javaprojects.graphics.zad1.filter.util.HistogramUtils;
import schumi178.javaprojects.graphics.zad1.filter.util.SimpleColor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HistogramExtensionFilter implements NoParamFilter {
    @Override
    public Color[][] transform(Color[][] image) {
        Color[][] result = new Color[image.length][image[0].length];
        Color[][] grey = new GreyscaleFilter().transform(image);
        Map<Integer, Integer> histogram = HistogramUtils.getHistogram(grey);
        Map<Integer, Integer> nonZeroHistogram = new HashMap<>();
        for(Map.Entry<Integer, Integer> entry: histogram.entrySet()) {
            if(entry.getValue() != 0) {
                nonZeroHistogram.put(entry.getKey(), entry.getValue());
            }
        }
        int kmax = Collections.max(nonZeroHistogram.keySet());
        int kmin = Collections.min(nonZeroHistogram.keySet());
        int diff = kmax - kmin;
        final int zk = 255;
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                int currentGrey = (int)((grey[i][j].getRed() - kmin) / diff * zk);
                result[i][j] = new SimpleColor(currentGrey, currentGrey, currentGrey);
            }
        }
        return result;
    }
}
