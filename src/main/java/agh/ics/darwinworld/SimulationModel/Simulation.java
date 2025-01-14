package agh.ics.darwinworld.SimulationModel;

import agh.ics.darwinworld.AnimalModel.Animal;
import agh.ics.darwinworld.Util.Vector2d;
import agh.ics.darwinworld.WorldModel.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation implements Runnable {
    private int animalsNumber;
    public static List<Animal> animals;
    public static WorldMap worldMap;

    private int genomesLength;
    private int startEnergyLevel;



    public Simulation(int animalsNumber, int width, int height, int startEnergyLevel, int genomesLength) {

        Random rand = new Random();
        this.animalsNumber = animalsNumber;
        this.worldMap = new WorldMap(width, height);
        this.animals = new ArrayList<>();
        this.startEnergyLevel = startEnergyLevel;
        this.genomesLength = genomesLength;

        //dodac dodawanie na nowa pozycje


        int i = 0;
        while (i < animalsNumber) {
            String genome = "";
            for (int k = 0; k < genomesLength; k++) {
                int gene = rand.nextInt(8);
                genome += (char) gene;
            }

            Animal addedAnimal = new Animal(position, genome, startEnergyLevel);
            animals.add(addedAnimal);
            worldMap.place(addedAnimal);
        }
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void run() {
        //tutaj animale będą się ruszać po kolei
    }
}
