package agh.ics.darwinworld.AnimalModel;

import agh.ics.darwinworld.SimulationModel.Simulation;

import java.util.HashSet;
import java.util.Random;

public class Reproduce {

    public static Animal reproduce(Animal parent1, Animal parent2, int startEnergyLevel, boolean mutationVariant, int mutations){
        Random rand = new Random();

        //tworzenie genomu młodego
        String youngGenome;
        boolean side = rand.nextBoolean();
        int firstEnergy = parent1.getEnergyLevel();
        int secondEnergy = parent2.getEnergyLevel();
        String firstGenome = parent1.getGenome();
        String secondGenome = parent2.getGenome();
        int n = firstGenome.length();
        int partition = (firstEnergy / (firstEnergy + secondEnergy)) * n;

        if(side){
            youngGenome = firstGenome.substring(0, partition) + secondGenome.substring(partition, n);
        }
        else{
            youngGenome = secondGenome.substring(0,partition) + firstGenome.substring(partition, n);
        }

        //mutacje
        //trzeba dorobic zeby wersja 2 sie robila tylko wtedy gdy uzytkownik tak wybierze w GUI

        int i = 0;
        boolean change = true;
        while (i < mutations) {
            if (mutationVariant) {
                change = rand.nextBoolean();
            }
            //wersja normalna "pelna losowosc"
            if (change) {
                int newGene;
                String oldGene;
                int mutationPlace = rand.nextInt(n);
                do {
                    newGene = rand.nextInt(8);
                    oldGene = youngGenome.substring(newGene, newGene + 1);
                } while (Integer.valueOf(oldGene) == newGene);

                char[] youngGenomeChars = youngGenome.toCharArray();
                youngGenomeChars[mutationPlace] = (char) newGene;
                youngGenome = String.valueOf(youngGenomeChars);
                i++;
            }
            //wersja 2 "podmianka"
            else {
                int firstChangePlace;
                int secondChangePlace;

                firstChangePlace = rand.nextInt(n);
                do {
                    secondChangePlace = rand.nextInt(n);
                } while (firstChangePlace == secondChangePlace);

                char[] youngGenomeChars = youngGenome.toCharArray();
                char buf = youngGenomeChars[firstChangePlace];
                youngGenomeChars[firstChangePlace] = youngGenomeChars[secondChangePlace];
                youngGenomeChars[secondChangePlace] = buf;
                youngGenome = String.valueOf(youngGenomeChars);
            }
        }


        parent1.updateEnergyLevel(parent1.getEnergyLevel()-startEnergyLevel/2);
        parent2.updateEnergyLevel(parent2.getEnergyLevel()-startEnergyLevel/2);

        parent1.updateKidsNumber(parent1.getKidsNumber()+1);
        parent2.updateKidsNumber(parent2.getKidsNumber()+1);

        return new Animal(parent1.getPosition(), youngGenome, startEnergyLevel, 0, parent1, parent2);
        //UpdateFamilyTree.updateFamilyTree(parent1, parent2); nie dziala bo moze sie zapetlic w nieskonczonosc jakby ktorys sie rozmnozyl z dziadkiem albo dalej w drzewie

    }
}
