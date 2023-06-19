package schumi178.javaprojects.graphics.zad1.filter;

public class AverageFilter extends MaskFilter {

    @Override
    public int[][] getMask() {
        return new int[][] {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    }

    @Override
    public int getMaskCenter() {
        return 1;
    }
}
