package schumi178.javaprojects.graphics.zad1.filter;

public class VerticalSobelFilter extends MaskFilter {
    @Override
    public int[][] getMask() {
        return new int[][] {{1, 0, -1}, {2, 0, -2}, {1, 0, -1}};
    }

    @Override
    public int getMaskCenter() {
        return 1;
    }
}
