package schumi178.javaprojects.graphics.zad1.analysis.util;

import java.util.function.Predicate;

public enum ColorRange {
    RED(hue -> hue > 345 || hue < 30),
    YELLOW(hue -> hue >= 45 && hue <= 60),
    GREEN(hue -> hue >= 75 && hue <= 140),
    CYAN(hue -> hue >= 150 && hue <= 195),
    BLUE(hue -> hue >= 210 && hue <= 255),
    MAGENTA(hue -> hue >= 285 && hue <= 330);

    private final Predicate<Double> condition;

    ColorRange(Predicate<Double> condition) {
        this.condition = condition;
    }

    public boolean isInRange(double hue) {
        return condition.test(hue);
    }
}
