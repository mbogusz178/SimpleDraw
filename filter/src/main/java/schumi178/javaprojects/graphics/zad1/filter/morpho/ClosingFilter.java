package schumi178.javaprojects.graphics.zad1.filter.morpho;

import schumi178.javaprojects.graphics.zad1.filter.GreyscaleFilter;
import schumi178.javaprojects.graphics.zad1.filter.NoParamFilter;
import schumi178.javaprojects.graphics.zad1.filter.binarization.OtsuBinarizationFilter;
import schumi178.javaprojects.graphics.zad1.filter.util.Color;

public class ClosingFilter implements NoParamFilter {
    @Override
    public Color[][] transform(Color[][] image) {
        Color[][] grey = new GreyscaleFilter().transform(image);
        Color[][] binary = new OtsuBinarizationFilter().transform(grey);
        Color[][] dilated = new DilationFilter().transform(binary);
        return new ErosionFilter().transform(dilated);
    }
}
