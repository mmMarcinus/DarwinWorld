package agh.ics.darwinworld.WorldModel;

import agh.ics.darwinworld.AnimalModel.Animal;
import agh.ics.darwinworld.WorldModel.Abstracts.AbstractWorldMap;


public class PolarWorldMap extends AbstractWorldMap {
    public PolarWorldMap(int width, int height) {
        this.width = width;
        this.height = height;

        this.jungleTop = (int) ( (double) height /2 + height * 0.1);
        this.jungleBottom = (int) ((double) height /2 - height * 0.1);
    }

    @Override
    public void move(Animal animal, String move, int energyTakenEachDay) {
        animal.move(move, this);
        //usuwamy energię zgodnie z odległością od bieguna
        int poleHeight = (int) (height * 2 / 10);
        int increasedEnergyDrop = 0;
        if (animal.getPosition().getY() <= poleHeight){
            increasedEnergyDrop = animal.getPosition().getY();
        }
        else if (animal.getPosition().getY() + poleHeight > height){
            increasedEnergyDrop = height - animal.getPosition().getY() + 1;
        }
        increasedEnergyDrop = (int) Math.ceil(increasedEnergyDrop*13/10*energyTakenEachDay);
        animal.updateEnergyLevel(animal.getEnergyLevel() - increasedEnergyDrop);

    }
}
