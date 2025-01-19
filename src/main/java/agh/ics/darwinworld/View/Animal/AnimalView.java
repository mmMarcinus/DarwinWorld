package agh.ics.darwinworld.View.Animal;
import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Presenter.Simulation.SimulationPresenter;
import javafx.geometry.Pos;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;


public class AnimalView extends StackPane{
    public AnimalView(SimulationPresenter simulationPresenter, Animal animal) {
        super();
        setStyle("-fx-background-image: url('/images/EmptyTile/EmptyTileImage.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
        setPrefSize(999,999);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        StackPane animalImage = new StackPane();
        animalImage.setStyle("-fx-background-image: url('/images/Animal/Animal.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
        animalImage.setPrefSize(999, 999);
        animalImage.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.getChildren().add(animalImage);

        ProgressBar energyBar = new ProgressBar((double) animal.getEnergyLevel()/simulationPresenter.getWorldParameters().reproduceEnergyRequired());
        energyBar.setMaxHeight(10);
        energyBar.setStyle("-fx-accent: lightgreen");
        this.getChildren().add(energyBar);

        this.setAlignment(Pos.TOP_CENTER);

        setOnMouseClicked(event -> {
            if(!animal.isHighlighted()){
                animalImage.setStyle("-fx-background-image: url('/images/Animal/ClickedAnimal.png'); -fx-background-size: 100% 100%; -fx-background-color: transparent;");
                simulationPresenter.highlightAnimal(animal);
            }
        });
    }
}