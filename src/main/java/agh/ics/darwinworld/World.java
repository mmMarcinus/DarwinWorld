package agh.ics.darwinworld;

import agh.ics.darwinworld.SimulationModel.Simulation;
import agh.ics.darwinworld.WorldModel.NormalWorldMap;

public class World {
    public static void main(String[] args) {
        System.out.println("Start symulacji"+"\n");

        NormalWorldMap normalWorldMap = new NormalWorldMap(10,10);


        Simulation firstSimulation = new Simulation(3, 100, 5, 10, 10, 8,
                                                8, 16, 5, 2, 1,
                                                    4, false, false);

        firstSimulation.run();

        System.out.println("\n"+"Koniec symulacji");
    }
}
