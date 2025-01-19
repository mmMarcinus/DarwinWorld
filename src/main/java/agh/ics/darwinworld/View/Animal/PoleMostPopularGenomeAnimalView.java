package agh.ics.darwinworld.View.Animal;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Presenter.Simulation.SimulationPresenter;

public class PoleMostPopularGenomeAnimalView extends AnimalView {
    public PoleMostPopularGenomeAnimalView(SimulationPresenter simulationPresenter, Animal animal) {
        super(simulationPresenter, animal);
        this.setStyle("-fx-background-color: #95bb65; -fx-background-image: url('/images/HighlightedPoleAnimal.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
    }
}
