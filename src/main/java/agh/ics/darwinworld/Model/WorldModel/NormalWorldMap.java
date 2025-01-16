package agh.ics.darwinworld.Model.WorldModel;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.AbstractWorldMap;


public class NormalWorldMap extends AbstractWorldMap {
    public NormalWorldMap(int width, int height) {
        this.width = width;
        this.height = height;

        this.jungleTop = (int) ( height /2 + height * 0.1);
        this.jungleBottom = (int) (height/2 - height * 0.1);
    }

    @Override
    public void move(Animal animal, String move, int energyTakenEachDay) {
        //tutaj ruszanie na mapie musi byc zawarte
        animal.move(move, this);
        animal.updateEnergyLevel(animal.getEnergyLevel() - energyTakenEachDay);
    }

}
