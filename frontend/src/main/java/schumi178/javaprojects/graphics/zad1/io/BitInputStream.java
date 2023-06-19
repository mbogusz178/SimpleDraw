package schumi178.javaprojects.graphics.zad1.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class BitInputStream extends InputStream implements Closeable {

    private final InputStream input;
    private int num = 0;
    private int count = 8;

    public BitInputStream(InputStream input) {
        this.input = input;
    }

    @Override
    public int read() throws IOException {
        if(count == 8) {
            int readValue = input.read();
            if(readValue == -1) {
                return -1;
            }
            num = readValue;
            count = 0;
        }

        int singleBit = 0b10000000;
        singleBit >>= count;

        int x = num & singleBit;
        if(x != 0) {
            x = 1;
        }
        count++;
        return x;
    }

    @Override
    public void close() throws IOException {
        input.close();
    }
}
