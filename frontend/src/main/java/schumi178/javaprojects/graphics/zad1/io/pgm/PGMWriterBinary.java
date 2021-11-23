package schumi178.javaprojects.graphics.zad1.io.pgm;

import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import schumi178.javaprojects.graphics.zad1.io.PortableAnymapWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PGMWriterBinary implements PortableAnymapWriter {

    private final OutputStream outputStream;

    public PGMWriterBinary(File file) throws FileNotFoundException {
        outputStream = new FileOutputStream(file);
    }

    @Override
    public void write(WritableImage image) throws IOException {
        outputStream.write("P5\n".getBytes(StandardCharsets.UTF_8));
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        String widthString = width + "";
        String heightString = height + "";
        outputStream.write(widthString.getBytes(StandardCharsets.UTF_8));
        outputStream.write(" ".getBytes(StandardCharsets.UTF_8));
        outputStream.write(heightString.getBytes(StandardCharsets.UTF_8));
        outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
        outputStream.write("255\n".getBytes(StandardCharsets.UTF_8));
        PixelReader reader = image.getPixelReader();
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                Color color = reader.getColor(j, i);
                double grayscaleElement = (color.getRed() + color.getGreen() + color.getBlue()) / 3.0;
                byte pixelValue = (byte)(grayscaleElement * 255.0);
                byte[] pixelValueArray = new byte[1];
                pixelValueArray[0] = pixelValue;
                outputStream.write(pixelValueArray);
            }
        }
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
