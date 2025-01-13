package agh.ics.darwinworld.WorldModel;

import agh.ics.darwinworld.AnimalModel.Animal;
import agh.ics.darwinworld.Util.Vector2d;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldMap {
    private int width;
    private int height;
    private int jungleTop;
    private int jungleBottom;

    private Map<Vector2d,Animal> animals = new HashMap<Vector2d, Animal>();

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
            //notifyObservers("Ustawiono nowe zwierze na pozycji " + position.toString());
        }//else throw new IncorrectPositionException(position); tutaj będa te rzeczy potem
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
