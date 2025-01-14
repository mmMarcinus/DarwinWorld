package agh.ics.darwinworld.WorldModel;

import agh.ics.darwinworld.Util.Vector2d;
import agh.ics.darwinworld.World;

public class Plant implements WorldElement{
    private Vector2d position;

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString(){
        return "Plant, " + energyLevel + " of energy";
    }
}
