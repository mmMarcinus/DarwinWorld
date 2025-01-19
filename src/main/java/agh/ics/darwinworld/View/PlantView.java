package agh.ics.darwinworld.View;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class PlantView extends StackPane {
    public PlantView() {
        super();
        setStyle("-fx-background-color: #95bb65; -fx-background-image: url('/images/Plant.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
        setPrefWidth(999);
        setPrefHeight(999);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        getChildren().add(new Label("Plant"));
    }
}