package schumi178.javaprojects.graphics.zad1.io.pbm;

import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import schumi178.javaprojects.graphics.zad1.io.PortableAnymapWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class PBMWriterText implements PortableAnymapWriter {

    private final PrintWriter writer;

    public PBMWriterText(File file) throws FileNotFoundException {
        writer = new PrintWriter(file);
    }

    @Override
    public void write(WritableImage image) {
        writer.write("P1\n");
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        writer.write(width + " " + height + "\n");
        PixelReader pixelReader = image.getPixelReader();
        int newLineCounter = 0;
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if(newLineCounter > 50) {
                    writer.write("\n");
                    newLineCounter = 0;
                }
                Color color = pixelReader.getColor(j, i);
                if(color.getRed() == 1.0 && color.getGreen() == 1.0 && color.getBlue() == 1.0) {
                    writer.write("0 ");
                } else writer.write("1 ");
                newLineCounter++;
            }
        }
    }

    @Override
    public void close() {
        writer.close();
    }
}
