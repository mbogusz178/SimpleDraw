package schumi178.javaprojects.graphics.zad1.shapes;

import javafx.geometry.Bounds;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Point2D;
import javafx.geometry.VerticalDirection;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public interface DrawableShape {

    int EDGE_SENSITIVITY = 8;
    List<Edge> ORDERED_EDGES = List.of(Edge.TOP_LEFT, Edge.TOP_RIGHT, Edge.BOTTOM_LEFT, Edge.BOTTOM_RIGHT, Edge.TOP, Edge.LEFT, Edge.RIGHT, Edge.BOTTOM);

    enum Edge {
        TOP(Cursor.N_RESIZE, null, VerticalDirection.UP),
        LEFT(Cursor.W_RESIZE, HorizontalDirection.LEFT, null),
        RIGHT(Cursor.E_RESIZE, HorizontalDirection.RIGHT, null),
        BOTTOM(Cursor.S_RESIZE, null, VerticalDirection.DOWN),
        TOP_LEFT(Cursor.NW_RESIZE, HorizontalDirection.LEFT, VerticalDirection.UP),
        TOP_RIGHT(Cursor.NE_RESIZE, HorizontalDirection.RIGHT, VerticalDirection.UP),
        BOTTOM_LEFT(Cursor.SW_RESIZE, HorizontalDirection.LEFT, VerticalDirection.DOWN),
        BOTTOM_RIGHT(Cursor.SE_RESIZE, HorizontalDirection.RIGHT, VerticalDirection.DOWN);

        private final Cursor cursor;
        private final HorizontalDirection horizontal;
        private final VerticalDirection vertical;

        Edge(Cursor cursor, HorizontalDirection horizontal, VerticalDirection vertical) {
            this.cursor = cursor;
            this.horizontal = horizontal;
            this.vertical = vertical;
        }

        public Cursor getCursor() {
            return cursor;
        }

        public HorizontalDirection getHorizontal() {
            return horizontal;
        }

        public VerticalDirection getVertical() {
            return vertical;
        }
    }

    void draw(GraphicsContext context);
    boolean containsPoint(double x, double y);
    void translate(double x, double y);
    void resize(double x, double y, double newMouseX, double newMouseY, Edge edge);
    void scaleByFactor(double factor, Point2D origin);
    void rotate(double angle);
    void setColor(Color color);
    void updateStartingPoint();
    byte[] serialize();
    default boolean edgeContainsPoint(double x, double y, Edge edge) {
        Bounds bounds = getBounds();
        switch (edge) {
            case TOP:
                return Math.abs(y - bounds.getMinY()) <= EDGE_SENSITIVITY && x >= bounds.getMinX() && x <= bounds.getMaxX();
            case BOTTOM:
                return Math.abs(y - bounds.getMaxY()) <= EDGE_SENSITIVITY && x >= bounds.getMinX() && x <= bounds.getMaxX();
            case LEFT:
                return Math.abs(x - bounds.getMinX()) <= EDGE_SENSITIVITY && y >= bounds.getMinY() && y <= bounds.getMaxY();
            case RIGHT:
                return Math.abs(x - bounds.getMaxX()) <= EDGE_SENSITIVITY && y >= bounds.getMinY() && y <= bounds.getMaxY();
            case TOP_LEFT:
                return Math.abs(x - bounds.getMinX()) <= EDGE_SENSITIVITY && Math.abs(y - bounds.getMinY()) <= EDGE_SENSITIVITY;
            case TOP_RIGHT:
                return Math.abs(x - bounds.getMaxX()) <= EDGE_SENSITIVITY && Math.abs(y - bounds.getMinY()) <= EDGE_SENSITIVITY;
            case BOTTOM_LEFT:
                return Math.abs(x - bounds.getMinX()) <= EDGE_SENSITIVITY && Math.abs(y - bounds.getMaxY()) <= EDGE_SENSITIVITY;
            case BOTTOM_RIGHT:
                return Math.abs(x - bounds.getMaxX()) <= EDGE_SENSITIVITY && Math.abs(y - bounds.getMaxY()) <= EDGE_SENSITIVITY;
        }
        return false;
    }
    Bounds getBounds();
}