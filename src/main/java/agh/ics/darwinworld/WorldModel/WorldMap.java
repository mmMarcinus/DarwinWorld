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

    WorldMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.jungleTop = (int) ( height /2 + height * 0.1);
        this.jungleBottom = (int) (height/2 - height * 0.1);
    }

    public void place(Animal animal){
        Vector2d position = animal.getPosition();
        if (!animals.containsKey(position)) {
            animals.put(position, animal);

            notifyObservers();
        }//else throw new IncorrectPositionException(position); tutaj będa te rzeczy potem
    }

    private void notifyObservers(){
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this);
        }
    }

    public boolean isOccupied(Vector2d position) {
        for(Vector2d key : animals.keySet()){
            if (position.equals(key)) return true;
        }
        return false;
    }

    public boolean canMoveTo(Vector2d position){
        return !animals.containsKey(position);
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getJungleTop() { return jungleTop; }
    public int getJungleBottom() { return jungleBottom; }
}
