package schumi178.javaprojects.graphics.zad1.filter;

public class HorizontalSobelFilter extends MaskFilter {
    @Override
    public int[][] getMask() {
        return new int[][] {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};
    }

    @Override
    public int getMaskCenter() {
        return 1;
    }
}
