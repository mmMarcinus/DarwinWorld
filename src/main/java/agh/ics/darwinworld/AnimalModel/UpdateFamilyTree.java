package agh.ics.darwinworld.AnimalModel;

public class UpdateFamilyTree {

    public static void updateFamilyTree(Animal parent1, Animal parent2){
        if (parent1 != null && parent2 != null){
            parent1.updateFamilyNumber(parent1.getFamilyNumber()+1);
            parent2.updateFamilyNumber(parent2.getFamilyNumber()+1);
            updateFamilyTree(parent1.getParent1(), parent1.getParent2());
            updateFamilyTree(parent2.getParent1(), parent2.getParent2());
        }
    }
}
