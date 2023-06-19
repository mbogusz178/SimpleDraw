package schumi178.javaprojects.graphics.zad1.util.menu;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.image.*;
import schumi178.javaprojects.graphics.zad1.filter.SingleParamFilter;
import schumi178.javaprojects.graphics.zad1.filter.util.Color;
import schumi178.javaprojects.graphics.zad1.util.ColorRGBAdapter;
import schumi178.javaprojects.graphics.zad1.util.IntInputDialog;

import java.util.Optional;
import java.util.function.Supplier;

public class SingleParamFilterMenuItem extends MenuItem {

    private final Supplier<SingleParamFilter> filterFactory;
    private final ImageView importedImage;
    private final String title;
    private final Integer range;

    public SingleParamFilterMenuItem(Supplier<SingleParamFilter> filterFactory, ImageView importedImage) {
        super();
        title = "Wprowadź wartość";
        setOnAction(this::onAction);
        this.filterFactory = filterFactory;
        this.importedImage = importedImage;
        range = null;
    }

    public SingleParamFilterMenuItem(Supplier<SingleParamFilter> filterFactory, ImageView importedImage, String text) {
        super(text);
        title = text;
        setOnAction(this::onAction);
        this.filterFactory = filterFactory;
        this.importedImage = importedImage;
        range = null;
    }

    public SingleParamFilterMenuItem(Supplier<SingleParamFilter> filterFactory, ImageView importedImage, String text, int range) {
        super(text);
        title = text;
        setOnAction(this::onAction);
        this.filterFactory = filterFactory;
        this.importedImage = importedImage;
        this.range = range;
    }

    public SingleParamFilterMenuItem(Supplier<SingleParamFilter> filterFactory, ImageView importedImage, String text, Node graphic) {
        super(text, graphic);
        title = text;
        setOnAction(this::onAction);
        this.filterFactory = filterFactory;
        this.importedImage = importedImage;
        range = null;
    }

    private void onAction(ActionEvent event) {
        IntInputDialog dialog;
        if(range == null) {
            dialog = new IntInputDialog();
        } else {
            dialog = new IntInputDialog(range);
        }
        dialog.setTitle(title);
        dialog.setHeaderText("Wprowadź wartość");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) {
            String text = result.get();
            double addition = Integer.parseInt(text);
            SingleParamFilter filter = filterFactory.get();
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
            Color[][] filtered = filter.transform(currentImage, addition);
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
}
