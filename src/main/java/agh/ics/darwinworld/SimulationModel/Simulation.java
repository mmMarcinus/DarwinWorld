package agh.ics.darwinworld.SimulationModel;

import agh.ics.darwinworld.AnimalModel.Animal;
import agh.ics.darwinworld.AnimalModel.Reproduce;
import agh.ics.darwinworld.Util.Vector2d;
import agh.ics.darwinworld.WorldModel.Plant;
import agh.ics.darwinworld.WorldModel.WorldMap;

import java.util.*;

public class Simulation implements Runnable {
    private final int startAnimalsNumber;
    public List<Animal> animals;
    public WorldMap worldMap;
    public List<Plant> plants;
    private final int genomesLength;
    public final int startEnergyLevel;
    private int reproduceEnergyRequired;
    private int energyFromPlant;
    private final int startPlantNumber;
    private final int dayPlantNumber;
    private boolean mapVariant;
    private boolean mutationVariant;

    public Simulation(int startAnimalsNumber, int startPlantNumber, int dayPlantNumber, int width, int height,
                      int startEnergyLevel, int genomesLength, int reproduceEnergyRequired, int energyFromPlant,
                      boolean mapVariant, boolean mutationVariant) {

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

        Random rand = new Random();
        //dodawanie startowych zwierzakow na losowe pozycje z losowymi genomami

        HashSet<Vector2d> animalPositionsTaken = new HashSet<>();
        Vector2d position;

        int i = 0;
        while (i < startAnimalsNumber) {
            String genome = "";
            for (int k = 0; k < genomesLength; k++) {
                int gene = rand.nextInt(8);
                genome += (char) gene;
            }
            do{
                int x = rand.nextInt(0, width) ;
                int y = rand.nextInt(0, height);
                position = new Vector2d(x,y);
            }while(animalPositionsTaken.contains(position));
            animalPositionsTaken.add(position);

            Animal addedAnimal = new Animal(position, genome, startEnergyLevel, 0);
            animals.add(addedAnimal);
            worldMap.place(addedAnimal);
            i+=1;
        }

        //dodawanie startowych roślinek na losowe pozycje

        HashSet<Vector2d> plantPositionsTaken = new HashSet<>();

        i = 0;
        while (i < startPlantNumber) {
            do{
                //najpierw wybieramy czy w jungli czy nie
                double isJungle = rand.nextDouble();
                int x = rand.nextInt(0, width);
                int y = 0;
                if (isJungle<=0.8){//w dzungli
                    y = rand.nextInt(worldMap.getJungleBottom(), worldMap.getJungleTop());
                }else{//poza dzungla
                    double isBottom = rand.nextDouble();
                    if(isBottom<=0.5){//poludnie
                        y = rand.nextInt(0, worldMap.getJungleBottom());
                    }else{//polnoc
                        y = rand.nextInt(worldMap.getJungleTop(), worldMap.getHeight());
                    }
                }
                position = new Vector2d(x,y);
            }while(plantPositionsTaken.contains(position));
            plantPositionsTaken.add(position);

            Plant addedPlant = new Plant(position, startEnergyLevel);
            plants.add(addedPlant);
            worldMap.place(addedPlant);
            i+=1;
        }

    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void run() {
        //PO CO NAM LISTY ZWIERZAKOW I ROSLIN W SIMULATION I W WORLDMAPIE
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
            HashSet<Animal> reproducedAnimals = new HashSet<>();

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
                        Reproduce.reproduce(reproduceCandidates.get(i-1),reproduceCandidates.get(i), startEnergyLevel);
                    }
                }
            }

            //Wzrastanie nowych roślin na wybranych polach mapy.
            for(int i = 0; i<dayPlantNumber; i++){
                //najpierw wybieramy czy w dżungli czy nie
                double isJungle = rand.nextDouble();
                int x = rand.nextInt(0, worldMap.getWidth());
                int y = 0;
                if (isJungle<=0.8){//w dzungli
                    y = rand.nextInt(worldMap.getJungleBottom(), worldMap.getJungleTop());
                }else{//poza dzungla
                    double isBottom = rand.nextDouble();
                    if(isBottom<=0.5){//poludnie
                        y = rand.nextInt(0, worldMap.getJungleBottom());
                    }else{//polnoc
                        y = rand.nextInt(worldMap.getJungleTop(), worldMap.getHeight());
                    }
                }

                Plant addedPlant = new Plant(new Vector2d(x,y), startEnergyLevel);
                plants.add(addedPlant);
                worldMap.place(addedPlant);
            }

            day++;
        }
    }
}
