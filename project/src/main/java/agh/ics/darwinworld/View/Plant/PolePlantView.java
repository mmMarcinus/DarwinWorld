package agh.ics.darwinworld.View.Plant;

import agh.ics.darwinworld.View.EmptyTile.EmptyTileView;
import javafx.scene.layout.StackPane;

public class PolePlantView extends StackPane {
    public PolePlantView() {
        super();
        setStyle("-fx-background-image: url('/images/EmptyTile/Pole.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
        setPrefSize(999, 999);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        StackPane plantImage = new StackPane();
        plantImage.setStyle("-fx-background-image: url('/images/Plant/PolePlant.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
        plantImage.setPrefSize(999, 999);
        plantImage.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.getChildren().add(plantImage);
    }
}
