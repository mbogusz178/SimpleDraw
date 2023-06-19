package schumi178.javaprojects.graphics.zad1.filter;

import schumi178.javaprojects.graphics.zad1.filter.util.Color;
import schumi178.javaprojects.graphics.zad1.filter.util.SimpleColor;

public abstract class MaskFilter implements NoParamFilter {

    public abstract int[][] getMask();
    public abstract int getMaskCenter();

    private double averagePixelRed(int i, int j, Color[][] image) {
        Color current = image[i][j];
        int[][] mask = getMask();
        double newRed = 0;
        double weightCount = 0;
        for(int k = 0; k < mask.length; k++) {
            for(int l = 0; l < mask[k].length; l++) {
                int xDist = k - getMaskCenter();
                int yDist = l - getMaskCenter();
                if((i + xDist) >= 0 && (i + xDist) < image.length && (j + yDist) >= 0 && (j + yDist) < image[0].length) {
                    newRed += image[i + xDist][j + yDist].getRed() * mask[k][l];
                    weightCount += mask[k][l];
                }
            }
        }

        if(weightCount != 0)
            newRed /= weightCount;
        if(newRed < 0)
            newRed = 0;
        if(newRed > 255)
            newRed = 255;
        return newRed;
    }

    private double averagePixelGreen(int i, int j, Color[][] image) {
        Color current = image[i][j];
        int[][] mask = getMask();
        double newGreen = 0;
        double weightCount = 0;
        for(int k = 0; k < mask.length; k++) {
            for(int l = 0; l < mask[k].length; l++) {
                int xDist = k - getMaskCenter();
                int yDist = l - getMaskCenter();
                if((i + xDist) >= 0 && (i + xDist) < image.length && (j + yDist) >= 0 && (j + yDist) < image[0].length) {
                    newGreen += image[i + xDist][j + yDist].getGreen() * mask[k][l];
                    weightCount += mask[k][l];
                }
            }
        }

        if(weightCount != 0)
            newGreen /= weightCount;
        if(newGreen < 0)
            newGreen = 0;
        if(newGreen > 255)
            newGreen = 255;
        return newGreen;
    }

    private double averagePixelBlue(int i, int j, Color[][] image) {
        Color current = image[i][j];
        int[][] mask = getMask();
        double newBlue = 0;
        double weightCount = 0;
        for(int k = 0; k < mask.length; k++) {
            for(int l = 0; l < mask[k].length; l++) {
                int xDist = k - getMaskCenter();
                int yDist = l - getMaskCenter();
                if((i + xDist) >= 0 && (i + xDist) < image.length && (j + yDist) >= 0 && (j + yDist) < image[0].length) {
                    newBlue += image[i + xDist][j + yDist].getBlue() * mask[k][l];
                    weightCount += mask[k][l];
                }
            }
        }

        if(weightCount != 0)
            newBlue /= weightCount;
        if(newBlue < 0)
            newBlue = 0;
        if(newBlue > 255)
            newBlue = 255;
        return newBlue;
    }

    @Override
    public Color[][] transform(Color[][] image) {
        Color[][] result = new SimpleColor[image.length][image[0].length];
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                double newRed = averagePixelRed(i, j, image);
                double newGreen = averagePixelGreen(i, j, image);
                double newBlue = averagePixelBlue(i, j, image);
                result[i][j] = new SimpleColor(newRed, newGreen, newBlue);
            }
        }
        return result;
    }
}
