package agh.ics.darwinworld.Presenter.Menu;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Model.Exceptions.*;
import agh.ics.darwinworld.Model.Records.WorldParameters;
import agh.ics.darwinworld.Model.SimulationModel.Simulation;
import agh.ics.darwinworld.Presenter.Simulation.SimulationPresenter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.swing.*;
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
    public void initialize(){
        startSimulation.setOnAction(event -> {
            try {
                WorldParameters worldparameters = getWorldParameters();
                checkParameters(worldparameters);
                startNewSimulation(worldparameters);
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
                Integer.parseInt(parameterEnergyFromPlant.getText())
                );
    }

    private void checkParameters(WorldParameters worldParameters) throws Exception {

        //Mapa
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

        if (worldParameters.reproduceEnergyRequired() < worldParameters.startEnergyLevel() / 2) {
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
        simulation.getWorldMap().attachListener(simulationPresenter);
        //simulation.run();

        simulationPresenter.setWorldParameters(worldParameters);
        simulationPresenter.setWorldMap(simulation.getWorldMap());
        simulationPresenter.fillLabels();
        simulationPresenter.drawMap();

    }

}
