package schumi178.javaprojects.graphics.zad1.io.pbm;

import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import schumi178.javaprojects.graphics.zad1.io.PortableAnymapWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PBMWriterBinary implements PortableAnymapWriter {

    private final OutputStream outputStream;

    public PBMWriterBinary(File file) throws FileNotFoundException {
        outputStream = new FileOutputStream(file);
    }

    @Override
    public void write(WritableImage image) throws IOException {
        outputStream.write("P4\n".getBytes(StandardCharsets.UTF_8));
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        String widthString = width + "";
        String heightString = height + "";
        outputStream.write(widthString.getBytes(StandardCharsets.UTF_8));
        outputStream.write(" ".getBytes(StandardCharsets.UTF_8));
        outputStream.write(heightString.getBytes(StandardCharsets.UTF_8));
        outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
        PixelReader reader = image.getPixelReader();
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j += 8) {
                boolean[] colorByte = new boolean[8];
                for(int k = 0; k < 8; k++) {
                    Color color = j + k < width ? reader.getColor(j + k, i) : Color.BLACK;
                    colorByte[k] = color.getRed() != 1.0 || color.getGreen() != 1.0 || color.getBlue() != 1.0;
                }
                byte pixelGroup = 0;
                for(int k = 0; k < 8; k++) {
                    pixelGroup |= (colorByte[k] ? (0b10000000 >> k) : 0);
                }
                byte[] pixelGroupArray = new byte[1];
                pixelGroupArray[0] = pixelGroup;
                outputStream.write(pixelGroupArray);
            }
        }
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
