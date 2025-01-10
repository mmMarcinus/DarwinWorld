package agh.ics.darwinworld.AnimalModel;

import agh.ics.darwinworld.Enums.MapDirection;
import agh.ics.darwinworld.Util.Vector2d;
import agh.ics.darwinworld.WorldModel.WorldMap;

public class Animal {
    private MapDirection direction;
    private Vector2d position;
    private String genome;
    private int energy_level;

    public Animal(Vector2d initialPosition) {
        this.position = initialPosition;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public MapDirection getOrientation() {
        return this.direction;
    }

    @Override
    public String toString(){
        return this.direction.toString();
    }

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    public void move(String movement, WorldMap map){
        Vector2d newposition;

        for(int i = 0; i < Integer.valueOf(movement); i++){
            this.direction = direction.next();
        }

        newposition = this.position.add(this.direction.toUnitVector());

        if (map.canMoveTo(newposition)){
            this.position = newposition;
        }
    }
}
