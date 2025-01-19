package agh.ics.darwinworld.Presenter.Simulation;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Model.SimulationModel.Simulation;
import agh.ics.darwinworld.Model.Util.Vector2d;
import agh.ics.darwinworld.Presenter.MapStatistics.MapStatistics;
import agh.ics.darwinworld.Presenter.MapStatistics.StatisticsToFile;
import agh.ics.darwinworld.View.Animal.AnimalStatLabel;
import agh.ics.darwinworld.View.Animal.AnimalTitleLabel;
import agh.ics.darwinworld.View.Animal.AnimalView;
import agh.ics.darwinworld.View.Animal.HighlightedAnimalView;
import agh.ics.darwinworld.View.EmptyTileView;
import agh.ics.darwinworld.View.PlantView;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.MapChangeListener;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.WorldMap;
import agh.ics.darwinworld.Model.Records.WorldParameters;
import agh.ics.darwinworld.Model.WorldModel.Plant;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.util.ArrayList;
import java.util.Set;


public class SimulationPresenter implements MapChangeListener {
    private WorldParameters worldParameters;
    private WorldMap worldMap;
    private Simulation simulation;
    private boolean simulationRunning;
    private MapStatistics mapStatistics;
    private boolean animalHighlighted;
    private Animal higlightedAnimal;

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
    @FXML
    private StackPane left_stack_pane;


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
            System.out.println(worldMap.getAnimals().size());
            //tutaj renderuje mape na nowo

            HashSet<Vector2d> usedPositions = new HashSet<>();
            List<Animal> animals = worldMap.getAnimals().values().stream().toList();
            List<Plant> plants = worldMap.getPlants().values().stream().toList();
            for (Animal currentAnimal : animals) {
                if (!usedPositions.contains(currentAnimal.getPosition())) {
                    if(currentAnimal.isHighlighted()){
                        mapGrid.add(new HighlightedAnimalView(this, currentAnimal), currentAnimal.getPosition().getX(), currentAnimal.getPosition().getY());
                    }else{
                        mapGrid.add(new AnimalView(this, currentAnimal), currentAnimal.getPosition().getX(), currentAnimal.getPosition().getY());
                    }
                    addTileConstraintsToMapGrid();
                    usedPositions.add(currentAnimal.getPosition());
                }
            }
            for (Plant currentPlant : plants) {
                if (!usedPositions.contains(currentPlant.getPosition())) {
                    mapGrid.add(new PlantView(), currentPlant.getPosition().getX(), currentPlant.getPosition().getY());
                    addTileConstraintsToMapGrid();
                    usedPositions.add(currentPlant.getPosition());
                }
            }
            for(int x = 0; x<worldMap.getHeight(); x++){
                for (int y = 0; y<worldMap.getWidth(); y++){
                    if(!usedPositions.contains(new Vector2d(x,y))){
                        mapGrid.add(new EmptyTileView(),x,y);
                        addTileConstraintsToMapGrid();
                    }
                }
            }
        });
    }

    private void addTileConstraintsToMapGrid(){
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS);
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS);
        mapGrid.getColumnConstraints().add(columnConstraints);
        mapGrid.getRowConstraints().add(rowConstraints);
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


    public void exportStatisticsToCsv(WorldMap worldMap, MapStatistics mapStatistics){
        String projectPath = System.getProperty("user.dir");
        String filename = "World_Statistics_" + worldMap.getMapID() + ".csv";
        String filePath = projectPath + "/src/main/resources/statistics/" + filename;

        File csvFile = new File(filePath);
        boolean fileExist = csvFile.exists();

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            if (!fileExist) {
                StatisticsToFile.setHeader(writer);
            }

            StatisticsToFile.fillStatisticsDay(writer, worldMap.getMapID(), mapStatistics);

        } catch (Exception e) {
            System.out.println("Saving statistics to csv file error");
        }
    }

    public synchronized void drawAnimalStats(Animal animal){
        Platform.runLater(()->{
            Button returnButton = new Button();
            returnButton.setText("Return");
            returnButton.setStyle("-fx-padding: 0 40 0 40; -fx-background-color: green;");
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

            VBox animalStatsVBox = new VBox();
            animalStatsVBox.getChildren().addAll(returnButton, titleLabel, ageLabel, childrenLabel, energyLabel, genomeLabel);
            animalStatsVBox.setStyle("-fx-background-color: #c5d9d0; -fx-alignment: center; -fx-min-height: 400");

            left_stack_pane.setAlignment(Pos.CENTER);

            left_stack_pane.getChildren().add(animalStatsVBox);

            returnButton.setOnAction(event -> {
                Platform.runLater(()->{
                    left_stack_pane.getChildren().remove(animalStatsVBox);
                    unhighlightAnimal(animal);
                });
            });
        });
    }

    public synchronized void fillAnimalStats(Animal animal){
        Platform.runLater(()->{
            Button returnButton = new Button();
            returnButton.setText("Return");
            returnButton.setStyle("-fx-padding: 0 40 0 40; -fx-background-color: green;");
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

            ObservableList<Node> leftStackPaneChildren = left_stack_pane.getChildren();
            VBox animalStatsVBoxAux = (VBox) leftStackPaneChildren.getLast();
            animalStatsVBoxAux.getChildren().setAll(returnButton, titleLabel, ageLabel, childrenLabel, energyLabel, genomeLabel);

            returnButton.setOnAction(event -> {
                Platform.runLater(()->{
                    left_stack_pane.getChildren().remove(animalStatsVBoxAux);
                    unhighlightAnimal(animal);
                });
            });
        });
    }

    public void highlightAnimal(Animal animal){
        animal.highlight();
        higlightedAnimal = animal;
        animalHighlighted=true;
        drawAnimalStats(animal);
    }

    public void unhighlightAnimal(Animal animal){
        animal.unhighlight();
        animalHighlighted=false;
    }

    @Override
    public void mapChanged(MapStatistics statistics) {
        if(animalHighlighted){
            fillAnimalStats(higlightedAnimal);
        }else{
            fillLabels(statistics);
        }
        drawMap();
        try{
            Thread.sleep(500);
        }catch(InterruptedException e) {
            System.out.println(e.getMessage());
        }
        if (worldParameters.exportStatistics()){
            exportStatisticsToCsv(worldMap, mapStatistics);
        }
    }

}
