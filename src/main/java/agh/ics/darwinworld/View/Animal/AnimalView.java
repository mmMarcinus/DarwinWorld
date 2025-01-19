package agh.ics.darwinworld.View.Animal;
import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Presenter.Simulation.SimulationPresenter;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class AnimalView extends StackPane{
    public AnimalView(SimulationPresenter simulationPresenter, Animal animal) {
        super();
        setStyle("-fx-background-image: url('/images/EmptyTileImage.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
        setPrefSize(999,999);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        StackPane animalImage = new StackPane();
        animalImage.setStyle("-fx-background-image: url('/images/Animal.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
        animalImage.setPrefSize(999, 999);
        animalImage.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.getChildren().add(animalImage);

        setOnMouseClicked(event -> {
            if(!animal.isHighlighted()){
                Circle greenCircle = new Circle();
                greenCircle.setRadius(30);
                greenCircle.setStroke(Color.LIGHTGREEN);
                greenCircle.setFill(Color.TRANSPARENT);
                greenCircle.setStrokeWidth(3);
                this.getChildren().add(greenCircle);
                simulationPresenter.highlightAnimal(animal);
            }
            //simulationPresenter.drawAnimalStats(animal);
        });
    }
}