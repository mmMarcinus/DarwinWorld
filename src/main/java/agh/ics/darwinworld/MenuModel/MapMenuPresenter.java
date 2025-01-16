package agh.ics.darwinworld.MenuModel;

import agh.ics.darwinworld.ApplicationModel.SimulationPresenter;
import agh.ics.darwinworld.SimulationModel.Simulation;
import agh.ics.darwinworld.Records.WorldParameters;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
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
                //validateStartParameters(worldparameters);
                startNewSimulation(worldparameters);
            } catch (Exception e) {
                System.out.println("Error");
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
        FXMLLoader loader = new FXMLLoader();
        Stage stage = new Stage();

        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        GridPane view = loader.load();
        SimulationPresenter presenter = loader.getController();
        presenter.setWorldParameters(worldParameters);
        //presenter.prepare();

        Simulation simulation = new Simulation(worldParameters, presenter);
//        ExtendedThread thread = new ExtendedThread(simulation);
//        thread.start();
//
//        stage.setOnCloseRequest(event -> {
//            simulation.stopRunning();
//        });

//        presenter.setThread(thread);

        stage.setTitle("Darwin World");
        stage.setScene(new Scene(view));
        stage.setMaximized(true);
        stage.show();
    }

}
