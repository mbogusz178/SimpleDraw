package schumi178.javaprojects.graphics.zad1.util.menu;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.image.*;
import schumi178.javaprojects.graphics.zad1.filter.NoParamFilter;
import schumi178.javaprojects.graphics.zad1.filter.util.Color;
import schumi178.javaprojects.graphics.zad1.util.ColorRGBAdapter;

import java.util.function.Supplier;

public class NoParamFilterMenuItem extends MenuItem {

    private final Supplier<NoParamFilter> filterFactory;
    private final ImageView importedImage;

    public NoParamFilterMenuItem(Supplier<NoParamFilter> filterFactory, ImageView importedImage) {
        super();
        setOnAction(this::onAction);
        this.filterFactory = filterFactory;
        this.importedImage = importedImage;
    }

    public NoParamFilterMenuItem(Supplier<NoParamFilter> filterFactory, ImageView importedImage, String text) {
        super(text);
        setOnAction(this::onAction);
        this.filterFactory = filterFactory;
        this.importedImage = importedImage;
    }

    public NoParamFilterMenuItem(Supplier<NoParamFilter> filterFactory, ImageView importedImage, String text, Node graphic) {
        super(text, graphic);
        setOnAction(this::onAction);
        this.filterFactory = filterFactory;
        this.importedImage = importedImage;
    }

    private void onAction(ActionEvent event) {
        NoParamFilter filter = filterFactory.get();
        Image image = importedImage.getImage();
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        Color[][] currentImage = new ColorRGBAdapter[width][height];
        PixelReader reader = image.getPixelReader();
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                currentImage[i][j] = new ColorRGBAdapter(reader.getColor(i, j));
            }
        }
        Color[][] filtered = filter.transform(currentImage);
        WritableImage newImage = new WritableImage(width, height);
        PixelWriter writer = newImage.getPixelWriter();
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                Color readColor = filtered[i][j];
                writer.setColor(i, j, javafx.scene.paint.Color.rgb((int)readColor.getRed(), (int)readColor.getGreen(), (int)readColor.getBlue()));
            }
        }
        importedImage.setImage(newImage);
    }
}
