package agh.ics.darwinworld.WorldModel;

import agh.ics.darwinworld.AnimalModel.Animal;
import agh.ics.darwinworld.Util.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldMap {
    private int width;
    private int height;
    private int jungleTop;
    private int jungleBottom;

    private Map<Vector2d,Animal> animals = new HashMap<Vector2d, Animal>();
    private Map<Vector2d, Plant> plants = new HashMap<Vector2d, Plant>();
    private ArrayList<MapChangeListener> observers = new ArrayList<MapChangeListener>();

    public WorldMap(int width, int height) {
        this.width = width;
        this.height = height;

        this.jungleTop = (int) ( height /2 + height * 0.1);
        this.jungleBottom = (int) (height/2 - height * 0.1);
    }

    public void attachListener(MapChangeListener listener) {
        observers.add(listener);
    }

    public void place(Animal animal){
        Vector2d position = animal.getPosition();
        if (!animals.containsKey(position)) {
            animals.put(position, animal);
            //notifyObservers("Ustawiono nowe zwierze na pozycji " + position.toString());
        }//else throw new IncorrectPositionException(position); tutaj będa te rzeczy potem
    }

    public void place(Plant plant){
        Vector2d position = plant.getPosition();
        if (!plants.containsKey(position)) {
            plants.put(position, plant);
            //notifyObservers("Ustawiono nowe zwierze na pozycji " + position.toString());
        }//else throw new IncorrectPositionException(position); tutaj będa te rzeczy potem
    }

    public void remove(Animal animal){
        Vector2d position = animal.getPosition();
        animals.remove(position, animal);
    }

    public void remove(Plant plant){
        Vector2d position = plant.getPosition();
        plants.remove(position, plant);
    }

    public boolean isOccupied(Vector2d position) {
        for(Vector2d key : animals.keySet()){
            if (position.equals(key)) return true;
        }
        return false;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getJungleTop() { return jungleTop; }
    public int getJungleBottom() { return jungleBottom; }
    public Map<Vector2d, Plant> getPlants() {return plants;}
    public Map<Vector2d, Animal> getAnimals() {return animals;}

}
