package agh.ics.darwinworld.Presenter.Menu;

import agh.ics.darwinworld.Model.Exceptions.*;
import agh.ics.darwinworld.Model.Records.WorldParameters;
import agh.ics.darwinworld.Model.SimulationModel.Simulation;
import agh.ics.darwinworld.Presenter.MapStatistics.StatisticsToFile;
import agh.ics.darwinworld.Presenter.Simulation.SimulationPresenter;
import agh.ics.darwinworld.Presenter.MapStatistics.MapStatistics;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapMenuPresenter {

    @FXML
    private TextField parameterMapHeight;

    @FXML
    private TextField parameterMapWidth;

    @FXML
    private CheckBox parameterPolarMap;

    @FXML
    private TextField parameterStartAnimalsNumber;

    @FXML
    private TextField parameterStartEnergyLevel;

    @FXML
    private TextField parameterEnergyTakenEachDay;

    @FXML
    private TextField parameterReproduceRequiredEnergy;

    @FXML
    private TextField parameterGenomesLength;

    @FXML
    private TextField parameterMinMutation;

    @FXML
    private TextField parameterMaxMutation;

    @FXML
    private CheckBox parameterChangeGenome;

    @FXML
    private TextField parameterStartPlantsNumber;

    @FXML
    private TextField parameterDayPlantNumber;

    @FXML
    private TextField parameterEnergyFromPlant;

    @FXML
    private Button startSimulation;

    @FXML
    private Button importParameters;

    @FXML
    private Button exportParameters;

    @FXML
    private CheckBox exportStatistics;


    @FXML
    public void initialize() {
        startSimulation.setOnAction(event -> {
            try {
                WorldParameters worldparameters = getWorldParameters();
                checkParameters(worldparameters);
                startNewSimulation(worldparameters);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        importParameters.setOnAction(event -> {
            try {
                importMapParameters();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        exportParameters.setOnAction(event -> {
            try {
                exportMapParameters();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }


    private WorldParameters getWorldParameters() {
        return new WorldParameters(
                Integer.parseInt(parameterMapHeight.getText()),
                Integer.parseInt(parameterMapWidth.getText()),
                parameterPolarMap.isSelected(),
                Integer.parseInt(parameterStartAnimalsNumber.getText()),
                Integer.parseInt(parameterStartEnergyLevel.getText()),
                Integer.parseInt(parameterEnergyTakenEachDay.getText()),
                Integer.parseInt(parameterReproduceRequiredEnergy.getText()),
                Integer.parseInt(parameterGenomesLength.getText()),
                Integer.parseInt(parameterMinMutation.getText()),
                Integer.parseInt(parameterMaxMutation.getText()),
                parameterChangeGenome.isSelected(),
                Integer.parseInt(parameterStartPlantsNumber.getText()),
                Integer.parseInt(parameterDayPlantNumber.getText()),
                Integer.parseInt(parameterEnergyFromPlant.getText()),
                exportStatistics.isSelected()
        );
    }

    private void checkParameters(WorldParameters worldParameters) throws Exception {

        //Map
        if (worldParameters.width() <= 0 || worldParameters.width() > 100) {
            throw new MapWidthException();
        }

        if (worldParameters.height() <= 0 || worldParameters.height() > 100) {
            throw new MapHeightException();
        }


        //Animals
        if (worldParameters.startAnimalsNumber() <= 0 || worldParameters.startAnimalsNumber() > worldParameters.height() * worldParameters.width()) {
            throw new AnimalStartNumberException();
        }

        if (worldParameters.startEnergyLevel() <= 0) {
            throw new StartEnergyLevelException();
        }

        if (worldParameters.energyTakenEachDay() < 0) {
            throw new EnergyTakenEachDayException();
        }

        if (worldParameters.reproduceEnergyRequired() <= 0) {
            throw new ReproduceEnergyRequiredException();
        }

        if (worldParameters.genomesLength() <= 0) {
            throw new GenomesLengthException();
        }

        if (worldParameters.minMutation() < 0 || worldParameters.minMutation() > worldParameters.maxMutation() || worldParameters.maxMutation() > worldParameters.genomesLength()) {
            throw new MutationException();
        }


        //Plant
        if (worldParameters.startPlantNumber() <= 0 || worldParameters.startPlantNumber() > worldParameters.height() * worldParameters.width()) {
            throw new StartPlantNumberException();
        }

        if (worldParameters.dayPlantNumber() <= 0 || worldParameters.dayPlantNumber() > worldParameters.height() * worldParameters.width()) {
            throw new dayPlantNumberException();
        }

        if (worldParameters.energyFromPlant() <= 0) {
            throw new EnergyFromPlantException();
        }
    }

    private void startNewSimulation(WorldParameters worldParameters) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Stage stage = new Stage();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = fxmlLoader.load();
        var scene = new Scene(viewRoot);

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Icon.png")));
        stage.getIcons().add(icon);

        SimulationPresenter simulationPresenter = fxmlLoader.getController();

        stage.show();

        String css = getClass().getClassLoader().getResource("simulation.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.setTitle("Simulation");
        stage.setResizable(false);
        stage.minWidthProperty().bind(viewRoot.minWidthProperty());
        stage.minHeightProperty().bind(viewRoot.minHeightProperty());

        Simulation simulation = new Simulation(worldParameters);
        simulationPresenter.setWorldParameters(worldParameters);
        simulationPresenter.setWorldMap(simulation.getWorldMap());
        simulationPresenter.setMapStatistics(new MapStatistics());
        simulationPresenter.setSimulation(simulation);
        simulationPresenter.setSimulationRunning(true);
        simulationPresenter.drawMap();

        simulation.getWorldMap().attachListener(simulationPresenter);

        Thread simulationThread = new Thread(simulation);
        simulationThread.start();
        stage.setOnCloseRequest(event -> {
            simulation.close();
        });
    }

    private void exportMapParameters() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File resourcesDirectory = new File("src/main/resources/parameters/");
        fileChooser.setInitialDirectory(resourcesDirectory);

        Stage stage = (Stage) exportParameters.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            PrintWriter writer = new PrintWriter(new FileWriter(file));

            writer.println("MapHeight:" + parameterMapHeight.getText());
            writer.println("MapWidth:" + parameterMapWidth.getText());
            writer.println("StartAnimalsNumber:" + parameterStartAnimalsNumber.getText());
            writer.println("StartEnergyLevel:" + parameterStartEnergyLevel.getText());
            writer.println("EnergyTakenEachDay:" + parameterEnergyTakenEachDay.getText());
            writer.println("EnergyRequiredToReproduce:" + parameterReproduceRequiredEnergy.getText());
            writer.println("GenomesLength:" + parameterGenomesLength.getText());
            writer.println("MinMutations:" + parameterMinMutation.getText());
            writer.println("MaxMutations:" + parameterMaxMutation.getText());
            writer.println("StartPlantsNumber:" + parameterStartPlantsNumber.getText());
            writer.println("DayPlantNumber:" + parameterDayPlantNumber.getText());
            writer.println("EnergyFromPlant:" + parameterEnergyFromPlant.getText());
            writer.println("PolarMap:" + parameterPolarMap.isSelected());
            writer.println("ChangeGenome:" + parameterChangeGenome.isSelected());
            writer.println("ExportStatistics:" + exportStatistics.isSelected());
            writer.close();
        }
    }

    private void importMapParameters() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File resourcesDirectory = new File("src/main/resources/parameters/");
        fileChooser.setInitialDirectory(resourcesDirectory);

        Stage stage = (Stage) importParameters.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        HashMap<String, String> importedMapParameters = new HashMap<>();

        if (file != null) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split(":");
                if (args.length == 2) {
                    String key = args[0].trim();
                    String value = args[1].trim();

                    importedMapParameters.put(key, value);
                }
            }

            fillFormWorldConfigParams(importedMapParameters);
        }

    }
    private void fillFormWorldConfigParams(HashMap<String, String> importedMapParameters){
        importedMapParameters.forEach((key, value) -> {
            switch (key) {
                case "MapHeight" -> parameterMapHeight.setText(value);
                case "MapWidth" -> parameterMapWidth.setText(value);
                case "StartAnimalsNumber" -> parameterStartAnimalsNumber.setText(value);
                case "StartEnergyLevel" -> parameterStartEnergyLevel.setText(value);
                case "EnergyTakenEachDay" -> parameterEnergyTakenEachDay.setText(value);
                case "EnergyRequiredToReproduce" -> parameterReproduceRequiredEnergy.setText(value);
                case "GenomesLength" -> parameterGenomesLength.setText(value);
                case "MinMutations" -> parameterMinMutation.setText(value);
                case "MaxMutations" -> parameterMaxMutation.setText(value);
                case "StartPlantsNumber" -> parameterStartPlantsNumber.setText(value);
                case "DayPlantNumber" -> parameterDayPlantNumber.setText(value);
                case "EnergyFromPlant" -> parameterEnergyFromPlant.setText(value);
                case "PolarMap" -> parameterPolarMap.setSelected(Boolean.parseBoolean(value));
                case "ChangeGenome" -> parameterChangeGenome.setSelected(Boolean.parseBoolean(value));
                case "ExportStatistics" -> exportStatistics.setSelected(Boolean.parseBoolean(value));
            }
        });
        refreshView();
    }

    private void refreshView() {
        parameterMapHeight.requestLayout();
        parameterMapWidth.requestLayout();
        parameterStartAnimalsNumber.requestLayout();
        parameterStartEnergyLevel.requestLayout();
        parameterEnergyTakenEachDay.requestLayout();
        parameterReproduceRequiredEnergy.requestLayout();
        parameterGenomesLength.requestLayout();
        parameterMinMutation.requestLayout();
        parameterMaxMutation.requestLayout();
        parameterStartPlantsNumber.requestLayout();
        parameterDayPlantNumber.requestLayout();
        parameterEnergyFromPlant.requestLayout();
        parameterPolarMap.requestLayout();
        parameterChangeGenome.requestLayout();
        exportStatistics.requestLayout();
    }
}
