package agh.ics.darwinworld.Presenter.Simulation;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Model.SimulationModel.Simulation;
import agh.ics.darwinworld.Model.Util.Vector2d;
import agh.ics.darwinworld.Presenter.MapStatistics.MapStatistics;
import agh.ics.darwinworld.Presenter.MapStatistics.StatisticsToFile;
import agh.ics.darwinworld.View.Animal.*;
import agh.ics.darwinworld.View.EmptyTile.EmptyTileView;
import agh.ics.darwinworld.View.EmptyTile.HighlightedEmptyTileView;
import agh.ics.darwinworld.View.EmptyTile.PoleEmptyTileView;
import agh.ics.darwinworld.View.Plant.PlantView;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.MapChangeListener;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.WorldMap;
import agh.ics.darwinworld.Model.Records.WorldParameters;
import agh.ics.darwinworld.Model.WorldModel.Plant;
import agh.ics.darwinworld.View.Plant.PolePlantView;
import agh.ics.darwinworld.WorldGUI;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class SimulationPresenter implements MapChangeListener {
    private WorldParameters worldParameters;
    private WorldMap worldMap;
    private Simulation simulation;
    private boolean simulationRunning;
    private MapStatistics mapStatistics;
    private boolean animalHighlighted;
    private Animal higlightedAnimal;
    private boolean showPreferredAreas;
    private boolean showMostPopularGenotypes;
    private boolean isRendering = false;

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
    @FXML
    private Button areasPreferredByPlants;
    @FXML
    private Button mostPopularGenotypes;


    public void setWorldParameters(WorldParameters worldParameters){
        this.worldParameters = worldParameters;
    }

    public WorldParameters getWorldParameters(){return worldParameters;}

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
        if (isRendering) {
            return; // Ignoruj, jeśli poprzednie renderowanie jeszcze trwa
        }
        isRendering = true;
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
        isRendering=false;
    }

    private void addOrUpdateGenome(Map<String, Integer> map, String genome) {
        map.put(genome, map.getOrDefault(genome, 0) + 1);
    }

    public synchronized void drawMap(){
        if (isRendering) {
            return; // Ignoruj, jeśli poprzednie renderowanie jeszcze trwa
        }
        isRendering = true;
        Platform.runLater(()->{
            mapGrid.getChildren().clear(); // Usuwanie starych elementów z siatki
            //tutaj renderuje mape na nowo

            Map<Vector2d, ArrayList<Animal>> animals = worldMap.getAnimals();
            List<Plant> plants = worldMap.getPlants().values().stream().toList();

            //Najpopularniejsze genotypy
            Map<String, Integer> genomesWithCount = new HashMap<>();
            List<Animal> mostPopularAnimals = new ArrayList<>();

            HashSet<Vector2d> usedPositions = new HashSet<>();
            if (showMostPopularGenotypes){
                for(ArrayList<Animal> animalsOnPosition : animals.values()){
                    for(Animal currentAnimal : animalsOnPosition){
                        addOrUpdateGenome(genomesWithCount, currentAnimal.getGenome().getGenes());
                    }
                }
            }
            Optional<Map.Entry<String, Integer>> maxEntry = genomesWithCount.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue());
            maxEntry.ifPresent(entry -> {
                for(ArrayList<Animal> animalsOnPosition : animals.values()){
                    for(Animal currentAnimal : animalsOnPosition){
                        if (currentAnimal.getGenome().getGenes().equals(maxEntry.get().getKey())){
                            mostPopularAnimals.add(currentAnimal);
                        }
                    }
                }
            });

            int poleHeight = (int) (worldMap.getHeight() * 2 / 10);

            ArrayList<Animal> animalsToPut = new ArrayList<>();
            for (ArrayList<Animal> animalsOnPosition : animals.values()) {
                if (animalsOnPosition.isEmpty() || animalsOnPosition == null) {
                    continue;
                }

                if (higlightedAnimal!=null){
                    if (higlightedAnimal.getPosition().equals(animalsOnPosition.getFirst().getPosition())){
                        animalsToPut.add(higlightedAnimal);
                        continue;
                    }
                }

                Animal currentMaxEnergyAnimal = animalsOnPosition.getFirst();
                for (Animal currentAnimal : animalsOnPosition) {
                    if (currentAnimal.getEnergyLevel()>currentMaxEnergyAnimal.getEnergyLevel()){
                        currentMaxEnergyAnimal = currentAnimal;
                    }
                }
                animalsToPut.add(currentMaxEnergyAnimal);
            }
            for (Animal currentAnimal : animalsToPut) {
                if (!usedPositions.contains(currentAnimal.getPosition())) {
                    if(worldParameters.polarMap() && (currentAnimal.getPosition().getY() <= poleHeight - 1 || worldMap.getHeight() - poleHeight < currentAnimal.getPosition().getY() + 1)){
                        if(currentAnimal.isHighlighted()){
                            if (!mostPopularAnimals.contains(currentAnimal)) {
                                mapGrid.add(new PoleHighlightedAnimalView(this, currentAnimal), currentAnimal.getPosition().getX(), currentAnimal.getPosition().getY());
                            }else{
                                mapGrid.add(new PoleMostPopularGenomeAnimalView(this, currentAnimal), currentAnimal.getPosition().getX(), currentAnimal.getPosition().getY());
                            }
                        }else{
                            if (!mostPopularAnimals.contains(currentAnimal)){
                                mapGrid.add(new PoleAnimalView(this, currentAnimal), currentAnimal.getPosition().getX(), currentAnimal.getPosition().getY());
                            }
                            else{
                                mapGrid.add(new PoleMostPopularGenomeAnimalView(this, currentAnimal), currentAnimal.getPosition().getX(), currentAnimal.getPosition().getY());
                            }
                        }
                    }
                    else {
                        if (currentAnimal.isHighlighted()) {
                            if (!mostPopularAnimals.contains(currentAnimal)) {
                                mapGrid.add(new HighlightedAnimalView(this, currentAnimal), currentAnimal.getPosition().getX(), currentAnimal.getPosition().getY());
                            } else {
                                mapGrid.add(new MostPopularGenomeAnimalView(this, currentAnimal), currentAnimal.getPosition().getX(), currentAnimal.getPosition().getY());
                            }
                        } else {
                            if (!mostPopularAnimals.contains(currentAnimal)) {
                                mapGrid.add(new AnimalView(this, currentAnimal), currentAnimal.getPosition().getX(), currentAnimal.getPosition().getY());
                            } else {
                                mapGrid.add(new MostPopularGenomeAnimalView(this, currentAnimal), currentAnimal.getPosition().getX(), currentAnimal.getPosition().getY());
                            }
                        }
                    }
                    addTileConstraintsToMapGrid();
                    usedPositions.add(currentAnimal.getPosition());
                }
            }
            for (Plant currentPlant : plants) {
                if (!usedPositions.contains(currentPlant.getPosition())) {
                    if (worldParameters.polarMap()){
                        if (currentPlant.getPosition().getY() <= poleHeight - 1|| worldMap.getHeight() - poleHeight < currentPlant.getPosition().getY() + 1){
                            mapGrid.add(new PolePlantView(), currentPlant.getPosition().getX(), currentPlant.getPosition().getY());
                        }
                        else{
                            mapGrid.add(new PlantView(), currentPlant.getPosition().getX(), currentPlant.getPosition().getY());
                        }
                    }
                    else {
                        mapGrid.add(new PlantView(), currentPlant.getPosition().getX(), currentPlant.getPosition().getY());
                    }
                    addTileConstraintsToMapGrid();
                    usedPositions.add(currentPlant.getPosition());
                }
            }
            for(int x = 0; x<worldMap.getHeight(); x++){
                for (int y = 0; y<worldMap.getWidth(); y++){
                    if(!usedPositions.contains(new Vector2d(x,y))){
                        if (showPreferredAreas && worldMap.getJungleTop() > y && worldMap.getJungleBottom() <= y) {
                            mapGrid.add(new HighlightedEmptyTileView(), x, y);
                        }
                        else if (worldParameters.polarMap()){
                            if (y <= poleHeight - 1|| worldMap.getHeight() - poleHeight < y + 1){
                                mapGrid.add(new PoleEmptyTileView(), x, y);
                            }
                            else{
                                mapGrid.add(new EmptyTileView(), x, y);
                            }
                        }
                        else{
                            mapGrid.add(new EmptyTileView(), x, y);
                        }
                        addTileConstraintsToMapGrid();
                    }
                }
            }
        });
        isRendering = false;
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
            startStopSimulation.setText("Resume simulation");
            startStopSimulation.getStyleClass().removeAll("stop-button");
            startStopSimulation.getStyleClass().add("bottom-button");
            System.out.println("Stopping simulation...");
            simulation.stop();
            simulationRunning = false;
        }
        else{
            startStopSimulation.setText("Stop simulation");
            startStopSimulation.getStyleClass().removeAll("bottom-button");
            startStopSimulation.getStyleClass().add("stop-button");
            System.out.println("Resuming simulation...");
            simulation.start();
            simulationRunning = true;
            areasPreferredByPlants.setText("Show areas preferred by plants");
            showPreferredAreas = false;
        }
    }

    public void preferredAreas(){
        if (!simulationRunning && areasPreferredByPlants.getText().equals("Show areas preferred by plants")){
            areasPreferredByPlants.setText("Hide areas preferred by plants");
            showPreferredAreas = true;
            drawMap();
        }
        else{
            areasPreferredByPlants.setText("Show areas preferred by plants");
            showPreferredAreas = false;
            drawMap();
        }
    }

    public void popularGenotypes(){
        if (!simulationRunning && mostPopularGenotypes.getText().equals("Show animals with most popular genomes")){
            mostPopularGenotypes.setText("Hide animals with most popular genomes");
            showMostPopularGenotypes = true;
            drawMap();
        }
        else{
            mostPopularGenotypes.setText("Show animals with most popular genomes");
            showMostPopularGenotypes = false;
            drawMap();
        }
    }

    public void exportStatisticsToCsv(WorldMap worldMap, MapStatistics mapStatistics){
        System.out.println();
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
            returnButton.setStyle("-fx-padding: 0 40 0 40; -fx-background-color: green; -fx-font-size: 25");
            AnimalTitleLabel titleLabel = new AnimalTitleLabel();
            titleLabel.setText("ANIMAL STATS");
            AnimalStatLabel genomeLabel = new AnimalStatLabel();
            genomeLabel.setText("Genome: " + animal.getGenome().getGenes());
            AnimalStatLabel currentGeneLabel = new AnimalStatLabel();
            currentGeneLabel.setText("Current gene: " + animal.getCurrentGene());
            AnimalStatLabel energyLabel = new AnimalStatLabel();
            energyLabel.setText("Energy: " + animal.getEnergyLevel());
            AnimalStatLabel ageLabel = new AnimalStatLabel();
            ageLabel.setText("Animal age: " + animal.getAge());
            AnimalStatLabel childrenLabel = new AnimalStatLabel();
            childrenLabel.setText("Children number: " + animal.getKidsNumber());
            AnimalStatLabel plantsEatenLabel = new AnimalStatLabel();
            plantsEatenLabel.setText("Plants eaten: " + animal.getPlantsEaten());

            VBox animalStatsVBox = new VBox();
            animalStatsVBox.getChildren().addAll(returnButton, titleLabel, genomeLabel, currentGeneLabel, ageLabel, childrenLabel, energyLabel, plantsEatenLabel);
            animalStatsVBox.setStyle("-fx-background-color: #c5d9d0; -fx-alignment: center; -fx-min-height: 400");

            if(!left_stack_pane.getChildren().contains(animalStatsVBox)){
                left_stack_pane.getChildren().add(animalStatsVBox);
            }

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
            returnButton.setStyle("-fx-padding: 0 40 0 40; -fx-background-color: green; -fx-font-size: 25");
            AnimalTitleLabel titleLabel = new AnimalTitleLabel();
            titleLabel.setText("ANIMAL STATS");
            AnimalStatLabel genomeLabel = new AnimalStatLabel();
            genomeLabel.setText("Genome: " + animal.getGenome().getGenes());
            AnimalStatLabel currentGeneLabel = new AnimalStatLabel();
            currentGeneLabel.setText("Current gene: " + animal.getCurrentGene());
            AnimalStatLabel energyLabel = new AnimalStatLabel();
            energyLabel.setText("Energy: " + animal.getEnergyLevel());
            AnimalStatLabel ageLabel = new AnimalStatLabel();
            if (animal.getEnergyLevel()<=0){
                ageLabel.setText("Died at age: " + animal.getAge());
            }
            else {
                ageLabel.setText("Animal age: " + animal.getAge());
            }
            AnimalStatLabel childrenLabel = new AnimalStatLabel();
            childrenLabel.setText("Children number: " + animal.getKidsNumber());
            AnimalStatLabel allDescendantsNumberLabel = new AnimalStatLabel();
            allDescendantsNumberLabel.setText("All descendants number: " + animal.getFamilyNumber());
            AnimalStatLabel plantsEatenLabel = new AnimalStatLabel();
            plantsEatenLabel.setText("Plants eaten: " + animal.getPlantsEaten());

            ObservableList<Node> leftStackPaneChildren = left_stack_pane.getChildren();
            VBox animalStatsVBoxAux = (VBox) leftStackPaneChildren.getLast();
            animalStatsVBoxAux.getChildren().clear();
            animalStatsVBoxAux.getChildren().addAll(returnButton, titleLabel, genomeLabel, currentGeneLabel, energyLabel,ageLabel, childrenLabel, allDescendantsNumberLabel, plantsEatenLabel);

            returnButton.setOnAction(event -> {
                Platform.runLater(()->{
                    left_stack_pane.getChildren().remove(animalStatsVBoxAux);
                    unhighlightAnimal(animal);
                });
            });
        });
    }

    public void highlightAnimal(Animal animal){
        if(animalHighlighted){
            return;
        }
        animal.highlight();
        higlightedAnimal = animal;
        animalHighlighted=true;
        drawAnimalStats(animal);
        drawMap();
    }

    public void unhighlightAnimal(Animal animal){
        animal.unhighlight();
        animalHighlighted=false;
        drawMap();
    }

    public boolean isAnimalHighlighted(){return animalHighlighted;}

    @Override
    public void mapChanged(MapStatistics statistics) {
        fillLabels(statistics);
        if(animalHighlighted){
            fillAnimalStats(higlightedAnimal);
        }
        drawMap();
        if (worldParameters.exportStatistics()){
            exportStatisticsToCsv(worldMap, mapStatistics);
        }
    }

}
