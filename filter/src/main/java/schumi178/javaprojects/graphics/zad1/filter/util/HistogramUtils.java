package schumi178.javaprojects.graphics.zad1.filter.util;

import java.util.HashMap;
import java.util.Map;

public class HistogramUtils {
    public static Map<Integer, Integer> getHistogram(Color[][] image) {
        Map<Integer, Integer> histogram = new HashMap<>();
        for(int i = 0; i < 256; i++) {
            histogram.put(i, 0);
        }
        for (Color[] colors : image) {
            for (Color color : colors) {
                int grey = (int) (color.getRed());
                int currentCount = histogram.get(grey);
                histogram.put(grey, currentCount + 1);
            }
        }
        return histogram;
    }

    public static Map<Integer, Integer> getCumulativeHistogram(Map<Integer, Integer> histogram) {
        Map<Integer, Integer> cumulative = new HashMap<>();
        cumulative.put(0, histogram.get(0));
        for(int i = 1; i < 256; i++) {
            cumulative.put(i, cumulative.get(i - 1) + histogram.get(i));
        }
        return cumulative;
    }
}
