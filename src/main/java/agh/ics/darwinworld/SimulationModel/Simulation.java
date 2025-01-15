package agh.ics.darwinworld.SimulationModel;

import agh.ics.darwinworld.AnimalModel.Animal;
import agh.ics.darwinworld.AnimalModel.ChangeGenome;
import agh.ics.darwinworld.AnimalModel.Genome;
import agh.ics.darwinworld.Util.Vector2d;
import agh.ics.darwinworld.WorldModel.Abstracts.WorldMap;
import agh.ics.darwinworld.WorldModel.Plant;
import agh.ics.darwinworld.WorldModel.NormalWorldMap;
import agh.ics.darwinworld.WorldModel.PolarWorldMap;

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
    private boolean polarMap;
    private boolean changeGenome;

    public Simulation(int startAnimalsNumber, int startPlantNumber, int dayPlantNumber, int width, int height,
                      int startEnergyLevel, int genomesLength, int reproduceEnergyRequired, int energyFromPlant,
                      int energyTakenEachDay, int minMutation, int maxMutation,boolean polarMap, boolean changeGenome) {

        this.startAnimalsNumber = startAnimalsNumber;
        this.startPlantNumber = startPlantNumber;
        this.dayPlantNumber = dayPlantNumber;
        this.startEnergyLevel = startEnergyLevel;
        this.genomesLength = genomesLength;
        this.reproduceEnergyRequired = reproduceEnergyRequired;
        this.energyFromPlant = energyFromPlant;
        this.changeGenome = changeGenome;
        this.polarMap = polarMap;
        this.energyTakenEachDay = energyTakenEachDay;
        this.minMutation = minMutation;
        this.maxMutation = maxMutation;
        if(!polarMap){
            this.worldMap = new NormalWorldMap(width, height);
        } else{
            this.worldMap = new PolarWorldMap(width, height);
        }

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
            Genome newGenome;
            if (!changeGenome){
                newGenome = new Genome(genomesLength);
            }else{
                newGenome = new ChangeGenome(genomesLength);
            }
            Animal addedAnimal = new Animal(position, newGenome, startEnergyLevel, 0, null, null, 0);

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
        Random rand = new Random();

        int day = 1;
        while(day < 4) {
            System.out.println("Dzien " + day + " rozpoczyna sie");


            //Usunięcie martwych zwierzaków z mapy.
            System.out.println("Usuwanie martwych zwierzakow z mapy");
            worldMap.removeDeadAnimals();

            //Skręt i przemieszczenie każdego zwierzaka.
            System.out.println("Zwierzaki wykonuja swoje ruchy");
            worldMap.moveAllAnimals(energyTakenEachDay);

            //Konsumpcja roślin, na których pola weszły zwierzaki
            System.out.println("Zwierzaki jedza napotkane rosliny");
            worldMap.eatPlants(energyFromPlant);

            //Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
            System.out.println("Zwierzaki rozmnazaja sie");
            worldMap.reproduceAnimals(startEnergyLevel, reproduceEnergyRequired, maxMutation-minMutation);

            System.out.println("Wyrastaja nowe rosliny");
            //Wzrastanie nowych roślin na wybranych polach mapy.
            worldMap.placeNewPlants(dayPlantNumber, energyFromPlant);


            System.out.println("Dzien " + day + " zakonczyl sie\n\n");
            day++;
       }
    }
}
