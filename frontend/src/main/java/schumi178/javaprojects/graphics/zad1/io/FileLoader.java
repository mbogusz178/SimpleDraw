package schumi178.javaprojects.graphics.zad1.io;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import schumi178.javaprojects.graphics.zad1.exception.BrokenFileException;
import schumi178.javaprojects.graphics.zad1.shapes.*;
import schumi178.javaprojects.graphics.zad1.util.SerializeUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileLoader {

    private final InputStream inputStream;

    public FileLoader(File file) throws FileNotFoundException {
        this.inputStream = new FileInputStream(file);
    }

    public void close() throws IOException {
        inputStream.close();
    }

    private void proceedWithLine(ObservableList<DrawableShape> newList) throws IOException, BrokenFileException {
        double lineStartX = SerializeUtils.getIntFromStream(inputStream);
        double lineStartY = SerializeUtils.getIntFromStream(inputStream);
        double lineEndX = SerializeUtils.getIntFromStream(inputStream);
        double lineEndY = SerializeUtils.getIntFromStream(inputStream);
        Color color = SerializeUtils.getColorFromStream(inputStream);
        DrawableLine line = new DrawableLine(new Line(lineStartX, lineStartY, lineEndX, lineEndY));
        line.setColor(color);
        newList.add(line);
    }

    private void proceedWithOval(ObservableList<DrawableShape> newList) throws BrokenFileException, IOException {
        double ovalCenterX = SerializeUtils.getIntFromStream(inputStream);
        double ovalCenterY = SerializeUtils.getIntFromStream(inputStream);
        double ovalRadiusX = SerializeUtils.getIntFromStream(inputStream);
        double ovalRadiusY = SerializeUtils.getIntFromStream(inputStream);
        double theoreticalRadiusX = SerializeUtils.getIntFromStream(inputStream);
        double theoreticalRadiusY = SerializeUtils.getIntFromStream(inputStream);
        Color color = SerializeUtils.getColorFromStream(inputStream);
        DrawableOval oval = new DrawableOval(new Ellipse(ovalCenterX, ovalCenterY, ovalRadiusX, ovalRadiusY));
        oval.setTheoreticalRadiusX(theoreticalRadiusX);
        oval.setTheoreticalRadiusY(theoreticalRadiusY);
        oval.setColor(color);
        newList.add(oval);
    }

    private void proceedWithRect(ObservableList<DrawableShape> newList) throws BrokenFileException, IOException {
        double rectX = SerializeUtils.getIntFromStream(inputStream);
        double rectY = SerializeUtils.getIntFromStream(inputStream);
        double rectWidth = SerializeUtils.getIntFromStream(inputStream);
        double rectHeight = SerializeUtils.getIntFromStream(inputStream);
        double theoreticalWidth = SerializeUtils.getIntFromStream(inputStream);
        double theoreticalHeight = SerializeUtils.getIntFromStream(inputStream);
        double startingPointX = SerializeUtils.getIntFromStream(inputStream);
        double startingPointY = SerializeUtils.getIntFromStream(inputStream);
        Color color = SerializeUtils.getColorFromStream(inputStream);
        DrawableRectangle rect = new DrawableRectangle(new Rectangle(rectX, rectY, rectWidth, rectHeight));
        rect.updateStartingPoint();
        rect.setTheoreticalWidth(theoreticalWidth);
        rect.setTheoreticalHeight(theoreticalHeight);
        rect.setColor(color);
        newList.add(rect);
    }

    private void proceedWithTriangle(ObservableList<DrawableShape> newList) throws BrokenFileException, IOException {
        double triangleX = SerializeUtils.getIntFromStream(inputStream);
        double triangleY = SerializeUtils.getIntFromStream(inputStream);
        double triangleWidth = SerializeUtils.getIntFromStream(inputStream);
        double triangleHeight = SerializeUtils.getIntFromStream(inputStream);
        double theoreticalWidth = SerializeUtils.getIntFromStream(inputStream);
        double theoreticalHeight = SerializeUtils.getIntFromStream(inputStream);
        double startingPointX = SerializeUtils.getIntFromStream(inputStream);
        double startingPointY = SerializeUtils.getIntFromStream(inputStream);
        Color color = SerializeUtils.getColorFromStream(inputStream);
        DrawableTriangle triangle = new DrawableTriangle(triangleX, triangleY, triangleWidth, triangleHeight);
        triangle.setStartingPointX(startingPointX);
        triangle.setStartingPointY(startingPointY);
        triangle.setTheoreticalWidth(theoreticalWidth);
        triangle.setTheoreticalHeight(theoreticalHeight);
        triangle.setColor(color);
        newList.add(triangle);
    }

    private void proceedWithFree(ObservableList<DrawableShape> newList) throws BrokenFileException, IOException {
        Color color = SerializeUtils.getColorFromStream(inputStream);
        int lineSize = SerializeUtils.getIntFromStream(inputStream);
        double startX = SerializeUtils.getIntFromStream(inputStream);
        double startY = SerializeUtils.getIntFromStream(inputStream);
        FreeDrawing drawing = new FreeDrawing(startX, startY, color);
        for(int i = 0; i < lineSize; i++) {
            double newX = SerializeUtils.getIntFromStream(inputStream);
            double newY = SerializeUtils.getIntFromStream(inputStream);
            drawing.addStroke(newX, newY);
        }
        newList.add(drawing);
    }

    private void proceedWithText(ObservableList<DrawableShape> newList) throws BrokenFileException, IOException {
        int textSize = SerializeUtils.getIntFromStream(inputStream);
        byte[] textBytes = inputStream.readNBytes(textSize);
        if(textBytes.length != textSize) throw new BrokenFileException();
        String text = new String(textBytes, StandardCharsets.UTF_8);
        Color color = SerializeUtils.getColorFromStream(inputStream);
        double x = SerializeUtils.getIntFromStream(inputStream);
        double y = SerializeUtils.getIntFromStream(inputStream);
        double width = SerializeUtils.getIntFromStream(inputStream);
        double height = SerializeUtils.getIntFromStream(inputStream);
        WritableText writable = new WritableText(text, color, x, y, width, height);
        newList.add(writable);
    }

    private void proceedWithBezier(ObservableList<DrawableShape> newList) throws BrokenFileException, IOException {
        int waypointsSize = SerializeUtils.getIntFromStream(inputStream);
        BezierCurve curve = new BezierCurve();
        for(int i = 0; i < waypointsSize; i++) {
            double newX = SerializeUtils.getIntFromStream(inputStream);
            double newY = SerializeUtils.getIntFromStream(inputStream);
            curve.addWaypoint(newX, newY);
        }
        double startingPointX = SerializeUtils.getIntFromStream(inputStream);
        double startingPointY = SerializeUtils.getIntFromStream(inputStream);
        Color color = SerializeUtils.getColorFromStream(inputStream);
        double maxX = SerializeUtils.getIntFromStream(inputStream);
        double maxY = SerializeUtils.getIntFromStream(inputStream);
        curve.setStartingPointX(startingPointX);
        curve.setStartingPointY(startingPointY);
        curve.setColor(color);
        curve.setMaxX(maxX);
        curve.setMaxY(maxY);
        newList.add(curve);
    }

    private void proceedWithPolygon(ObservableList<DrawableShape> newList) throws BrokenFileException, IOException {
        int pointsSize = SerializeUtils.getIntFromStream(inputStream);
        DrawablePolygon polygon = new DrawablePolygon();
        for(int i = 0; i < pointsSize / 2; i++) {
            polygon.addPoint(new Point2D(SerializeUtils.getIntFromStream(inputStream),
                    SerializeUtils.getIntFromStream(inputStream)));
        }
        double maxX = SerializeUtils.getIntFromStream(inputStream);
        double maxY = SerializeUtils.getIntFromStream(inputStream);
        double startingPointX = SerializeUtils.getIntFromStream(inputStream);
        double startingPointY = SerializeUtils.getIntFromStream(inputStream);
        Color color = SerializeUtils.getColorFromStream(inputStream);
        polygon.setMaxX(maxX);
        polygon.setMaxY(maxY);
        polygon.setStartingPointX(startingPointX);
        polygon.setStartingPointY(startingPointY);
        polygon.setColor(color);
        newList.add(polygon);
    }

    public ObservableList<DrawableShape> load() throws IOException, BrokenFileException {
        ObservableList<DrawableShape> newList = FXCollections.observableArrayList();
        byte[] openingCode = inputStream.readNBytes(3);
        if(openingCode.length != 3 || openingCode[0] != 10 || openingCode[1] != 20 || openingCode[2] != 21) {
            inputStream.close();
            throw new BrokenFileException();
        }
        while(true) {
            byte input = (byte)inputStream.read();
            if(input == -1) break;
            switch (input) {
                case 0:
                    proceedWithLine(newList);
                    break;
                case 1:
                    proceedWithOval(newList);
                    break;
                case 2:
                    proceedWithRect(newList);
                    break;
                case 3:
                    proceedWithTriangle(newList);
                    break;
                case 4:
                    proceedWithFree(newList);
                    break;
                case 5:
                    proceedWithText(newList);
                    break;
                case 6:
                    proceedWithBezier(newList);
                    break;
                case 7:
                    proceedWithPolygon(newList);
                    break;
                default:
                    throw new BrokenFileException();
            }
        }
        return newList;
    }
}
