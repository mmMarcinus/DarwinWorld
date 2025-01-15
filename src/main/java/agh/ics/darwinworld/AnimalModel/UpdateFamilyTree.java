package agh.ics.darwinworld.AnimalModel;

import java.util.HashSet;

public class UpdateFamilyTree {

    public static void updateFamilyTree(Animal parent1, Animal parent2, HashSet<Animal> alreadyUpdated) {
        if (parent1 != null && parent2 != null){
            if(!alreadyUpdated.contains(parent1)){
                alreadyUpdated.add(parent1);
                parent1.updateFamilyNumber(parent1.getFamilyNumber()+1);
                updateFamilyTree(parent1.getParent1(), parent1.getParent2(), alreadyUpdated);
            }
            if(!alreadyUpdated.contains(parent2)){
                alreadyUpdated.add(parent2);
                parent2.updateFamilyNumber(parent2.getFamilyNumber()+1);
                updateFamilyTree(parent2.getParent1(), parent2.getParent2(), alreadyUpdated);
            }
        }
    }
}
