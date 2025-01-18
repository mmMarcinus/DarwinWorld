package agh.ics.darwinworld.Presenter.Simulation;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Model.Util.Vector2d;
import agh.ics.darwinworld.View.*;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.MapChangeListener;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.WorldMap;
import agh.ics.darwinworld.Model.Records.WorldParameters;
import agh.ics.darwinworld.Model.WorldModel.Plant;
import agh.ics.darwinworld.View.Animal.AnimalStatLabel;
import agh.ics.darwinworld.View.Animal.AnimalTitleLabel;
import agh.ics.darwinworld.View.Animal.AnimalView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

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
    @FXML
    private VBox left_vbox;

    public void setWorldParameters(WorldParameters worldParameters){
        this.worldParameters = worldParameters;
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void fillLabels(){
        width_label.setText("width: "+worldParameters.width());
        height_label.setText("height: "+worldParameters.height());
        start_number_of_animals_label.setText("animals start number: "+worldParameters.startAnimalsNumber());
        start_animal_energy_label.setText("animal start energy:"+worldParameters.startEnergyLevel());
        energy_required_to_reproduce_label.setText("min energy to reproduce: "+worldParameters.startEnergyLevel());
        energy_from_plant_label.setText("energy from plant: "+worldParameters.energyFromPlant());
        energy_taken_each_day_label.setText("energy taken per day: "+worldParameters.energyTakenEachDay());
        plants_grown_each_day_label.setText("plants grown each day: "+worldParameters.dayPlantNumber());
        genome_length_label.setText("genome length: " + worldParameters.genomesLength());
        max_mutation_number_label.setText("max mutation number: " + worldParameters.maxMutation());
        min_mutation_number_label.setText("min mutation number: " + worldParameters.minMutation());
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
            System.out.println(worldMap.getAnimals().size());
            //tutaj renderuje mape na nowo

            HashSet<Vector2d> usedPositions = new HashSet<>();
            List<Animal> animals = worldMap.getAnimals().values().stream().toList();
            List<Plant> plants = worldMap.getPlants().values().stream().toList();
            for (Animal currentAnimal : animals) {
                if (!usedPositions.contains(currentAnimal.getPosition())) {
                    mapGrid.add(new AnimalView(this, currentAnimal), currentAnimal.getPosition().getX(), currentAnimal.getPosition().getY());
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
            for(int x = 0; x<worldMap.getHeight(); x++){
                for (int y = 0; y<worldMap.getWidth(); y++){
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

    public void drawAnimalStats(Animal animal){
        Platform.runLater(()->{
            Button returnButton = new Button();
            returnButton.setText("Return");
            returnButton.setStyle("-fx-padding: 0 40 0 40; -fx-background-color: green;");
            returnButton.setOnAction(event -> {
                Label isPolarLabel = new Label("BIEGUNY");
                isPolarLabel.getStyleClass().add("title-label");
                Label isChangeLabel = new Label("ZAMIANA");
                isChangeLabel.getStyleClass().add("title-label");
                Label mapCategoryLabel = new Label("MAP:");
                mapCategoryLabel.getStyleClass().add("parameter_category-label");
                Label heightLabel = new Label("height: " + worldParameters.height());
                heightLabel.getStyleClass().add("parameter-label");
                Label widthLabel = new Label("width: " + worldParameters.width());
                widthLabel.getStyleClass().add("parameter-label");
                Label animalsCategoryLabel = new Label("ANIMALS:");
                animalsCategoryLabel.getStyleClass().add("parameter_category-label");
                Label startNumberOfAnimalsLabel = new Label("start animals number: " + worldParameters.startAnimalsNumber());
                startNumberOfAnimalsLabel.getStyleClass().add("parameter-label");
                Label startAnimalEnergyLabel = new Label("start energy: " + worldParameters.startEnergyLevel());
                startAnimalEnergyLabel.getStyleClass().add("parameter-label");
                Label energyTakenEachDayLabel = new Label("energy taken each day: " + worldParameters.energyTakenEachDay());
                energyTakenEachDayLabel.getStyleClass().add("parameter-label");
                Label energyRequiredToReproduceLabel = new Label("energy required to reproduce: " + worldParameters.reproduceEnergyRequired());
                energyRequiredToReproduceLabel.getStyleClass().add("parameter-label");
                Label genomeLengthLabel = new Label("genome length: " + worldParameters.genomesLength());
                genomeLengthLabel.getStyleClass().add("parameter-label");
                Label minMutationNumberLabel = new Label("min mutation number: " + worldParameters.minMutation());
                minMutationNumberLabel.getStyleClass().add("parameter-label");
                Label maxMutationNumberLabel = new Label("max mutation number: " + worldParameters.maxMutation());
                maxMutationNumberLabel.getStyleClass().add("parameter-label");
                Label plantsCategoryLabel = new Label("PLANTS:");
                plantsCategoryLabel.getStyleClass().add("parameter_category-label");
                Label numberOfPlantsLabel = new Label("plants number: " + worldParameters.startPlantNumber());
                numberOfPlantsLabel.getStyleClass().add("parameter-label");
                Label plantsGrownEachDayLabel = new Label("plants each day: " + worldParameters.dayPlantNumber());
                plantsGrownEachDayLabel.getStyleClass().add("parameter-label");
                Label energyFromPlantLabel = new Label("energy from plant: " + worldParameters.energyFromPlant());
                energyFromPlantLabel.getStyleClass().add("parameter-label");
                left_vbox.getChildren().setAll(isPolarLabel, isChangeLabel, mapCategoryLabel, heightLabel, widthLabel, animalsCategoryLabel, startNumberOfAnimalsLabel, startAnimalEnergyLabel, energyTakenEachDayLabel, energyRequiredToReproduceLabel, genomeLengthLabel, minMutationNumberLabel, maxMutationNumberLabel, plantsCategoryLabel, numberOfPlantsLabel);
            });
            AnimalTitleLabel titleLabel = new AnimalTitleLabel();
            titleLabel.setText("ANIMAL STATS");
            AnimalStatLabel ageLabel = new AnimalStatLabel();
            ageLabel.setText("Animal age: " + animal.getAge());
            AnimalStatLabel childrenLabel = new AnimalStatLabel();
            childrenLabel.setText("Children number: " + animal.getKidsNumber());
            AnimalStatLabel energyLabel = new AnimalStatLabel();
            energyLabel.setText("Energy: " + animal.getEnergyLevel());
            AnimalStatLabel genomeLabel = new AnimalStatLabel();
            genomeLabel.setText("Genome: " + animal.getGenome().getGenes());
            AnimalStatLabel currentGeneLabel = new AnimalStatLabel();
            currentGeneLabel.setText("Current gene: " + animal.getCurrentGene());
            left_vbox.setAlignment(Pos.CENTER);
            left_vbox.getChildren().setAll(returnButton, titleLabel, ageLabel, childrenLabel, energyLabel, genomeLabel, currentGeneLabel);
        });
    }

    @Override
    public void mapChanged() {
        drawMap();
        try{
            Thread.sleep(1300);
        }catch(InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

}
