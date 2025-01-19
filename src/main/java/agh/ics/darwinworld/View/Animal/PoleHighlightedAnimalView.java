package agh.ics.darwinworld.View.Animal;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Presenter.Simulation.SimulationPresenter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PoleHighlightedAnimalView extends AnimalView {
    public PoleHighlightedAnimalView(SimulationPresenter simulationPresenter, Animal animal) {
        super(simulationPresenter, animal);
        Circle greenCircle = new Circle();
        greenCircle.setRadius(30);
        greenCircle.setStroke(Color.LIGHTGREEN);
        greenCircle.setFill(Color.TRANSPARENT);
        greenCircle.setStrokeWidth(3);
        this.getChildren().add(greenCircle);
    }
}
