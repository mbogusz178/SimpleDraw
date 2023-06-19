package schumi178.javaprojects.graphics.zad1.filter;

public class CustomFilter extends MaskFilter {
    @Override
    public int[][] getMask() {
        return new int[][] {{1, 5, 1}, {5, 1, 5}, {1, 5, 1}};
    }

    @Override
    public int getMaskCenter() {
        return 1;
    }
}
