package schumi178.javaprojects.graphics.zad1.io.ppm;

import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import schumi178.javaprojects.graphics.zad1.io.PortableAnymapWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PPMWriterBinary implements PortableAnymapWriter {

    private final OutputStream outputStream;

    public PPMWriterBinary(File file) throws FileNotFoundException {
        outputStream = new FileOutputStream(file);
    }

    @Override
    public void write(WritableImage image) throws IOException {
        outputStream.write("P6\n".getBytes(StandardCharsets.UTF_8));
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
                byte red = (byte)(color.getRed() * 255.0);
                byte green = (byte)(color.getGreen() * 255.0);
                byte blue = (byte)(color.getBlue() * 255.0);
                byte[] pixelGroupArray = new byte[3];
                pixelGroupArray[0] = red;
                pixelGroupArray[1] = green;
                pixelGroupArray[2] = blue;
                outputStream.write(pixelGroupArray);
            }
        }
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
