package agh.ics.darwinworld.MenuModel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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

        String css = getClass().getClassLoader().getResource("MapMenu.css").toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setScene(scene);
        primaryStage.setTitle("MapMenu");
    }
}
