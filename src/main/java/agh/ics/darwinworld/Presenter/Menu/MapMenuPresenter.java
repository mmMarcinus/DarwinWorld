package agh.ics.darwinworld.Presenter.Menu;

import agh.ics.darwinworld.Model.SimulationModel.Simulation;
import agh.ics.darwinworld.Model.Records.WorldParameters;
import agh.ics.darwinworld.Presenter.Simulation.SimulationPresenter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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
                //validateStartParameters(worldparameters);t
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

    private void startNewSimulation(WorldParameters worldParameters) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Stage stage = new Stage();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = fxmlLoader.load();

        var scene = new Scene(viewRoot);

        stage.show();

        String css = getClass().getClassLoader().getResource("simulation.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.setTitle("Simulation");
        stage.minWidthProperty().bind(viewRoot.minWidthProperty());
        stage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

}
