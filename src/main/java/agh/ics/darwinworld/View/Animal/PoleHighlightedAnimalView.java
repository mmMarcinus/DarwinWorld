package agh.ics.darwinworld.View.Animal;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Presenter.Simulation.SimulationPresenter;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;

public class PoleHighlightedAnimalView extends AnimalView {
    public PoleHighlightedAnimalView(SimulationPresenter simulationPresenter, Animal animal) {
        super(simulationPresenter, animal);
        this.getChildren().clear();

        setStyle("-fx-background-image: url('/images/EmptyTile/Pole.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
        setPrefSize(999,999);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        StackPane animalImage = new StackPane();
        animalImage.setStyle("-fx-background-image: url('/images/Animal/ClickedPoleAnimal.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
        animalImage.setPrefSize(999, 999);
        animalImage.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.getChildren().add(animalImage);

        ProgressBar energyBar = new ProgressBar((double) animal.getEnergyLevel()/simulationPresenter.getWorldParameters().reproduceEnergyRequired());
        energyBar.setMaxHeight(10);
        energyBar.setStyle("-fx-accent: lightgreen");
        this.getChildren().add(energyBar);
    }
}
