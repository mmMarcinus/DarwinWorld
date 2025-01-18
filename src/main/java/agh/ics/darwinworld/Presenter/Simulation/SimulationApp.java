package agh.ics.darwinworld.Presenter.Simulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Objects;

public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = fxmlLoader.load();

        configureStage(primaryStage, viewRoot);

        primaryStage.show();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);

        String css = getClass().getClassLoader().getResource("simulation.css").toExternalForm();
        scene.getStylesheets().add(css);

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Icon.png")));
        primaryStage.getIcons().add(icon);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation");
        primaryStage.setResizable(false);
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
