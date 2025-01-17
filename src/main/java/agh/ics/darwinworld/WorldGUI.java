package agh.ics.darwinworld;

import agh.ics.darwinworld.Presenter.Menu.MapMenuApp;
import agh.ics.darwinworld.Presenter.Simulation.SimulationApp;
import javafx.application.Application;

public class WorldGUI {
    public static void main(String[] args) {
        Application.launch(SimulationApp.class, args);
    }
}
