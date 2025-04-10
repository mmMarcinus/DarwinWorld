package agh.ics.darwinworld.Model.SimulationModel;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Model.AnimalModel.ChangeGenome;
import agh.ics.darwinworld.Model.AnimalModel.Genome;
import agh.ics.darwinworld.Model.Util.Vector2d;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.WorldMap;
import agh.ics.darwinworld.Model.WorldModel.Plant;
import agh.ics.darwinworld.Model.WorldModel.NormalWorldMap;
import agh.ics.darwinworld.Model.WorldModel.PolarWorldMap;
import agh.ics.darwinworld.Model.Records.WorldParameters;
import agh.ics.darwinworld.Presenter.MapStatistics.MapStatistics;

import java.util.*;

public class Simulation implements Runnable {
    private final WorldParameters worldParameters;

    private final WorldMap worldMap;

    private int dayCount = 1;

    private volatile boolean simulationRunning;

    private boolean running;

    private MapStatistics mapStatistics = new MapStatistics();

    public Simulation(WorldParameters worldParameters) {
        Random rand = new Random();

        this.worldParameters = worldParameters;

        UUID uuid = UUID.randomUUID();

        if(!worldParameters.polarMap()){
            this.worldMap = new NormalWorldMap(worldParameters.width(), worldParameters.height(), uuid);
        } else{
            this.worldMap = new PolarWorldMap(worldParameters.width(), worldParameters.height(), uuid);
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

        this.worldMap.attachSimulation(this);
    }

    public WorldMap getWorldMap(){
        return worldMap;
    }

    public void run() {
        simulationRunning = true;
        running = true;
        while(simulationRunning && dayCount <= 500) {
            if (running) {
                System.out.println("Dzien " + dayCount);

                worldMap.removeDeadAnimals();

                worldMap.moveAllAnimals(worldParameters.energyTakenEachDay());

                worldMap.eatPlants(worldParameters.energyFromPlant());

                worldMap.reproduceAnimals(worldParameters.startEnergyLevel(), worldParameters.reproduceEnergyRequired(),
                        worldParameters.minMutation(), worldParameters.maxMutation());

                worldMap.placeNewPlants(worldParameters.dayPlantNumber(), worldParameters.energyFromPlant());

                worldMap.updateStatistics(mapStatistics, dayCount);

                worldMap.notifyListeners(this, mapStatistics);

                dayCount++;
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void close(){
        simulationRunning=false;
    }

    public void stop(){
        running=false;
    }

    public void start(){
        running=true;
    }

    public int getDayCount() {return dayCount;}
}
