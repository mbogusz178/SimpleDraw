package schumi178.javaprojects.graphics.zad1.filter.binarization;

import schumi178.javaprojects.graphics.zad1.filter.GreyscaleFilter;
import schumi178.javaprojects.graphics.zad1.filter.NoParamFilter;
import schumi178.javaprojects.graphics.zad1.filter.util.Color;
import schumi178.javaprojects.graphics.zad1.filter.util.HistogramUtils;
import schumi178.javaprojects.graphics.zad1.filter.util.SimpleColor;

import java.util.Map;

public class OtsuBinarizationFilter implements NoParamFilter {

    @Override
    public Color[][] transform(Color[][] image) {
        Color[][] result = new Color[image.length][image[0].length];
        Color[][] grey = new GreyscaleFilter().transform(image);
        Map<Integer, Integer> histogram = HistogramUtils.getHistogram(grey);
        int threshold;
        int optimalThreshold = 0;
        int sum = 0;
        int total = 0;

        for (threshold = 0; threshold < histogram.size(); threshold++) {
            sum += ((double) threshold * histogram.get(threshold));
            total += histogram.get(threshold);
        }

        int sumK = 0;
        double zeroIntensity = histogram.get(0);
        double bcv;
        double bcvMax = 0;
        double numerator, denominator;

        for (threshold = 1; threshold < histogram.size() - 1; threshold++) {
            sumK += (double) threshold * histogram.get(threshold);
            zeroIntensity += histogram.get(threshold);

            denominator = zeroIntensity * (total - zeroIntensity);

            if (denominator != 0) {
                numerator = (zeroIntensity / total) * sum - sumK;
                bcv = Math.pow(numerator, 2) / denominator;
            } else {
                bcv = 0;
            }

            if (bcv >= bcvMax) {
                bcvMax = bcv;
                optimalThreshold = threshold;
            }
        }

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                int current = (int)(grey[i][j].getRed());
                int newGrey = current < optimalThreshold ? 0 : 255;
                result[i][j] = new SimpleColor(newGrey, newGrey, newGrey);
            }
        }
        return result;
    }
}
