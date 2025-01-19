package agh.ics.darwinworld.Model.WorldModel;

import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Model.Util.Vector2d;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.AbstractWorldMap;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;


public class NormalWorldMap extends AbstractWorldMap {
    public NormalWorldMap(int width, int height, UUID mapID) {
        this.mapID = mapID;

        this.width = width;
        this.height = height;

        this.jungleTop = (int) ((double) height/2 + height * 0.1);
        this.jungleBottom = (int) ((double) height/2 - height * 0.1);
    }

    @Override
    public synchronized void move(Animal animal, String move, int energyTakenEachDay) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(move, this);
        Vector2d newPosition = animal.getPosition();

        animals.get(oldPosition).remove(animal);
        if (animals.get(newPosition)==null){
            ArrayList<Animal> listToPut = new ArrayList<>();
            listToPut.add(animal);
            animals.put(newPosition, listToPut);
        }else{
            animals.get(newPosition).add(animal);
        }


        animal.updateEnergyLevel(animal.getEnergyLevel() - energyTakenEachDay);

        int updatedGene=animal.getCurrentGene();
        if(animal.getCurrentGene()>=animal.getGenome().getLength()-1){
            updatedGene=0;
        }else{
            updatedGene++;
        }
        animal.updateCurrentGene(updatedGene);
    }

}
