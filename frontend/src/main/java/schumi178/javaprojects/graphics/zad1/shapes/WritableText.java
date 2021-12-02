package schumi178.javaprojects.graphics.zad1.shapes;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import schumi178.javaprojects.graphics.zad1.util.SerializeUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class WritableText implements DrawableShape {

    private final Text text;
    private Color color;
    private double width;
    private double height;
    private ChangeListener<Bounds> updateListener;

    public WritableText(String text, Color color, double x, double y, double width, double height) {
        this.text = new Text(text);
        this.color = color;
        this.text.setX(x);
        this.text.setY(y);
        this.width = width;
        this.height = height;
        this.text.textProperty().addListener((observable, oldValue, newValue) -> {
            this.width = this.text.getLayoutBounds().getWidth();
            this.height = this.text.getLayoutBounds().getHeight();
        });
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(color);
        context.fillText(text.getText(), text.getX(), text.getY());
    }

    @Override
    public boolean containsPoint(double x, double y) {
        return getBounds().contains(x, y);
    }

    public void appendText(String text) {
        setText(this.text.getText() + text);
    }

    public void backspace() {
        String str = text.getText();
        setText(str.substring(0, Math.max(0, str.length() - 1)));
    }

    @Override
    public void translate(double x, double y) {
        text.setX(text.getX() + x);
        text.setY(text.getY() + y);
    }

    @Override
    public boolean edgeContainsPoint(double x, double y, Edge edge) {
        return false;
    }

    @Override
    public void resize(double x, double y, double newMouseX, double newMouseY, Edge edge) {

    }

    @Override
    public void rotate(double angle) {

    }

    @Override
    public void scaleByFactor(double factor, Point2D origin) {

    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void updateStartingPoint() {

    }

    public StringProperty textProperty() {
        return text.textProperty();
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    @Override
    public byte[] serialize() {
        byte[] startCode = new byte[1];
        startCode[0] = 5;
        byte[] text = this.text.getText().getBytes(StandardCharsets.UTF_8);
        byte[] textSize = SerializeUtils.intToByte(text.length);
        byte[] colorBytes = SerializeUtils.colorToByte(color);
        byte[] x = SerializeUtils.intToByte((int)this.text.getX());
        byte[] y = SerializeUtils.intToByte((int)this.text.getY());
        byte[] width = SerializeUtils.intToByte((int)this.width);
        byte[] height = SerializeUtils.intToByte((int)this.height);
        return SerializeUtils.joinBytes(List.of(startCode, textSize, text, colorBytes, x, y, width, height));
    }

    @Override
    public Bounds getBounds() {
        return text.getLayoutBounds();
    }
}
