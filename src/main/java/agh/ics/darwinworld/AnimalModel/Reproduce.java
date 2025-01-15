package agh.ics.darwinworld.AnimalModel;

import agh.ics.darwinworld.SimulationModel.Simulation;
import agh.ics.darwinworld.Util.Vector2d;

import java.util.HashSet;
import java.util.Random;
import java.util.logging.ConsoleHandler;

public class Reproduce {

    public static Animal reproduce(Animal parent1, Animal parent2, int startEnergyLevel, boolean mutationVariant, int mutations){
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
        boolean change = true;
        while (i < mutations) {
            if (mutationVariant) {
                change = rand.nextBoolean();
            }
            //wersja normalna "pelna losowosc"

            youngGenome.twist(change);
            i++;

        }

        int startGene = rand.nextInt(youngGenome.getLength())-1;

        parent1.updateEnergyLevel(parent1.getEnergyLevel()-startEnergyLevel/2);
        parent2.updateEnergyLevel(parent2.getEnergyLevel()-startEnergyLevel/2);

        parent1.updateKidsNumber(parent1.getKidsNumber()+1);
        parent2.updateKidsNumber(parent2.getKidsNumber()+1);

        UpdateFamilyTree.updateFamilyTree(parent1, parent2, new HashSet<>());

        return new Animal(parent1.getPosition(), youngGenome, startEnergyLevel, 0, parent1, parent2, startGene);

    }
}
