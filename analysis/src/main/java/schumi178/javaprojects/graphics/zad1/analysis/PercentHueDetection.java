package schumi178.javaprojects.graphics.zad1.analysis;

import schumi178.javaprojects.graphics.zad1.analysis.util.ColorHSV;
import schumi178.javaprojects.graphics.zad1.analysis.util.ColorRange;

public class PercentHueDetection {

    private final ColorRange range;

    public PercentHueDetection() {
        this.range = ColorRange.RED;
    }

    public PercentHueDetection(ColorRange range) {
        this.range = range;
    }

    public double getPercentage(ColorHSV[][] image) {
        final int imageSize = image.length * image[0].length;
        int count = 0;
        for (ColorHSV[] colorHSVS : image) {
            for (ColorHSV currentColor : colorHSVS) {
                if (range.isInRange(currentColor.getHue())) {
                    if (currentColor.getSaturation() > 50.0 / 255 && currentColor.getValue() > 3.0 / 255) {
                        count++;
                    }
                }
            }
        }
        return (double)count / imageSize * 100;
    }
}
