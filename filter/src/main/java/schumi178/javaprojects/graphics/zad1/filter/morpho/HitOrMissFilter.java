package schumi178.javaprojects.graphics.zad1.filter.morpho;

import schumi178.javaprojects.graphics.zad1.filter.GreyscaleFilter;
import schumi178.javaprojects.graphics.zad1.filter.NoParamFilter;
import schumi178.javaprojects.graphics.zad1.filter.binarization.OtsuBinarizationFilter;
import schumi178.javaprojects.graphics.zad1.filter.util.Color;
import schumi178.javaprojects.graphics.zad1.filter.util.SimpleColor;

public class HitOrMissFilter implements NoParamFilter {
    @Override
    public Color[][] transform(Color[][] image) {
        Color[][] result = new Color[image.length][image[0].length];
        Color[][] grey = new GreyscaleFilter().transform(image);
        Color[][] binary = new OtsuBinarizationFilter().transform(grey);
        for(int i = 0; i < result.length; i++) {
            for(int j = 0; j < result[i].length; j++) {
                result[i][j] = new SimpleColor(binary[i][j].getRed(), binary[i][j].getGreen(), binary[i][j].getBlue());
            }
        }
        int[][] kernel = new int[][] {{0, 1, 0},
                {1, 1, 1},
                {0, 1, 0}};

        int verticalWindow = image.length - 3;
        int horizontalWindow = image[0].length - 3;

        for(int i = 0; i < kernel.length; i++) {
            for(int j = 0; j < kernel[i].length; j++) {
                if(kernel[i][j] > 0)
                    kernel[i][j] = 255;
            }
        }

        for(int verticalPos = 1; verticalPos <= verticalWindow; verticalPos++) {
            for(int horizontalPos = 1; horizontalPos <= horizontalWindow; horizontalPos++) {
                boolean skipFlag = false;
                for(int i = 0; i < 3; i++) {
                    for(int j = 0; j < 3; j++) {
                        if(binary[verticalPos+i-1][horizontalPos+i-1].getRed() != kernel[i][j]) {
                            skipFlag = true;
                            break;
                        }
                    }
                    if(skipFlag) {
                        result[verticalPos][horizontalPos] = new SimpleColor(255, 255, 255);
                        break;
                    }
                }
                if(!skipFlag) {
                    result[verticalPos][horizontalPos] = new SimpleColor(0, 0, 0);
                }
            }
        }
        return result;
    }
}
