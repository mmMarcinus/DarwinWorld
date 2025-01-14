package agh.ics.darwinworld.SimulationModel;

import agh.ics.darwinworld.AnimalModel.Animal;
import agh.ics.darwinworld.AnimalModel.Reproduce;
import agh.ics.darwinworld.Util.Vector2d;
import agh.ics.darwinworld.WorldModel.Plant;
import agh.ics.darwinworld.WorldModel.WorldMap;

import java.util.*;

public class Simulation implements Runnable {
    private int startAnimalsNumber;
    public static List<Animal> animals;
    public static WorldMap worldMap;
    public static List<Plant> plants;
    private int genomesLength;
    public static int startEnergyLevel;
    private int reproduceEnergyRequired;
    private int energyFromPlant;
    private int startPlantNumber;
    private int dayPlantNumber;
    private boolean mapVariant;
    private boolean mutationVariant;


    public Simulation(int startAnimalsNumber, int startPlantNumber, int dayPlantNumber, int width, int height,
                      int startEnergyLevel, int genomesLength, int reproduceEnergyRequired, int energyFromPlant,
                      boolean mapVariant, boolean mutationVariant) {

        Random rand = new Random();
        this.startAnimalsNumber = startAnimalsNumber;
        this.animals = new ArrayList<>();
        this.startPlantNumber = startPlantNumber;
        this.dayPlantNumber = dayPlantNumber;
        this.worldMap = new WorldMap(width, height);
        this.startEnergyLevel = startEnergyLevel;
        this.genomesLength = genomesLength;
        this.reproduceEnergyRequired = reproduceEnergyRequired;
        this.energyFromPlant = energyFromPlant;
        this.mapVariant = mapVariant;
        this.mutationVariant = mutationVariant;

        //dodawanie startowych zwierzakow na losowe pozycje z losowymi genomami

        HashSet animalPositionsTaken = new HashSet<>();
        Vector2d position;

        int i = 0;
        while (i < startAnimalsNumber) {
            String genome = "";
            for (int k = 0; k < genomesLength; k++) {
                int gene = rand.nextInt(8);
                genome += (char) gene;
            }
            do{
                int x = rand.nextInt(width) ;
                int y = rand.nextInt(height);
                position = new Vector2d(x,y);
            }while(animalPositionsTaken.contains(position));
            animalPositionsTaken.add(position);

            Animal addedAnimal = new Animal(position, genome, startEnergyLevel, 0);
            animals.add(addedAnimal);
            worldMap.place(addedAnimal);
        }

        //dodawanie startowych roślinek na losowe pozycje

        HashSet PlantPositionsTaken = new HashSet<>();

        i = 0;
        while (i < startPlantNumber){
            do{
                int x = rand.nextInt(width) ;
                int y = rand.nextInt(height);
                position = new Vector2d(x,y);
            }while(PlantPositionsTaken.contains(position));
            PlantPositionsTaken.add(position);

            Plant addedPlant = new Plant();
            plants.add(addedPlant);
            worldMap.place(addedPlant);
        }

    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void run() {
        //tutaj animale będą się ruszać po kolei

        Random rand = new Random();

        int day = 1;
        while(day < 10000) {
            //Usunięcie martwych zwierzaków z mapy.

            for (Animal animal : animals){
                if (animal.getEnergyLevel() <= 0){
                    animals.remove(animal);
                    worldMap.remove(animal);
                }
                else{
                    animal.updateAge(animal.getAge()+1);
                }
            }

            //Skręt i przemieszczenie każdego zwierzaka.

            for (Animal animal : animals){
                String genome = animal.getGenome();
                String move = genome.substring((day - 1) % genomesLength);
                animal.move(move,worldMap);
            }

            //Konsumpcja roślin, na których pola weszły zwierzaki

            for (Plant plant : plants){
                Animal consumer = new Animal(new Vector2d(0,0), "", -1, 0);
                for (Animal animal : animals){
                    if (plant.getPosition() == animal.getPosition()){
                        if (consumer.getEnergyLevel() < animal.getEnergyLevel()){
                            consumer = animal;
                        }
                        else if (consumer.getEnergyLevel() == animal.getEnergyLevel()){
                            if (consumer.getAge() < animal.getAge()){
                                consumer = animal;
                            }
                            else if(consumer.getAge() == animal.getAge()) {
                                if (consumer.getKidsNumber() < animal.getKidsNumber()) {
                                    consumer = animal;
                                }
                                else if(consumer.getKidsNumber() == animal.getKidsNumber()){
                                    boolean random = rand.nextBoolean();
                                    if (random){
                                        consumer = animal;
                                    }
                                }
                            }
                        }
                    }
                }
                if (consumer.getEnergyLevel() != -1){
                    consumer.updateEnergyLevel(consumer.getEnergyLevel()+energyFromPlant);
                    plants.remove(plant);
                    worldMap.remove(plant);
                }
            }

            //Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.

            ArrayList<Animal> reproduceCandidates;
            HashSet reproducedAnimals = new HashSet<>();

            for (Animal positionAnimal : animals){
                if (!reproducedAnimals.contains(positionAnimal) && positionAnimal.getEnergyLevel() >= reproduceEnergyRequired) {
                    reproduceCandidates = new ArrayList<>();
                    boolean isCandidate = false;
                    for (Animal animal : animals) {
                        if (positionAnimal.getPosition() == animal.getPosition() && animal.getEnergyLevel() >= reproduceEnergyRequired) {
                            isCandidate = true;
                            reproduceCandidates.add(animal);
                            reproducedAnimals.add(animal);
                        }
                    }
                    if (isCandidate) {
                        reproduceCandidates.add(positionAnimal);
                        reproducedAnimals.add(positionAnimal);
                    }
                    reproduceCandidates.sort(Comparator.comparing(Animal::getEnergyLevel)
                                            .thenComparing(Animal::getAge)
                                            .thenComparing(Animal::getKidsNumber));
                    for (int i = 1; i < reproduceCandidates.size(); i+=2){
                        Reproduce.reproduce(reproduceCandidates.get(i-1),reproduceCandidates.get(i), mutationVariant);
                    }
                }
            }

            //Wzrastanie nowych roślin na wybranych polach mapy.



            day++;
        }
    }
}
