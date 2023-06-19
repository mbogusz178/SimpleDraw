package schumi178.javaprojects.graphics.zad1.filter.binarization;

public class NiblackBinarizationFilter extends NiblackBasedBinarizationFilter {

    @Override
    protected int getThreshold(int mean, double sd) {
        return (int)(mean + sd * -0.2);
    }
}
