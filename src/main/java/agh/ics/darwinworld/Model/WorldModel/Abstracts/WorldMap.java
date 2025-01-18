package agh.ics.darwinworld.Model.WorldModel.Abstracts;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Model.Util.Vector2d;
import agh.ics.darwinworld.Model.WorldModel.Plant;

import java.util.Map;

public interface WorldMap{
    void place(Animal animal);

    void place(Plant plant);

    void remove(Animal animal);

    void remove(Plant plant);

    void move(Animal animal, String move, int energyTakenEachDay);

    void removeDeadAnimals();

    void moveAllAnimals(int energyTakenEachDay);

    void eatPlants(int energyFromPlant);

    void reproduceAnimals(int startEnergyLevel, int reproduceEnergyRequired, int minMutations, int maxMutations);

    void placeNewPlants(int dayPlantNumber, int energyFromPlant);

    int getWidth();

    int getHeight();

    int getJungleTop();

    int getJungleBottom();

    Map<Vector2d, Plant> getPlants();

    Map<Vector2d, Animal> getAnimals();

    void attachListener(MapChangeListener observer);

    void detachListener(MapChangeListener observer);

    void notifyListeners();
}
