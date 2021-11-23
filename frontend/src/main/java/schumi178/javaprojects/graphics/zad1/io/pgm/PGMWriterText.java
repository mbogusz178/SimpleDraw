package schumi178.javaprojects.graphics.zad1.io.pgm;

import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import schumi178.javaprojects.graphics.zad1.io.PortableAnymapWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class PGMWriterText implements PortableAnymapWriter {

    private final PrintWriter writer;

    public PGMWriterText(File file) throws FileNotFoundException {
        writer = new PrintWriter(file);
    }

    @Override
    public void write(WritableImage image) {
        writer.write("P2\n");
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        writer.write(width + " " + height + "\n");
        writer.write("255\n");
        PixelReader reader = image.getPixelReader();
        int newLineCounter = 0;
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if(newLineCounter > 50) {
                    writer.write("\n");
                    newLineCounter = 0;
                }
                Color color = reader.getColor(j, i);
                double grayscaleElement = (color.getRed() + color.getGreen() + color.getBlue()) / 3.0;
                int pixelValue = (int)(grayscaleElement * 255.0);
                writer.write(pixelValue + " ");
                newLineCounter++;
            }
        }
    }

    @Override
    public void close() {
        writer.close();
    }
}
