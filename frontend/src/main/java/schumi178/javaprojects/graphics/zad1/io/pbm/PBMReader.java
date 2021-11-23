package schumi178.javaprojects.graphics.zad1.io.pbm;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import schumi178.javaprojects.graphics.zad1.exception.BrokenFileException;
import schumi178.javaprojects.graphics.zad1.io.BitInputStream;
import schumi178.javaprojects.graphics.zad1.io.PortableAnymapReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PBMReader implements PortableAnymapReader {

    private final InputStream input;
    private final Scanner scanner;

    public PBMReader(File file) throws FileNotFoundException {
        input = new FileInputStream(file);
        scanner = new Scanner(file);
    }

    private void skipCommentBinary() throws BrokenFileException, IOException {
        while(true) {
            byte[] text = input.readNBytes(1);
            if(text.length != 1) {
                throw new BrokenFileException();
            }
            String c = new String(text, StandardCharsets.UTF_8);
            if(c.charAt(0) == '\n') {
                return;
            }
        }
    }

    private String checkedNextBinary() throws IOException, BrokenFileException {
        StringBuilder resultString = new StringBuilder();
        while(true) {
            byte[] text = input.readNBytes(1);
            if(text.length != 1) {
                throw new BrokenFileException();
            }
            String c = new String(text, StandardCharsets.UTF_8);
            if(c.charAt(0) == '#') {
                skipCommentBinary();
                return checkedNextBinary();
            }
            if(Character.isWhitespace(c.charAt(0))) {
                break;
            }
            resultString.append(c);
        }
        return resultString.toString();
    }

    private String checkedNext() throws BrokenFileException {
        if(!scanner.hasNext()) {
            throw new BrokenFileException();
        }
        String next = scanner.next();
        if(next.startsWith("#")) {
            scanner.skip(".*");
            return checkedNext();
        }
        return next;
    }

    private int checkedNextIntBinary() throws BrokenFileException, IOException {
        StringBuilder resultString = new StringBuilder();
        while(true) {
            byte[] text = input.readNBytes(1);
            if(text.length != 1) {
                throw new BrokenFileException();
            }
            String c = new String(text, StandardCharsets.UTF_8);
            if(c.charAt(0) == '#') {
                skipCommentBinary();
                return checkedNextIntBinary();
            }
            if(Character.isWhitespace(c.charAt(0))) {
                break;
            }
            resultString.append(c);
        }
        return Integer.parseInt(resultString.toString());
    }

    private int checkedNextInt() throws BrokenFileException {
        if(!scanner.hasNext()) {
            throw new BrokenFileException();
        }
        String next = scanner.next();
        if(next.startsWith("#")) {
            scanner.skip(".*");
            return checkedNextInt();
        }
        return Integer.parseInt(next);
    }

    private Image readBinary() throws BrokenFileException, IOException {
        int width = checkedNextIntBinary();
        int height = checkedNextIntBinary();
        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();
        BitInputStream bitInputStream = new BitInputStream(input);
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                int bit = bitInputStream.read();
                if(bit == -1) {
                    throw new BrokenFileException();
                }
                if(bit == 1) writer.setColor(j, i, Color.BLACK);
                else writer.setColor(j, i, Color.WHITE);
            }
        }
        return image;
    }

    private Image readText() throws BrokenFileException {
        int width = checkedNextInt();
        int height = checkedNextInt();
        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                int color = checkedNextInt();
                if(color == 1) {
                    writer.setColor(j, i, Color.BLACK);
                } else writer.setColor(j, i, Color.WHITE);
            }
        }
        return image;
    }

    @Override
    public Image read() throws BrokenFileException, IOException {
        if(!scanner.hasNext()) {
            throw new BrokenFileException();
        }
        String opening = scanner.next();
        if(opening.equals("P1")) {
            return readText();
        } else if(opening.equals("P4")) {
            long skipped = input.skip(3);
            if(skipped != 3) {
                throw new BrokenFileException();
            }
            return readBinary();
        }
        throw new BrokenFileException();
    }

    @Override
    public void close() throws IOException {
        scanner.close();
        input.close();
    }
}
