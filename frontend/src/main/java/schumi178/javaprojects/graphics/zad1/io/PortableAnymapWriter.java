package schumi178.javaprojects.graphics.zad1.io;

import javafx.scene.image.WritableImage;
import schumi178.javaprojects.graphics.zad1.io.pbm.PBMWriterBinary;
import schumi178.javaprojects.graphics.zad1.io.pbm.PBMWriterText;
import schumi178.javaprojects.graphics.zad1.io.pgm.PGMWriterBinary;
import schumi178.javaprojects.graphics.zad1.io.pgm.PGMWriterText;
import schumi178.javaprojects.graphics.zad1.io.ppm.PPMWriterBinary;
import schumi178.javaprojects.graphics.zad1.io.ppm.PPMWriterText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface PortableAnymapWriter {
    static PortableAnymapWriter getInstance(File file, boolean isBinary) throws FileNotFoundException {
        String name = file.getName();
        if(isBinary) {
            if(name.endsWith(".pbm")) return new PBMWriterBinary(file);
            if(name.endsWith(".pgm")) return new PGMWriterBinary(file);
            if(name.endsWith(".ppm")) return new PPMWriterBinary(file);
        } else {
            if (name.endsWith(".pbm")) return new PBMWriterText(file);
            if (name.endsWith(".pgm")) return new PGMWriterText(file);
            if (name.endsWith(".ppm")) return new PPMWriterText(file);
        }
        throw new IllegalArgumentException("Nieznany typ pliku.");
    }
    void write(WritableImage image) throws IOException;
    void close() throws IOException;
}
