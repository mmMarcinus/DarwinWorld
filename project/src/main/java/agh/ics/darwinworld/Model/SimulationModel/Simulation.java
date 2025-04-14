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

        Vector2d position;

        for (int i = 0; i < worldParameters.startAnimalsNumber(); i++){
            int x = rand.nextInt(0, worldParameters.width()) ;
            int y = rand.nextInt(0, worldParameters.height());
            position = new Vector2d(x,y);
            Genome newGenome;
            if (!worldParameters.changeGenome()){
                newGenome = new Genome(worldParameters.genomesLength());
            }else{
                newGenome = new ChangeGenome(worldParameters.genomesLength());
            }
            Animal addedAnimal = new Animal(position, newGenome, worldParameters.startEnergyLevel(), 0, null, null, 0);

            worldMap.place(addedAnimal);
        }

        //adding plants to random positions

        List<Vector2d> availableJunglePositions = new ArrayList<>();
        List<Vector2d> availableOutsidePositions = new ArrayList<>();

        for (int x = 0; x < worldParameters.width(); x++) {
            // Jungle positions
            for (int y = worldMap.getJungleBottom(); y <= worldMap.getJungleTop(); y++) {
                availableJunglePositions.add(new Vector2d(x, y));
            }

            // Outside positions
            for (int y = 0; y < worldMap.getJungleBottom(); y++) {
                availableOutsidePositions.add(new Vector2d(x, y));
            }
            for (int y = worldMap.getJungleTop() + 1; y < worldMap.getHeight(); y++) {
                availableOutsidePositions.add(new Vector2d(x, y));
            }
        }

        Collections.shuffle(availableJunglePositions);
        Collections.shuffle(availableOutsidePositions);

        int jungleCount = Math.min((int) (worldParameters.startPlantNumber() * 0.8), availableJunglePositions.size());
        int outsideCount = Math.min(worldParameters.startPlantNumber() - jungleCount, availableOutsidePositions.size());

        for (int i = 0; i < jungleCount; i++) {
            Vector2d plantPosition = availableJunglePositions.get(i);
            Plant addedPlant = new Plant(plantPosition, worldParameters.energyFromPlant());
            worldMap.place(addedPlant);
        }

        for (int i = 0; i < outsideCount; i++) {
            Vector2d plantPosition = availableOutsidePositions.get(i);
            Plant addedPlant = new Plant(plantPosition, worldParameters.energyFromPlant());
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
        while(simulationRunning && dayCount <= 100) {
            if (running) {
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
