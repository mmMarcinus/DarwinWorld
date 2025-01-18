package agh.ics.darwinworld.Model.SimulationModel;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Model.AnimalModel.ChangeGenome;
import agh.ics.darwinworld.Model.AnimalModel.Genome;
import agh.ics.darwinworld.Presenter.Simulation.SimulationPresenter;
import agh.ics.darwinworld.Model.Util.Vector2d;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.WorldMap;
import agh.ics.darwinworld.Model.WorldModel.Plant;
import agh.ics.darwinworld.Model.WorldModel.NormalWorldMap;
import agh.ics.darwinworld.Model.WorldModel.PolarWorldMap;
import agh.ics.darwinworld.Model.Records.WorldParameters;

import java.util.*;

public class Simulation implements Runnable {
    private final WorldParameters worldParameters;

    private WorldMap worldMap;

    private int dayCount;

    public Simulation(WorldParameters worldParameters) {
        Random rand = new Random();

        this.worldParameters = worldParameters;

        if(!worldParameters.polarMap()){
            this.worldMap = new NormalWorldMap(worldParameters.width(), worldParameters.height());
        } else{
            this.worldMap = new PolarWorldMap(worldParameters.width(), worldParameters.height());
        }

        //dodawanie startowych zwierzakow na losowe pozycje z losowymi genomami

        HashSet<Vector2d> animalPositionsTaken = new HashSet<>();
        Vector2d position;

        int i = 0;
        while (i < worldParameters.startAnimalsNumber()) {
            do{
                int x = rand.nextInt(0, worldParameters.width()) ;
                int y = rand.nextInt(0, worldParameters.height());
                position = new Vector2d(x,y);
            }while(animalPositionsTaken.contains(position));
            animalPositionsTaken.add(position);
            Genome newGenome;
            if (!worldParameters.changeGenome()){
                newGenome = new Genome(worldParameters.genomesLength());
            }else{
                newGenome = new ChangeGenome(worldParameters.genomesLength());
            }
            Animal addedAnimal = new Animal(position, newGenome, worldParameters.startEnergyLevel(), 0, null, null, 0);

            worldMap.place(addedAnimal);
            i+=1;
        }


        //dodawanie startowych roślinek na losowe pozycje

        HashSet<Vector2d> plantPositionsTaken = new HashSet<>();

        i = 0;
        while (i < worldParameters.startPlantNumber()) {
            do{
                //najpierw wybieramy czy w jungli czy nie
                double isJungle = rand.nextDouble();
                int x = rand.nextInt(0, worldParameters.width());
                int y = 0;
                if (isJungle<=0.8){//w dzungli
                    y = rand.nextInt(worldMap.getJungleBottom(), worldMap.getJungleTop()+1);
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
            Plant addedPlant = new Plant(position, worldParameters.energyFromPlant());

            worldMap.place(addedPlant);
            i+=1;
        }

    }

    public WorldMap getWorldMap(){
        return worldMap;
    }

    public void run() {

        this.dayCount = 1;
        while(dayCount < 20) {
            System.out.println("Dzien " + dayCount + " rozpoczyna sie");


            //Usunięcie martwych zwierzaków z mapy.
            System.out.println("Usuwanie martwych zwierzakow z mapy");
            worldMap.removeDeadAnimals();

            //Skręt i przemieszczenie każdego zwierzaka.
            System.out.println("Zwierzaki wykonuja swoje ruchy");
            worldMap.moveAllAnimals(worldParameters.energyTakenEachDay());

            //Konsumpcja roślin, na których pola weszły zwierzaki
            System.out.println("Zwierzaki jedza napotkane rosliny");
            worldMap.eatPlants(worldParameters.energyFromPlant());

            //Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
            System.out.println("Zwierzaki rozmnazaja sie");
            worldMap.reproduceAnimals(worldParameters.startEnergyLevel(), worldParameters.reproduceEnergyRequired(),
                                      worldParameters.minMutation(), worldParameters.maxMutation());

            System.out.println("Wyrastaja nowe rosliny");
            //Wzrastanie nowych roślin na wybranych polach mapy.
            worldMap.placeNewPlants(worldParameters.dayPlantNumber(), worldParameters.energyFromPlant());

            System.out.println("Dzien " + dayCount + " zakonczyl sie\n\n");
            dayCount++;
        }
    }
}
