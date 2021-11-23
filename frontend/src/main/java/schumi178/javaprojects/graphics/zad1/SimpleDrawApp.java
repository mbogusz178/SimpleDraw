package schumi178.javaprojects.graphics.zad1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class SimpleDrawApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleDrawApp.class.getResource("/fxml/simpledraw.fxml"));
        Stage stage = fxmlLoader.load();
        stage.show();
    }

    public static void startApp(String[] args) {
        launch(args);
    }
}