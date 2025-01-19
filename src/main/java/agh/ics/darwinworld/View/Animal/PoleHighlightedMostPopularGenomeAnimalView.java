package agh.ics.darwinworld.View.Animal;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Presenter.Simulation.SimulationPresenter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PoleHighlightedMostPopularGenomeAnimalView extends AnimalView {
    public PoleHighlightedMostPopularGenomeAnimalView(SimulationPresenter simulationPresenter, Animal animal) {
        super(simulationPresenter, animal);
        this.setStyle("-fx-background-color: #95bb65; -fx-background-image: url('/images/HighlightedPoleAnimal.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
        Circle greenCircle = new Circle();
        greenCircle.setRadius(30);
        greenCircle.setStroke(Color.LIGHTGREEN);
        greenCircle.setFill(Color.TRANSPARENT);
        greenCircle.setStrokeWidth(3);
        this.getChildren().add(greenCircle);
    }
}
