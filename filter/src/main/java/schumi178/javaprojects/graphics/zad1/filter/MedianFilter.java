package schumi178.javaprojects.graphics.zad1.filter;

import schumi178.javaprojects.graphics.zad1.filter.util.Color;
import schumi178.javaprojects.graphics.zad1.filter.util.SimpleColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MedianFilter implements NoParamFilter {

    private double medianRed(int i, int j, Color[][] image) {
        List<Double> pixels = new ArrayList<>();
        for(int k = 0; k < 3; k++) {
            for(int l = 0; l < 3; l++) {
                int x = i+k-1;
                int y = j+l-1;
                if(x >= 0 && x < image.length && y >= 0 && y < image[0].length)
                    pixels.add(image[x][y].getRed());
            }
        }
        Collections.sort(pixels);
        return pixels.get(pixels.size() / 2);
    }

    private double medianGreen(int i, int j, Color[][] image) {
        List<Double> pixels = new ArrayList<>();
        for(int k = 0; k < 3; k++) {
            for(int l = 0; l < 3; l++) {
                int x = i+k-1;
                int y = j+l-1;
                if(x >= 0 && x < image.length && y >= 0 && y < image[0].length)
                    pixels.add(image[x][y].getGreen());
            }
        }
        Collections.sort(pixels);
        return pixels.get(pixels.size() / 2);
    }

    private double medianBlue(int i, int j, Color[][] image) {
        List<Double> pixels = new ArrayList<>();
        for(int k = 0; k < 3; k++) {
            for(int l = 0; l < 3; l++) {
                int x = i+k-1;
                int y = j+l-1;
                if(x >= 0 && x < image.length && y >= 0 && y < image[0].length)
                    pixels.add(image[x][y].getBlue());
            }
        }
        Collections.sort(pixels);
        return pixels.get(pixels.size() / 2);
    }

    @Override
    public Color[][] transform(Color[][] image) {
        Color[][] result = new SimpleColor[image.length][image[0].length];
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                double newRed = medianRed(i, j, image);
                double newGreen = medianGreen(i, j, image);
                double newBlue = medianBlue(i, j, image);
                result[i][j] = new SimpleColor(newRed, newGreen, newBlue);
            }
        }
        return result;
    }
}
