package agh.ics.darwinworld.View.Animal;
import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Presenter.Simulation.SimulationPresenter;
import javafx.scene.layout.StackPane;

public class AnimalView extends StackPane{
    public AnimalView(SimulationPresenter simulationPresenter, Animal animal) {
        super();
        setStyle("-fx-background-color: #95bb65; -fx-background-image: url('/images/Animal.png'); -fx-background-size: 100% 100%;");
        setPrefWidth(999);
        setPrefHeight(999);
        setOnMouseClicked(event -> {
            System.out.println("Clicked");
            simulationPresenter.drawAnimalStats(animal);
        });
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }
}