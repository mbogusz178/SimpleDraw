package schumi178.javaprojects.graphics.zad1.filter.binarization;

public class SauvolaBinarizationFilter extends NiblackBasedBinarizationFilter {

    @Override
    protected int getThreshold(int mean, double sd) {
        double secondOperand = 1 + 0.2 * ((sd / 125) - 1);
        return (int)(mean * secondOperand);
    }
}
