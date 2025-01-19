package agh.ics.darwinworld.Presenter.Simulation;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Model.SimulationModel.Simulation;
import agh.ics.darwinworld.Model.Util.Vector2d;
import agh.ics.darwinworld.Presenter.Statistics.MapStatistics;
import agh.ics.darwinworld.View.AnimalView;
import agh.ics.darwinworld.View.EmptyTileView;
import agh.ics.darwinworld.View.PlantView;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.MapChangeListener;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.WorldMap;
import agh.ics.darwinworld.Model.Records.WorldParameters;
import agh.ics.darwinworld.Model.WorldModel.Plant;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.HashSet;
import java.util.List;

public class SimulationPresenter implements MapChangeListener {
    private WorldParameters worldParameters;
    private WorldMap worldMap;
    private Simulation simulation;
    private boolean simulationRunning;
    private MapStatistics mapStatistics;

    @FXML
    private Label day;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Label animalNumber;
    @FXML
    public Label plantNumber;
    @FXML
    private Label emptyTiles;
    @FXML
    private Label averageEnergyLevel;
    @FXML
    private Label averageLifeLength;
    @FXML
    private Label averageKidsNumber;
    @FXML
    private Label firstPopularGenome;
    @FXML
    private Label secondPopularGenome;
    @FXML
    private Label thirdPopularGenome;
    @FXML
    private Button startStopSimulation;

    public void setWorldParameters(WorldParameters worldParameters){
        this.worldParameters = worldParameters;
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void setMapStatistics(MapStatistics mapStatistics){
        this.mapStatistics = mapStatistics;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public void setSimulationRunning(boolean simulationRunning){
        this.simulationRunning = simulationRunning;
    }

    public void fillLabels(MapStatistics mapStatistics){
        Platform.runLater(() -> {
            day.setText("Day " + mapStatistics.getDayCount());
            animalNumber.setText("Animals number: " + mapStatistics.getAnimalNumber());
            plantNumber.setText("Plants number: " + mapStatistics.getPlantNumber());
            emptyTiles.setText("Empty tiles number: " + mapStatistics.getEmptyTile());
            if (mapStatistics.getFirstPopularGenomeCount() != 0) {
                firstPopularGenome.setText(mapStatistics.getFirstPopularGenome() + ": " + mapStatistics.getFirstPopularGenomeCount() + " appearance");
            }
            else{
                firstPopularGenome.setText(mapStatistics.getFirstPopularGenome());
            }
            if (mapStatistics.getSecondPopularGenomeCount() != 0) {
                secondPopularGenome.setText(mapStatistics.getSecondPopularGenome() + ": " + mapStatistics.getSecondPopularGenomeCount() + " appearance");
            }
            else{
                secondPopularGenome.setText(mapStatistics.getSecondPopularGenome());
            }
            if (mapStatistics.getThirdPopularGenomeCount() != 0) {
                thirdPopularGenome.setText(mapStatistics.getThirdPopularGenome() + ": " + mapStatistics.getThirdPopularGenomeCount() + " appearance");
            }
            else{
                thirdPopularGenome.setText(mapStatistics.getThirdPopularGenome());
            }
            averageEnergyLevel.setText("Average energy level: " + mapStatistics.getAverageEnergyLevel());
            averageLifeLength.setText("Average life length of dead animals: " + mapStatistics.getAverageLifeLength());
            averageKidsNumber.setText("Average kids number: " + mapStatistics.getAverageKidsNumber());
        });
    }

    public synchronized void drawMap(){
        Platform.runLater(()->{
            mapGrid.getChildren().clear(); // Usuwanie starych elementów z siatki
            //tutaj renderuje mape na nowo

            HashSet<Vector2d> animalsPositions = new HashSet<>();
            HashSet<Vector2d> plantsPositions = new HashSet<>();
            List<Animal> animals = worldMap.getAnimals().values().stream().toList();
            List<Plant> plants = worldMap.getPlants().values().stream().toList();
            for (Animal currentAnimal : animals) {
                if (!animalsPositions.contains(currentAnimal.getPosition())) {
                    animalsPositions.add(currentAnimal.getPosition());
                }
            }
            for (Plant currentPlant : plants) {
                if (!plantsPositions.contains(currentPlant.getPosition())) {
                    plantsPositions.add(currentPlant.getPosition());
                }
            }

            for(int x = 0; x<worldMap.getWidth(); x++){
                for (int y = 0; y<worldMap.getHeight(); y++){
                    mapGrid.add(new EmptyTileView(),x,y);
                    ColumnConstraints columnConstraints = new ColumnConstraints();
                    columnConstraints.setHgrow(Priority.ALWAYS);
                    RowConstraints rowConstraints = new RowConstraints();
                    rowConstraints.setVgrow(Priority.ALWAYS);
                    mapGrid.getColumnConstraints().add(columnConstraints);
                    mapGrid.getRowConstraints().add(rowConstraints);
                }
            }

            for (int x = 0; x < worldMap.getWidth(); x++) {
                for (int y = 0; y < worldMap.getHeight(); y++) {
                    StackPane cell = new StackPane();

                    cell.getChildren().add(new EmptyTileView());

                    Vector2d position = new Vector2d(x, y);

                    if (animalsPositions.contains(position)) {
                        cell.getChildren().add(new AnimalView());
                    }
                    else if (plantsPositions.contains(position)){
                        cell.getChildren().add(new PlantView());
                    }
                    mapGrid.add(cell, x, y);
                }
            }
        });
    }

    public void startStopSimulation() {
        if (simulationRunning) {
            startStopSimulation.setText("Start simulation");
            startStopSimulation.getStyleClass().removeAll("stop-button");
            startStopSimulation.getStyleClass().add("bottom-button");
            System.out.println("Stopping simulation...");
            simulation.stop();
            simulationRunning = false;
        }
        else{
            startStopSimulation.setText("Resume simulation");
            startStopSimulation.getStyleClass().removeAll("bottom-button");
            startStopSimulation.getStyleClass().add("stop-button");
            System.out.println("Resuming simulation...");
            simulation.start();
            simulationRunning = true;
        }
    }

    @Override
    public void mapChanged(Simulation simulation, MapStatistics mapStatistics) {
        fillLabels(mapStatistics);
        drawMap();
        try{
            Thread.sleep(500);
        }catch(InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

}
