package agh.ics.darwinworld.SimulationModel;

import agh.ics.darwinworld.AnimalModel.Animal;
import agh.ics.darwinworld.AnimalModel.Genome;
import agh.ics.darwinworld.AnimalModel.Reproduce;
import agh.ics.darwinworld.Util.Vector2d;
import agh.ics.darwinworld.WorldModel.Plant;
import agh.ics.darwinworld.WorldModel.WorldMap;

import java.util.*;

public class Simulation implements Runnable {
    private final int startAnimalsNumber;
    private WorldMap worldMap;
    private final int genomesLength;
    private final int startEnergyLevel;
    private int reproduceEnergyRequired;
    private int energyFromPlant;
    private final int startPlantNumber;
    private final int dayPlantNumber;
    private final int energyTakenEachDay;
    private final int minMutation;
    private final int maxMutation;
    private boolean mapVariant;
    private boolean mutationVariant;

    public Simulation(int startAnimalsNumber, int startPlantNumber, int dayPlantNumber, int width, int height,
                      int startEnergyLevel, int genomesLength, int reproduceEnergyRequired, int energyFromPlant,
                      int energyTakenEachDay, int minMutation, int maxMutation, boolean mapVariant, boolean mutationVariant) {

        this.startAnimalsNumber = startAnimalsNumber;
        this.startPlantNumber = startPlantNumber;
        this.dayPlantNumber = dayPlantNumber;
        this.worldMap = new WorldMap(width, height);
        this.startEnergyLevel = startEnergyLevel;
        this.genomesLength = genomesLength;
        this.reproduceEnergyRequired = reproduceEnergyRequired;
        this.energyFromPlant = energyFromPlant;
        this.mapVariant = mapVariant;
        this.mutationVariant = mutationVariant;
        this.energyTakenEachDay = energyTakenEachDay;
        this.minMutation = minMutation;
        this.maxMutation = maxMutation;

        Random rand = new Random();
        //dodawanie startowych zwierzakow na losowe pozycje z losowymi genomami

        HashSet<Vector2d> animalPositionsTaken = new HashSet<>();
        Vector2d position;

        int i = 0;
        while (i < startAnimalsNumber) {
            do{
                int x = rand.nextInt(0, width) ;
                int y = rand.nextInt(0, height);
                position = new Vector2d(x,y);
            }while(animalPositionsTaken.contains(position));
            animalPositionsTaken.add(position);
            Animal addedAnimal = new Animal(position, new Genome(genomesLength), startEnergyLevel, 0, null, null, 0);

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
            Plant addedPlant = new Plant(position, energyFromPlant);

            worldMap.place(addedPlant);
            i+=1;
        }

    }

    public void run() {
        //PO CO NAM LISTY ZWIERZAKOW I ROSLIN W SIMULATION I W WORLDMAPIE
        Random rand = new Random();
        ArrayList<Animal> animalsToDelete;
        ArrayList<Plant> plantsToDelete;

        int day = 1;
        while(day < 4) {
            System.out.println("Dzien " + day + " rozpoczyna sie");

            //Usunięcie martwych zwierzaków z mapy.
            System.out.println("Usuwanie martwych zwierzakow z mapy");
            animalsToDelete = new ArrayList<>();
            for (Animal animal : worldMap.getAnimals().values()) {
                if (animal.getEnergyLevel() <= 0){
                    animalsToDelete.add(animal);
                }
                else{
                    animal.updateAge(animal.getAge()+1);
                }
            }
            for (Animal animal : animalsToDelete) {
                worldMap.remove(animal);
            }

            //Skręt i przemieszczenie każdego zwierzaka.
            System.out.println("Zwierzaki wykonuja swoje ruchy");
            for (Animal animal : worldMap.getAnimals().values()){
                Genome genome = animal.getGenome();
                String move = genome.getGenes().substring((animal.getCurrentGene()));
                animal.move(move,worldMap);
            }

            //Konsumpcja roślin, na których pola weszły zwierzaki
            System.out.println("Zwierzaki jedza napotkane rosliny");

            Animal consumer;
            plantsToDelete = new ArrayList<>();
            for (Plant plant : worldMap.getPlants().values()){
                consumer = new Animal(new Vector2d(0,0), new Genome(0), -1, 0, null, null, 0);
                if (plantsToDelete.contains(plant)){
                    continue;
                }
                for (Animal animal : worldMap.getAnimals().values()){
                    if (plant.getPosition().equals(animal.getPosition())){
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
                    plantsToDelete.add(plant);
                }
            }
            //usuwamy zjedzone rosliny
            for(Plant plant : plantsToDelete){
                worldMap.remove(plant);
            }


            System.out.println("Zwierzaki rozmnazaja sie");
            //Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
            ArrayList<Animal> reproduceCandidates;
            HashSet<Animal> reproducedAnimals = new HashSet<>();

            for (Animal positionAnimal : worldMap.getAnimals().values()){
                if (!reproducedAnimals.contains(positionAnimal) && positionAnimal.getEnergyLevel() >= reproduceEnergyRequired) {
                    reproduceCandidates = new ArrayList<>();
                    boolean isCandidate = false;
                    for (Animal animal : worldMap.getAnimals().values()) {
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
                        Reproduce.reproduce(reproduceCandidates.get(i-1),reproduceCandidates.get(i), startEnergyLevel, mutationVariant, maxMutation-minMutation);
                    }
                }
            }

            System.out.println("Wyrastaja nowe rosliny");
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
                worldMap.place(addedPlant);
            }

            System.out.println("Odejmujemy energie");
            //Zmniejszanie energii
            for (Animal animal : worldMap.getAnimals().values()){
                //Wariant Bieguny
                if (mapVariant) {
                    int poleHeight = (int) (worldMap.getHeight() * 2 / 10);
                    int increasedEnergyDrop = 0;
                    if (animal.getPosition().getY() <= poleHeight){
                        increasedEnergyDrop = animal.getPosition().getY();
                    }
                    else if (animal.getPosition().getY() + poleHeight > worldMap.getHeight()){
                        increasedEnergyDrop = worldMap.getHeight() - animal.getPosition().getY() + 1;
                    }
                    increasedEnergyDrop = (int) Math.ceil(increasedEnergyDrop*13/10*energyTakenEachDay);
                    animal.updateEnergyLevel(animal.getEnergyLevel() - increasedEnergyDrop);
                }
                //Wariant normalny
                else {
                    animal.updateEnergyLevel(animal.getEnergyLevel() - energyTakenEachDay);
                }
            }
            // Przesuwanie genomu na następny
            for (Animal animal : worldMap.getAnimals().values()){
                animal.updateCurrentGene((animal.getCurrentGene()+1)%genomesLength);
            }
            System.out.println("Dzien " + day + " zakonczyl sie\n\n");
           day++;
       }
    }
}
