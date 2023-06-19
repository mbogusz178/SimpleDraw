package schumi178.javaprojects.graphics.zad1.io;

import javafx.scene.image.Image;
import schumi178.javaprojects.graphics.zad1.exception.BrokenFileException;
import schumi178.javaprojects.graphics.zad1.io.pbm.PBMReader;
import schumi178.javaprojects.graphics.zad1.io.pgm.PGMReader;
import schumi178.javaprojects.graphics.zad1.io.ppm.PPMReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface PortableAnymapReader {
    static PortableAnymapReader getInstance(File file) throws FileNotFoundException {
        String name = file.getName();
        if(name.endsWith(".pbm")) return new PBMReader(file);
        if(name.endsWith(".pgm")) return new PGMReader(file);
        if(name.endsWith(".ppm")) return new PPMReader(file);
        throw new IllegalArgumentException("Nieznany typ pliku.");
    }
    Image read() throws BrokenFileException, IOException;
    void close() throws IOException;
}
