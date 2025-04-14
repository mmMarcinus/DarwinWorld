package agh.ics.darwinworld.Presenter.Menu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Objects;

public class MapMenuApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("MapMenu.fxml"));
        GridPane viewRoot = fxmlLoader.load();

        MapMenuPresenter presenter = fxmlLoader.getController();

        configureStage(primaryStage, viewRoot);

        primaryStage.show();
    }

    private void configureStage(Stage primaryStage, GridPane viewRoot) {
        var scene = new Scene(viewRoot);

        String css = getClass().getClassLoader().getResource("menu.css").toExternalForm();
        scene.getStylesheets().add(css);

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Icon.png")));
        primaryStage.getIcons().add(icon);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Map Menu");
        primaryStage.setResizable(false);
    }
}
