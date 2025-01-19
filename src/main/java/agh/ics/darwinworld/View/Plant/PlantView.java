package agh.ics.darwinworld.View.Plant;
import javafx.scene.layout.StackPane;

public class PlantView extends StackPane {
    public PlantView() {
        super();
        setStyle("-fx-background-image: url('/images/EmptyTileImage.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
        setPrefSize(999, 999);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        StackPane plantImage = new StackPane();
        plantImage.setStyle("-fx-background-image: url('/images/Plant.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
        plantImage.setPrefSize(999, 999);
        plantImage.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.getChildren().add(plantImage);
    }
}