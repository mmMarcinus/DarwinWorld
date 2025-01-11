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
    private Map<Vector2d,Animal> animals = new HashMap<Vector2d, Animal>;

    WorldMap(int width, int height) {
        this.width = width;
        this.height = height;

        this.jungleTop = (int) ( height /2 + height * 0.1);
        this.jungleBottom = (int) (height/2 - height * 0.1);
    }

    public void place(Animal animal){
        Vector2d position = animal.getPosition();
        if (!animals.containsKey(position)) {
            low_x = Math.min(this.low_x, position.getX());
            low_y = Math.min(this.low_y, position.getY());
            high_x = Math.max(high_x, position.getX());
            high_y = Math.max(high_x, position.getY());
            animals.put(position, animal);

            notifyObservers("Ustawiono nowe zwierze na pozycji " + position.toString());
        }else throw new IncorrectPositionException(position);
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getJungleTop() { return jungleTop; }
    public int getJungleBottom() { return jungleBottom; }
}
