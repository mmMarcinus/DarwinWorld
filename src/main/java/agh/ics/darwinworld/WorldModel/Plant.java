package agh.ics.darwinworld.WorldModel;

import agh.ics.darwinworld.Util.Vector2d;
import agh.ics.darwinworld.World;

public class Plant implements WorldElement{
    private Vector2d position;
    private int energyLevel;

    @Override
    public Vector2d getPosition() {
        return null;
    }

    @Override
    public String toString(){
        return "Plant, " + energyLevel + " of energy";
    }

    public int getEnergyLevel() {
        return energyLevel;
    }
}
