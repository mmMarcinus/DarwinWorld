package agh.ics.darwinworld.SimulationModel;

import agh.ics.darwinworld.AnimalModel.Animal;
import agh.ics.darwinworld.Util.Vector2d;
import agh.ics.darwinworld.WorldModel.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private List<Vector2d> positions;
    public static List<Animal> animals;
    public static WorldMap worldMap;
    private List<String> genomes;
    private int startEnergyLevel;


    public Simulation(List<Vector2d> positions, WorldMap worldMap, List<String> genomes, int startEnergyLevel) {
        this.positions = positions;
        this.worldMap = worldMap;
        this.animals = new ArrayList<Animal>();
        int i = 0;
        for (Vector2d position : positions) {
            Animal addedAnimal = new Animal(position, genomes.get(i), startEnergyLevel);
            animals.add(addedAnimal);
            worldMap.place(addedAnimal);
            i++;
        }
    }

    public List<Animal> getAnimals() {return animals;}

    public void run(){
        //tutaj animale będą się ruszać po kolei
    }
}
