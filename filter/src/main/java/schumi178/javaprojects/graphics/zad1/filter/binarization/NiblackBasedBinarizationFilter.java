package schumi178.javaprojects.graphics.zad1.filter.binarization;

import schumi178.javaprojects.graphics.zad1.filter.GreyscaleFilter;
import schumi178.javaprojects.graphics.zad1.filter.NoParamFilter;
import schumi178.javaprojects.graphics.zad1.filter.util.Color;
import schumi178.javaprojects.graphics.zad1.filter.util.SimpleColor;

import java.util.ArrayList;
import java.util.List;

public abstract class NiblackBasedBinarizationFilter implements NoParamFilter {
    private double standardDeviation(List<Integer> values) {
        int mean = mean(values);
        double sd = 0;
        for (Integer value : values) {
            sd += Math.pow(value - mean, 2);
        }
        if(values.size() != 0)
            sd /= values.size();
        return Math.sqrt(sd);
    }

    private int mean(List<Integer> values) {
        int mean = 0;
        for (Integer value : values) {
            mean += value;
        }
        if(values.size() != 0)
            mean /= values.size();
        return mean;
    }

    private int[][] getThresholdMatrix(Color[][] image) {
        int[][] matrix = new int[image.length][image[0].length];
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                matrix[i][j] = getThresholdAtPixel(image, i, j);
            }
        }
        return matrix;
    }

    @SuppressWarnings("ConstantConditions")
    private int getThresholdAtPixel(Color[][] image, int x, int y) {
        List<Integer> windowValues = new ArrayList<>();
        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                if((x + i) >= 0 && (x + i) < image.length && (y + j) >= 0 && (y + j) < image[0].length) {
                    windowValues.add((int)image[x+i][y+j].getRed());
                }
            }
        }
        int mean = mean(windowValues);
        double sd = standardDeviation(windowValues);
        return getThreshold(mean, sd);
    }

    @Override
    public Color[][] transform(Color[][] image) {
        Color[][] result = new Color[image.length][image[0].length];
        Color[][] grey = new GreyscaleFilter().transform(image);
        int[][] thresholdMatrix = getThresholdMatrix(grey);
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                int current = (int)(grey[i][j].getRed());
                int newGrey = current < thresholdMatrix[i][j] ? 0 : 255;
                result[i][j] = new SimpleColor(newGrey, newGrey, newGrey);
            }
        }
        return result;
    }

    protected abstract int getThreshold(int mean, double sd);
}
