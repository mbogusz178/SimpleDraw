package schumi178.javaprojects.graphics.zad1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import schumi178.javaprojects.graphics.zad1.filter.*;
import schumi178.javaprojects.graphics.zad1.filter.binarization.*;
import schumi178.javaprojects.graphics.zad1.filter.morpho.*;
import schumi178.javaprojects.graphics.zad1.util.ImageViewPane;
import schumi178.javaprojects.graphics.zad1.util.menu.NoParamFilterMenuItem;
import schumi178.javaprojects.graphics.zad1.util.menu.SingleParamFilterMenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class ImportFileViewController implements Initializable {

    private ImageView importedImage;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu singleParamFilterMenu;
    @FXML
    private Menu noParamFilterMenu;
    @FXML
    private Menu binarizationFilterMenu;
    @FXML
    private Menu morphoFilterMenu;
    @FXML
    private StackPane imageRoot;

    @FXML
    private Stage stage;

    public void setImportedImage(Image image) {
        stage.setWidth(image.getWidth() >= 100 ? image.getWidth() : 100);
        stage.setHeight(image.getHeight() + menuBar.getHeight() >= 100 ? image.getHeight() + menuBar.getHeight() : 100);
        ImageViewPane imageViewPane = new ImageViewPane(importedImage);
        imageRoot.getChildren().add(imageViewPane);
        importedImage.setPreserveRatio(false);
        importedImage.setImage(image);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        importedImage = new ImageView();
        singleParamFilterMenu.getItems().addAll(new SingleParamFilterMenuItem(AdditiveFilter::new, importedImage, "Filtr addytywny"),
                new SingleParamFilterMenuItem(SubtractiveFilter::new, importedImage, "Filtr odejmowania"),
                new SingleParamFilterMenuItem(MultiplicativeFilter::new, importedImage, "Filtr mno??enia"),
                new SingleParamFilterMenuItem(DivisionFilter::new, importedImage, "Filtr dzielenia"),
                new SingleParamFilterMenuItem(BrightenFilter::new, importedImage, "Zwi??ksz jasno????"),
                new SingleParamFilterMenuItem(DarkenFilter::new, importedImage, "Zmniejsz jasno????"));
        noParamFilterMenu.getItems().addAll(new NoParamFilterMenuItem(GreyscaleFilter::new, importedImage, "Filtr szaro??ci"),
                new NoParamFilterMenuItem(AverageFilter::new, importedImage, "Filtr u??redniaj??cy"),
                new NoParamFilterMenuItem(MedianFilter::new, importedImage, "Filtr medianowy"));
        Menu sobelMenu = new Menu("Filtr Sobela");
        sobelMenu.getItems().addAll(new NoParamFilterMenuItem(HorizontalSobelFilter::new, importedImage, "Poziomy"),
                new NoParamFilterMenuItem(VerticalSobelFilter::new, importedImage, "Pionowy"));
        noParamFilterMenu.getItems().addAll(sobelMenu);
        noParamFilterMenu.getItems().addAll(new NoParamFilterMenuItem(SharpenFilter::new, importedImage, "Filtr g??rnoprzepustowy wyostrzaj??cy"),
                new NoParamFilterMenuItem(GaussianBlurFilter::new, importedImage, "Filtr rozmycie gaussowskie"),
                new NoParamFilterMenuItem(CustomFilter::new, importedImage, "Filtr z w??asn?? mask??"),
                new NoParamFilterMenuItem(HistogramEqualizationFilter::new, importedImage, "Wyr??wnywanie histogramu"),
                new NoParamFilterMenuItem(HistogramExtensionFilter::new, importedImage, "Rozszerzenie histogramu"));
        binarizationFilterMenu.getItems().addAll(new SingleParamFilterMenuItem(ManualBinarizationFilter::new, importedImage, "R??czna", 100),
                new SingleParamFilterMenuItem(PercentBlackSelectionBinarizationFilter::new, importedImage, "Procentowa selekcja czarnego", 100),
                new NoParamFilterMenuItem(MeanIterativeSelectionBinarizationFilter::new, importedImage, "Selekcja iteratywna ??redniej"),
                new NoParamFilterMenuItem(OtsuBinarizationFilter::new, importedImage, "Otsu"),
                new NoParamFilterMenuItem(NiblackBinarizationFilter::new, importedImage, "Niblack"),
                new NoParamFilterMenuItem(SauvolaBinarizationFilter::new, importedImage, "Sauvola"),
                new NoParamFilterMenuItem(PhansalkarBinarizationFilter::new, importedImage, "Phansalkar"));
        morphoFilterMenu.getItems().addAll(new NoParamFilterMenuItem(ErosionFilter::new, importedImage, "Erozja"),
                new NoParamFilterMenuItem(DilationFilter::new, importedImage, "Dylatacja"),
                new NoParamFilterMenuItem(OpeningFilter::new, importedImage, "Otwarcie"),
                new NoParamFilterMenuItem(ClosingFilter::new, importedImage, "Domkni??cie"),
                new NoParamFilterMenuItem(HitOrMissFilter::new, importedImage, "Hit-or-miss"));
    }
}
