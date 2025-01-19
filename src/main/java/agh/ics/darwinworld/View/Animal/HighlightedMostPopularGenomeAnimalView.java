package agh.ics.darwinworld.View.Animal;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Presenter.Simulation.SimulationPresenter;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class HighlightedMostPopularGenomeAnimalView extends AnimalView {
    public HighlightedMostPopularGenomeAnimalView(SimulationPresenter simulationPresenter, Animal animal) {
        super(simulationPresenter, animal);

        this.getChildren().clear();

        StackPane animalImage = new StackPane();
        animalImage.setStyle("-fx-background-image: url('/images/HighlightedAnimal.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
        animalImage.setPrefSize(999, 999);
        animalImage.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.getChildren().add(animalImage);

        ProgressBar energyBar = new ProgressBar((double) animal.getEnergyLevel()/simulationPresenter.getWorldParameters().reproduceEnergyRequired());
        energyBar.setMaxHeight(10);
        energyBar.setStyle("-fx-accent: lightgreen");
        this.getChildren().add(energyBar);

        Circle greenCircle = new Circle();
        greenCircle.setRadius(30);
        greenCircle.setStroke(Color.LIGHTGREEN);
        greenCircle.setFill(Color.TRANSPARENT);
        greenCircle.setStrokeWidth(3);
        this.getChildren().add(greenCircle);
    }
}
