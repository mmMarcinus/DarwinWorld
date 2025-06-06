package agh.ics.darwinworld.View.Animal;
import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Presenter.Simulation.SimulationPresenter;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class AnimalView extends StackPane{

    public AnimalView(SimulationPresenter simulationPresenter, Animal animal) {
        super();

        setStyle("-fx-background-image: url('/images/Animal/Animal.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
        setPrefSize(999,999);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        ProgressBar energyBar = new ProgressBar((double) animal.getEnergyLevel()/simulationPresenter.getWorldParameters().reproduceEnergyRequired());
        energyBar.setMaxHeight(10);
        energyBar.setStyle("-fx-accent: lightgreen");
        this.getChildren().add(energyBar);

        this.setAlignment(Pos.TOP_CENTER);

        setOnMouseClicked(event -> {
            System.out.println("mouse clicked");
            if(!animal.isHighlighted() && !simulationPresenter.isAnimalHighlighted()){
                setStyle("-fx-background-image: url('/images/Animal/ClickedAnimal.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
                simulationPresenter.highlightAnimal(animal);
            }
        });
    }
}