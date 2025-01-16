package agh.ics.darwinworld.Model.WorldModel;

import agh.ics.darwinworld.Model.Util.Vector2d;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.WorldElement;

public class Plant implements WorldElement {
    private Vector2d position;
    private int energyLevel;

    public Plant(Vector2d position, int energyLevel) {
        this.position = position;
        this.energyLevel = energyLevel;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString(){
        return "Plant, " + energyLevel + " of energy";
    }
}
