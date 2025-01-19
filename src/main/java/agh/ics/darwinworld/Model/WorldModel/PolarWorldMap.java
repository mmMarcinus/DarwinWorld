package agh.ics.darwinworld.Model.WorldModel;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.AbstractWorldMap;

import java.util.UUID;


public class PolarWorldMap extends AbstractWorldMap {
    public PolarWorldMap(int width, int height, UUID mapID) {
        this.mapID = mapID;

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

        int updatedGene=animal.getCurrentGene();
        if(animal.getCurrentGene()>=animal.getGenome().getLength()-1){
            updatedGene=0;
        }else{
            updatedGene++;
        }
        animal.updateCurrentGene(updatedGene);
    }
}
