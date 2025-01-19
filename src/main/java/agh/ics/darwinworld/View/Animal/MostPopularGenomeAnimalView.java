package agh.ics.darwinworld.View.Animal;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Presenter.Simulation.SimulationPresenter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MostPopularGenomeAnimalView extends AnimalView {
    public MostPopularGenomeAnimalView(SimulationPresenter simulationPresenter, Animal animal) {
        super(simulationPresenter, animal);
        this.setStyle("-fx-background-color: #95bb65; -fx-background-image: url('/images/HighlightedAnimal.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
    }
}
