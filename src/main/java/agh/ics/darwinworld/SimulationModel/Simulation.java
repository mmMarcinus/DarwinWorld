package agh.ics.darwinworld.SimulationModel;

import agh.ics.darwinworld.AnimalModel.Animal;
import agh.ics.darwinworld.Util.Vector2d;
import agh.ics.darwinworld.WorldModel.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private List<Vector2d> positions;
    private List<Animal> animals;
    private WorldMap worldMap;

    public Simulation(List<Vector2d> positions, WorldMap worldMap) {
        this.positions = positions;
        this.worldMap = worldMap;
        this.animals = new ArrayList<Animal>();
        for (Vector2d position : positions) {
            Animal addedAnimal = new Animal(position);
            animals.add(addedAnimal);
            worldMap.place(addedAnimal);
        }
    }

    public List<Animal> getAnimals() {return animals;}

    public void run(){
        //tutaj animale będą się ruszać po kolei
    }
}
