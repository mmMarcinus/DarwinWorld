package agh.ics.darwinworld.AnimalModel;

import agh.ics.darwinworld.Enums.MapDirection;
import agh.ics.darwinworld.Util.Vector2d;

public class Animal {
    private MapDirection orientation;
    private Vector2d position;
    private String genome;
    private int energy_level;

    public Animal(Vector2d initialPosition) {
        this.position = initialPosition;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public MapDirection getOrientation() {
        return this.orientation;
    }

    @Override
    public String toString(){
        return this.orientation.toString();
    }

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    public void move(MapDirection direction, WorldMap map){
        Vector2d newposition;

        switch (direction){
            case RIGHT -> this.orientation = this.orientation.next();
            case LEFT -> this.orientation = this.orientation.previous();
            case FORWARD -> {
                newposition = this.position.add(this.orientation.toUnitVector());
                if (map.canMoveTo(newposition)){
                    this.position = newposition;
                }
            }
            case BACKWARDS -> {
                newposition = this.position.subtract(this.orientation.toUnitVector());
                if (map.canMoveTo(newposition)){
                    this.position = newposition;
                }
            }
        }
    }
}
