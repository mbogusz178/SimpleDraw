package schumi178.javaprojects.graphics.zad1.util;

public class Matrix3 {

    private final double[][] mat;

    public Matrix3() {
        mat = new double[3][3];
    }

    public double get(int x, int y) {
        return mat[x][y];
    }

    public void set(int x, int y, double val) {
        mat[x][y] = val;
    }

    public static Matrix3 rotation(double angle) {
        Matrix3 matrix = new Matrix3();
        angle = Math.toRadians(angle);
        matrix.set(0, 0, Math.cos(angle));
        matrix.set(1, 0, Math.sin(angle) * -1);
        matrix.set(0, 1, Math.sin(angle));
        matrix.set(1, 1, Math.cos(angle));
        matrix.set(2, 2, 1);
        return matrix;
    }
}
