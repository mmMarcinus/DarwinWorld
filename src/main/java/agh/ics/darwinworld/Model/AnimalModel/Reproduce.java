package agh.ics.darwinworld.Model.AnimalModel;

import java.util.HashSet;
import java.util.Random;

public class Reproduce {

    public static Animal reproduce(Animal parent1, Animal parent2, int startEnergyLevel, int minMutation, int maxMutation){
        Random rand = new Random();
        //tworzenie genomu młodego
        Genome youngGenome;
        boolean side = rand.nextBoolean();
        int firstEnergy = parent1.getEnergyLevel();
        int secondEnergy = parent2.getEnergyLevel();
        Genome firstGenome = parent1.getGenome();
        Genome secondGenome = parent2.getGenome();
        int n = firstGenome.getLength();
        int partition = (firstEnergy / (firstEnergy + secondEnergy)) * n;

        youngGenome = firstGenome.getYoungGenome(secondGenome, partition, side, n);

        //mutacje
        //trzeba dorobic zeby wersja 2 sie robila tylko wtedy gdy uzytkownik tak wybierze w GUI

        int i = 0;
        int mutationsNumber = rand.nextInt(minMutation, maxMutation);
        while (i < mutationsNumber) {
            youngGenome.twist();
            i++;
        }

        int startGene = rand.nextInt(youngGenome.getLength())-1;

        parent1.updateEnergyLevel(parent1.getEnergyLevel()-(startEnergyLevel/3));
        parent2.updateEnergyLevel(parent2.getEnergyLevel()-(startEnergyLevel/3));

        parent1.updateKidsNumber(parent1.getKidsNumber()+1);
        parent2.updateKidsNumber(parent2.getKidsNumber()+1);

        UpdateFamilyTree.updateFamilyTree(parent1, parent2, new HashSet<>());

        return new Animal(parent1.getPosition(), youngGenome, startEnergyLevel, 0, parent1, parent2, startGene);
    }
}
