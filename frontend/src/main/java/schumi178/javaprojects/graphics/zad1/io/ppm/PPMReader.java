package schumi178.javaprojects.graphics.zad1.io.ppm;

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

public class PPMReader implements PortableAnymapReader {

    private final Scanner scanner;
    private final InputStream input;

    public PPMReader(File file) throws FileNotFoundException {
        scanner = new Scanner(new BufferedReader(new FileReader(file)));
        input = new FileInputStream(file);
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

    private static int log2(int n) {
        return (int)(Math.log(n) / Math.log(2));
    }

    private Image readBinary() throws BrokenFileException, IOException {
        int width = checkedNextIntBinary();
        int height = checkedNextIntBinary();
        int maxValue = checkedNextIntBinary();
        int bitNumber = log2(maxValue) + 1;
        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();
        BitInputStream bitInputStream = new BitInputStream(input);
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                byte[] redBits = bitInputStream.readNBytes(bitNumber);
                if(redBits.length != bitNumber) {
                    throw new BrokenFileException();
                }
                byte[] greenBits = bitInputStream.readNBytes(bitNumber);
                if(greenBits.length != bitNumber) {
                    throw new BrokenFileException();
                }
                byte[] blueBits = bitInputStream.readNBytes(bitNumber);
                if(blueBits.length != bitNumber) {
                    throw new BrokenFileException();
                }
                byte completeRedByte = 0;
                for(int k = 0; k < redBits.length; k++) {
                    completeRedByte |= (redBits[k] == 0 ? 0 : (0b10000000 >> k));
                }
                byte completeGreenByte = 0;
                for(int k = 0; k < greenBits.length; k++) {
                    completeGreenByte |= (greenBits[k] == 0 ? 0 : (0b10000000 >> k));

                }
                byte completeBlueByte = 0;
                for(int k = 0; k < blueBits.length; k++) {
                    completeBlueByte |= (blueBits[k] == 0 ? 0 : (0b10000000 >> k));
                }
                double completeRed = ((completeRedByte & 0xFF) / (double)maxValue);
                double completeGreen = ((completeGreenByte & 0xFF) / (double)maxValue);
                double completeBlue = ((completeBlueByte & 0xFF) / (double)maxValue);
                writer.setColor(j, i, Color.color(completeRed, completeGreen, completeBlue));
            }
        }
        return image;
    }

    private Image readText() throws BrokenFileException {
        int width = checkedNextInt();
        int height = checkedNextInt();
        int maxValue = checkedNextInt();
        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                double redValue = checkedNextInt() / (double)maxValue;
                double greenValue = checkedNextInt() / (double)maxValue;
                double blueValue = checkedNextInt() / (double)maxValue;
                if(redValue < 0 || redValue > 1
                || greenValue < 0 || greenValue > 1
                || blueValue < 0 || blueValue > 1) {
                    throw new BrokenFileException();
                }
                writer.setColor(j, i, Color.color(redValue, greenValue, blueValue));
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
        if(opening.equals("P3")) {
            return readText();
        } else if(opening.equals("P6")) {
            long skipped = input.skip(3);
            if(skipped != 3) {
                throw new BrokenFileException();
            }
            return readBinary();
        }
        throw new BrokenFileException();
    }

    @Override
    public void close() {
        scanner.close();
    }
}
