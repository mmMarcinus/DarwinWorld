package agh.ics.darwinworld.Model.WorldModel.Abstracts;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Model.AnimalModel.Genome;
import agh.ics.darwinworld.Model.AnimalModel.Reproduce;
import agh.ics.darwinworld.Model.SimulationModel.Simulation;
import agh.ics.darwinworld.Model.Util.Vector2d;
import agh.ics.darwinworld.Model.WorldModel.Plant;
import agh.ics.darwinworld.Presenter.Statistics.MapStatistics;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractWorldMap implements WorldMap {
    protected int width;
    protected int height;
    protected int jungleTop;
    protected int jungleBottom;
    protected Map<Vector2d, Animal> animals = new HashMap<Vector2d,Animal>();
    protected Map<Vector2d, Plant> plants = new HashMap<Vector2d, Plant>();
    protected List<MapChangeListener> listeners = new ArrayList<>();
    protected Simulation simulation;
    protected List<Animal> deadAnimals = new ArrayList<>();

    @Override
    public abstract void move(Animal animal, String move, int energyTakenEachDay);

    @Override
    public void removeDeadAnimals() {
        ArrayList<Animal> animalsToDelete;
        animalsToDelete = new ArrayList<>();
        for (Animal animal : animals.values()) {
            if (animal.getEnergyLevel() <= 0){
                animalsToDelete.add(animal);
            }
            else{
                animal.updateAge(animal.getAge()+1);
            }
        }
        for (Animal animal : animalsToDelete) {
            deadAnimals.add(animal);
            this.remove(animal);
        }
    }

    @Override
    public void moveAllAnimals(int energyTakenEachDay) {
        for (Animal animal : animals.values()){
            Genome genome = animal.getGenome();
            String move = genome.getGenes().substring((animal.getCurrentGene()));
            move(animal, move, energyTakenEachDay);
        }
    }

    @Override
    public void eatPlants(int energyFromPlant){
        Random rand = new Random();

        ArrayList<Plant> plantsToDelete = new ArrayList<>();
        Animal consumer;
        for (Plant plant : plants.values()){
            //wybieramy, zgodnie z wytycznymi, który zwierzak zje roślinkę
            consumer = new Animal(new Vector2d(0,0), new Genome(0), -1, 0, null, null, 0);
            if (plantsToDelete.contains(plant)){
                continue;
            }
            for (Animal animal : animals.values()){
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
            this.remove(plant);
        }
    }

    @Override
    public void reproduceAnimals(int startEnergyLevel, int reproduceEnergyRequired, int minMutation, int maxMutation) {
        ArrayList<Animal> reproduceCandidates;
        HashSet<Animal> reproducedAnimals = new HashSet<>();

        for (Animal positionAnimal : animals.values()){
            if (!reproducedAnimals.contains(positionAnimal) && positionAnimal.getEnergyLevel() >= reproduceEnergyRequired) {
                reproduceCandidates = new ArrayList<>();
                boolean isCandidate = false;
                for (Animal animal : animals.values()) {
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
                    Reproduce.reproduce(reproduceCandidates.get(i-1),reproduceCandidates.get(i), startEnergyLevel, minMutation, maxMutation);
                }
            }
        }
    }

    @Override
    public void placeNewPlants(int dayPlantNumber, int energyFromPlant){
        Random rand = new Random();

        for(int i = 0; i<dayPlantNumber; i++){
            //jeśli nie da się już umieścić rośliny to kończymy
            if(plants.size()>=(height*width)){break;}

            //ustawiamy roślinkę
            int x;
            int y;
            do {
                //najpierw wybieramy czy w dżungli czy nie
                double isJungle = rand.nextDouble();
                x = rand.nextInt(0, width);
                y = 0;
                if (isJungle <= 0.8) {//w dzungli
                    y = rand.nextInt(jungleBottom, jungleTop);
                } else {//poza dzungla
                    double isBottom = rand.nextDouble();
                    if (isBottom <= 0.5) {//poludnie
                        y = rand.nextInt(0, jungleBottom);
                    } else {//polnoc
                        y = rand.nextInt(jungleTop, height);
                    }
                }
            }while(plants.containsKey(new Vector2d(x,y)));

            Plant addedPlant = new Plant(new Vector2d(x,y), energyFromPlant);
            this.place(addedPlant);
        }
    }

    private void addOrUpdateGenome(Map<String, Integer> map, String genome) {
        map.put(genome, map.getOrDefault(genome, 0) + 1);
    }

    public void updateStatistics(MapStatistics statistics, int dayCount){
        double averageKidsNumber = 0.0;
        double averageEnergyLevel = 0.0;
        String firstPopularGenome;
        int firstPopularGenomeCount;
        String secondPopularGenome;
        int secondPopularGenomeCount;
        String thirdPopularGenome;
        int thirdPopularGenomeCount;
        Map<Vector2d, Animal> animalPlaces = new HashMap<>();
        Map<String, Integer> genomesWithCount = new HashMap<>();

        for (Animal animal : animals.values()) {
            averageKidsNumber += animal.getKidsNumber();
            averageEnergyLevel += animal.getEnergyLevel();
            addOrUpdateGenome(genomesWithCount, animal.getGenome().getGenes());
            Vector2d position = animal.getPosition();
            if (!animalPlaces.containsKey(position)){
                animalPlaces.put(position, animal);
            }
        }

        averageKidsNumber /= animals.size();
        averageEnergyLevel /= animals.size();

        List<Map.Entry<String, Integer>> top3 = genomesWithCount.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(3)
                .collect(Collectors.toList());

        if (top3.size() > 0) {
            firstPopularGenome = top3.get(0).getKey();
            firstPopularGenomeCount = top3.get(0).getValue();
        } else {
            firstPopularGenome = "Too little different genomes";
            firstPopularGenomeCount = 0;
        }

        if (top3.size() > 1) {
            secondPopularGenome = top3.get(1).getKey();
            secondPopularGenomeCount = top3.get(1).getValue();
        } else {
            secondPopularGenome = "Too little different genomes";
            secondPopularGenomeCount = 0;
        }

        if (top3.size() > 2) {
            thirdPopularGenome = top3.get(2).getKey();
            thirdPopularGenomeCount = top3.get(2).getValue();
        } else {
            thirdPopularGenome = "Too little different genomes";
            thirdPopularGenomeCount = 0;
        }

        double averageLifeLength = 0.0;

        for (Animal deadAnimal : deadAnimals) {
            averageLifeLength += deadAnimal.getAge();
        }

        averageLifeLength /= deadAnimals.size();

        statistics.setStatistics(dayCount, animals.size(), plants.size(), height * width - plants.size() - animalPlaces.size(),
                firstPopularGenome, firstPopularGenomeCount, secondPopularGenome, secondPopularGenomeCount, thirdPopularGenome,
                thirdPopularGenomeCount, averageEnergyLevel, averageLifeLength, averageKidsNumber);
    }

    @Override
    public void place(Animal animal){
        Vector2d position = animal.getPosition();
        animals.put(position, animal);
    }

    @Override
    public void place(Plant plant){
        Vector2d position = plant.getPosition();
        plants.put(position, plant);
    }

    @Override
    public void remove(Animal animal){
        Vector2d position = animal.getPosition();
        animals.remove(position, animal);
    }

    @Override
    public void remove(Plant plant){
        Vector2d position = plant.getPosition();
        plants.remove(position, plant);
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public int getJungleTop() { return jungleTop; }

    public int getJungleBottom() { return jungleBottom; }

    public Map<Vector2d, Plant> getPlants() {return plants;}

    public Map<Vector2d, Animal> getAnimals() {return animals;}

    @Override
    public void attachListener(MapChangeListener listener){
        listeners.add(listener);
    }

    @Override
    public void detachListener(MapChangeListener listener){
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners(Simulation simulation, MapStatistics mapStatistics) {
        for (MapChangeListener listener : listeners) {
            listener.mapChanged(mapStatistics);
        }
    }
}
