package schumi178.javaprojects.graphics.zad1.io.ppm;

import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import schumi178.javaprojects.graphics.zad1.io.PortableAnymapWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class PPMWriterText implements PortableAnymapWriter {

    private final PrintWriter writer;

    public PPMWriterText(File file) throws FileNotFoundException {
        writer = new PrintWriter(file);
    }

    @Override
    public void write(WritableImage image) {
        writer.write("P3\n");
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
                int red = (int)(color.getRed() * 255.0);
                int green = (int)(color.getGreen() * 255.0);
                int blue = (int)(color.getBlue() * 255.0);
                writer.write(red + " " + green + " " + blue + " ");
                newLineCounter += 9;
            }
        }
    }

    @Override
    public void close() {
        writer.close();
    }
}
