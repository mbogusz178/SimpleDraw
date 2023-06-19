package schumi178.javaprojects.graphics.zad1.analysis;

import schumi178.javaprojects.graphics.zad1.analysis.util.ColorHSV;
import schumi178.javaprojects.graphics.zad1.analysis.util.ColorRange;
import schumi178.javaprojects.graphics.zad1.analysis.util.Point;
import schumi178.javaprojects.graphics.zad1.analysis.util.SimpleColorHSV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PercentHueDetection {

    private final ColorRange range;

    public PercentHueDetection() {
        this.range = ColorRange.RED;
    }

    public PercentHueDetection(ColorRange range) {
        this.range = range;
    }

    private ColorHSV[][] getConditionMap(ColorHSV[][] image) {
        ColorHSV[][] result = new SimpleColorHSV[image.length][image[0].length];
        for(int i = 0; i < result.length; i++) {
            for(int j = 0; j < result[i].length; j++) {
                result[i][j] = new SimpleColorHSV(0, 0, 1);
            }
        }
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                ColorHSV currentColor = image[i][j];
                if (range.isInRange(currentColor.getHue())) {
                    if (currentColor.getSaturation() > 50.0 / 255 && currentColor.getValue() > 3.0 / 255) {
                        result[i][j] = new SimpleColorHSV(0, 0, 0);
                    }
                }
            }
        }
        return result;
    }

    public double getPercentage(ColorHSV[][] image) {
        ColorHSV[][] conditionMap = getConditionMap(image);
        TwoPass twoPass = new TwoPass(conditionMap);
        double size = image.length * image[0].length;
        return Collections.max(twoPass.getRegionSizes()) / size * 100;
    }
}
