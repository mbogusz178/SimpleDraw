package schumi178.javaprojects.graphics.zad1.util;

import javafx.scene.paint.Color;
import schumi178.javaprojects.graphics.zad1.exception.BrokenFileException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class SerializeUtils {
    public static byte[] intToByte(int number) {
        return ByteBuffer.allocate(4).putInt(number).array();
    }

    public static byte[] colorToByte(Color color) {
        byte[] colorBytes = new byte[4];
        colorBytes[0] = (byte)(Math.round(color.getRed() * 255) - 128.0);
        colorBytes[1] = (byte)(Math.round(color.getGreen() * 255) - 128.0);
        colorBytes[2] = (byte)(Math.round(color.getBlue() * 255) - 128.0);
        colorBytes[3] = (byte)(Math.round(color.getOpacity() * 255) - 128.0);
        return colorBytes;
    }

    public static byte[] joinBytes(List<byte[]> list) {
        int counter = 0;
        int size = list.stream().map(e -> e.length).reduce(0, Integer::sum);
        byte[] resultBytes = new byte[size];
        for(byte[] bytes: list) {
            for(byte b: bytes) {
                resultBytes[counter] = b;
                counter++;
            }
        }
        return resultBytes;
    }

    private static int byteToInt(byte[] arr) {
        return ByteBuffer.wrap(arr).getInt();
    }

    public static int getIntFromStream(InputStream inputStream) throws BrokenFileException, IOException {
        byte[] bytes = inputStream.readNBytes(4);
        if(bytes.length != 4) {
            inputStream.close();
            throw new BrokenFileException();
        }
        return byteToInt(bytes);
    }

    public static Color getColorFromStream(InputStream inputStream) throws IOException, BrokenFileException {
        byte[] bytes = inputStream.readNBytes(4);
        if(bytes.length != 4) {
            inputStream.close();
            throw new BrokenFileException();
        }
        return new Color((bytes[0] + 128.0) / 255.0, (bytes[1] + 128.0) / 255.0, (bytes[2] + 128.0) / 255.0, (bytes[3] + 128.0) / 255.0);
    }
}
