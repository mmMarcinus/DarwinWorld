package agh.ics.darwinworld;

import agh.ics.darwinworld.SimulationModel.Simulation;
import agh.ics.darwinworld.WorldModel.NormalWorldMap;

public class World {
    public static void main(String[] args) {
        System.out.println("Start symulacji"+"\n");

        NormalWorldMap normalWorldMap = new NormalWorldMap(10,10);

        System.out.println("\n"+"Koniec symulacji");
    }
}
