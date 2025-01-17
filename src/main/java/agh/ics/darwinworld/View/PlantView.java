package agh.ics.darwinworld.View;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class PlantView extends StackPane {
    public PlantView() {
        super();
        setStyle("-fx-background-color: #95bb65;");
//        ImageView imageView = new ImageView();
//        Image image = new Image(getClass().getResource("/images/PlantImage.png").toExternalForm());
//        imageView.setImage(image);
//        imageView.setFitWidth(10);
//        imageView.setFitHeight(10);
//        this.getChildren().add(imageView);

        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }
}