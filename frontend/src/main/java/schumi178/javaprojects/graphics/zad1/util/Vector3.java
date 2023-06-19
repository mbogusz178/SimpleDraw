package schumi178.javaprojects.graphics.zad1.util;

public class Vector3 {
    private double x;
    private double y;
    private double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3 uniform(double x, double y) {
        return new Vector3(x, y, 1);
    }

    public Vector3 multiplyByMatrix(Matrix3 matrix) {
        double x = matrix.get(0, 0) * this.x + matrix.get(1, 0) * this.y + matrix.get(2, 0) * this.z;
        double y = matrix.get(0, 1) * this.x + matrix.get(1, 1) * this.y + matrix.get(2, 1) * this.z;
        double z = matrix.get(0, 2) * this.x + matrix.get(1, 2) * this.y + matrix.get(2, 2) * this.z;
        return new Vector3(x, y, z);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
