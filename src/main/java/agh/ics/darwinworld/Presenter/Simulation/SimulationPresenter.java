package agh.ics.darwinworld.Presenter.Simulation;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Model.SimulationModel.Simulation;
import agh.ics.darwinworld.Model.Util.Vector2d;
import agh.ics.darwinworld.View.AnimalView;
import agh.ics.darwinworld.View.EmptyTileView;
import agh.ics.darwinworld.View.PlantView;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.MapChangeListener;
import agh.ics.darwinworld.Model.WorldModel.NormalWorldMap;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.WorldMap;
import agh.ics.darwinworld.Model.Records.WorldParameters;
import agh.ics.darwinworld.Model.WorldModel.Plant;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.HashSet;
import java.util.List;

public class SimulationPresenter implements MapChangeListener {
    private WorldParameters worldParameters;
    private WorldMap worldMap;
    private Simulation simulation;
    private boolean simulationRunning;

    @FXML
    private GridPane mapGrid;
    @FXML
    private Label is_polar_label;
    @FXML
    private Label is_change_label;
    @FXML
    public Label height_label;
    @FXML
    private Label width_label;
    @FXML
    private Label start_number_of_animals_label;
    @FXML
    private Label start_animal_energy_label;
    @FXML
    private Label energy_required_to_reproduce_label;
    @FXML
    private Label energy_taken_each_day_label;
    @FXML
    private Label genome_length_label;
    @FXML
    private Label min_mutation_number_label;
    @FXML
    private Label max_mutation_number_label;
    @FXML
    private Label plants_grown_each_day_label;
    @FXML
    private Label number_of_plants_label;
    @FXML
    private Label energy_from_plant_label;
    @FXML
    private Button startStopSimulation;

    public void setWorldParameters(WorldParameters worldParameters){
        this.worldParameters = worldParameters;
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public void setSimulationRunning(boolean simulationRunning){
        this.simulationRunning = simulationRunning;
    }

    public void fillLabels(){
        width_label.setText("Width: "+worldParameters.width());
        height_label.setText("Height: "+worldParameters.height());
        start_number_of_animals_label.setText("Animals start number: "+worldParameters.startAnimalsNumber());
        start_animal_energy_label.setText("Animal start energy: "+worldParameters.startEnergyLevel());
        energy_required_to_reproduce_label.setText("Min energy to reproduce: "+worldParameters.startEnergyLevel());
        energy_from_plant_label.setText("Energy from plant: "+worldParameters.energyFromPlant());
        energy_taken_each_day_label.setText("Energy taken per day: "+worldParameters.energyTakenEachDay());
        plants_grown_each_day_label.setText("Plants grown each day: "+worldParameters.dayPlantNumber());
        genome_length_label.setText("Genome length: " + worldParameters.genomesLength());
        max_mutation_number_label.setText("Max mutation number: " + worldParameters.maxMutation());
        min_mutation_number_label.setText("Min mutation number: " + worldParameters.minMutation());
        number_of_plants_label.setText("Plants start number: " + worldParameters.startPlantNumber());
        if(worldParameters.changeGenome()){
            is_change_label.setText("PRZEMIANKA");
        }else{
            is_change_label.setText("BEZ PRZEMIANKI");
        }
        if (worldParameters.polarMap()){
            is_polar_label.setText("BIEGUNY");
        }else{
            is_polar_label.setText("BEZ BIEGUNÓW");
        }
    }

    public synchronized void drawMap(){
        Platform.runLater(()->{
            mapGrid.getChildren().clear(); // Usuwanie starych elementów z siatki
            //tutaj renderuje mape na nowo

            HashSet<Vector2d> usedPositions = new HashSet<>();
            List<Animal> animals = worldMap.getAnimals().values().stream().toList();
            List<Plant> plants = worldMap.getPlants().values().stream().toList();
            for (Animal currentAnimal : animals) {
                if (!usedPositions.contains(currentAnimal.getPosition())) {
                    mapGrid.add(new AnimalView(), currentAnimal.getPosition().getX(), currentAnimal.getPosition().getY());
                    ColumnConstraints columnConstraints = new ColumnConstraints();
                    columnConstraints.setHgrow(Priority.ALWAYS);
                    RowConstraints rowConstraints = new RowConstraints();
                    rowConstraints.setVgrow(Priority.ALWAYS);
                    mapGrid.getColumnConstraints().add(columnConstraints);
                    mapGrid.getRowConstraints().add(rowConstraints);

                    usedPositions.add(currentAnimal.getPosition());
                }
            }
            for (Plant currentPlant : plants) {
                if (!usedPositions.contains(currentPlant.getPosition())) {
                    mapGrid.add(new PlantView(), currentPlant.getPosition().getX(), currentPlant.getPosition().getY());
                    ColumnConstraints columnConstraints = new ColumnConstraints();
                    columnConstraints.setHgrow(Priority.ALWAYS);
                    RowConstraints rowConstraints = new RowConstraints();
                    rowConstraints.setVgrow(Priority.ALWAYS);
                    mapGrid.getColumnConstraints().add(columnConstraints);
                    mapGrid.getRowConstraints().add(rowConstraints);

                    usedPositions.add(currentPlant.getPosition());
                }
            }
            for(int x = 0; x<worldMap.getWidth(); x++){
                for (int y = 0; y<worldMap.getHeight(); y++){
                    if(!usedPositions.contains(new Vector2d(x,y))){
                        mapGrid.add(new EmptyTileView(),x,y);
                        ColumnConstraints columnConstraints = new ColumnConstraints();
                        columnConstraints.setHgrow(Priority.ALWAYS);
                        RowConstraints rowConstraints = new RowConstraints();
                        rowConstraints.setVgrow(Priority.ALWAYS);
                        mapGrid.getColumnConstraints().add(columnConstraints);
                        mapGrid.getRowConstraints().add(rowConstraints);
                    }
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
    public void mapChanged(Simulation simulation) {
        drawMap();
        try{
            Thread.sleep(200);
        }catch(InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

}
