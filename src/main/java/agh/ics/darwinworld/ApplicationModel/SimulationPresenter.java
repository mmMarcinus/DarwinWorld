package agh.ics.darwinworld.ApplicationModel;

import agh.ics.darwinworld.AnimalModel.Animal;
import agh.ics.darwinworld.Util.Vector2d;
import agh.ics.darwinworld.View.AnimalView;
import agh.ics.darwinworld.View.EmptyTileView;
import agh.ics.darwinworld.View.PlantView;
import agh.ics.darwinworld.WorldModel.Abstracts.MapChangeListener;
import agh.ics.darwinworld.WorldModel.NormalWorldMap;
import agh.ics.darwinworld.WorldModel.Abstracts.WorldMap;
import agh.ics.darwinworld.Records.WorldParameters;
import agh.ics.darwinworld.WorldModel.Plant;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.HashSet;
import java.util.List;

public class SimulationPresenter implements MapChangeListener {
    private WorldParameters worldParameters;
    private WorldMap worldMap;

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

    public void setWorldParameters(WorldParameters worldParameters){
        this.worldParameters = worldParameters;
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @FXML
    public void initialize(){
        worldMap = new NormalWorldMap(10,10);
        setWorldMap(worldMap);
        fillLabels();
        drawMap();
    }

    public void fillLabels(){
        //TUTAJ DZIALA TYLKO NIE MA DANYCH W WORLDPRAAMETERS
//        width_label.setText("width: "+worldParameters.width());
//        height_label.setText("height: "+worldParameters.height());
//        start_number_of_animals_label.setText("animals start number: "+worldParameters.startAnimalsNumber());
//        start_animal_energy_label.setText("animal start energy:"+worldParameters.startEnergyLevel());
//        energy_required_to_reproduce_label.setText("min energy to reproduce: "+worldParameters.startEnergyLevel());
//        energy_from_plant_label.setText("energy from plant: "+worldParameters.energyFromPlant());
//        energy_taken_each_day_label.setText("energy taken per day: "+worldParameters.energyTakenEachDay());
//        plants_grown_each_day_label.setText("plants grown each day: "+worldParameters.dayPlantNumber());
//        genome_length_label.setText("genome length: " + worldParameters.genomesLength());
//        max_mutation_number_label.setText("max mutation number: " + worldParameters.maxMutation());
//        min_mutation_number_label.setText("min mutation number: " + worldParameters.minMutation());
    }

    public void drawMap(){
        mapGrid.getChildren().clear(); // Usuwanie starych elementów z siatki
        System.out.println(worldMap.getAnimals().size());
        //tutaj renderuje mape na nowo

        HashSet<Vector2d> usedPositions = new HashSet<>();
        List<Animal> animals = worldMap.getAnimals().values().stream().toList();
        List<Plant> plants = worldMap.getPlants().values().stream().toList();
        for (Animal currentAnimal : animals) {
            if (!usedPositions.contains(currentAnimal.getPosition())) {
                mapGrid.add(new AnimalView(), currentAnimal.getPosition().getX(), currentAnimal.getPosition().getY());
                usedPositions.add(currentAnimal.getPosition());
            }
        }
        for (Plant currentPlant : plants) {
            if (!usedPositions.contains(currentPlant.getPosition())) {
                mapGrid.add(new PlantView(), currentPlant.getPosition().getX(), currentPlant.getPosition().getY());
                usedPositions.add(currentPlant.getPosition());
            }
        }
        for(int x = 0; x<worldMap.getHeight(); x++){
            for (int y = 0; y<worldMap.getWidth(); y++){
                if(!usedPositions.contains(new Vector2d(x,y))){
                    mapGrid.add(new EmptyTileView(),x,y);
                }
            }
        }
    }

    @Override
    public void mapChanged() {

    }

}
