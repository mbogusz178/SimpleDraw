package schumi178.javaprojects.graphics.zad1.filter.binarization;

public class PhansalkarBinarizationFilter extends NiblackBasedBinarizationFilter {

    @Override
    protected int getThreshold(int mean, double sd) {
        return (int)(mean * (1 + 2 * Math.exp(-10 * mean) + 0.025 * (sd / 0.5 - 1)));
    }
}
