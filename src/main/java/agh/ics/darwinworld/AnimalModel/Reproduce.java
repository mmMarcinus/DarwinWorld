package agh.ics.darwinworld.AnimalModel;

import agh.ics.darwinworld.SimulationModel.Simulation;

import java.util.HashSet;
import java.util.Random;

public class Reproduce {

    public static void reproduce(Animal parent1, Animal parent2){
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

        //wersja normalna
        HashSet mutatedPlaces = new HashSet<>();
        int mutationCount = rand.nextInt(n+1);
        int newGene;
        String oldGene;

        int i = 0;
        while(i < mutationCount){
            int mutationPlace = rand.nextInt(n);
            if (!mutatedPlaces.contains(mutationPlace)){
                mutatedPlaces.add(mutationPlace);
                do {
                    newGene = rand.nextInt(8);
                    oldGene = youngGenome.substring(newGene, newGene+1);
                } while(Integer.valueOf(oldGene) == newGene);

                char[] youngGenomeChars = youngGenome.toCharArray();
                youngGenomeChars[mutationPlace] = (char) newGene;
                youngGenome = String.valueOf(youngGenomeChars);
                i++;
            }
        }

        //wersja 2

        boolean change = rand.nextBoolean();
        int firstChangePlace;
        int secondChangePlace;

        if (change){
            firstChangePlace = rand.nextInt(n);
            do{
                secondChangePlace = rand.nextInt(n);
            }while(firstChangePlace == secondChangePlace);

            char[] youngGenomeChars = youngGenome.toCharArray();
            char buf = youngGenomeChars[firstChangePlace];
            youngGenomeChars[firstChangePlace] = youngGenomeChars[secondChangePlace];
            youngGenomeChars[secondChangePlace] = buf;
            youngGenome = String.valueOf(youngGenomeChars);
        }

        // tworzenie energii młodego
        // przegadac czy dajemy stala wartosc czy procent energii

        Animal addedAnimal = new Animal(parent1.getPosition(), youngGenome, Simulation.startEnergyLevel, 0);
        Simulation.animals.add(addedAnimal);
        Simulation.worldMap.place(addedAnimal);

        parent1.updateEnergyLevel(parent1.getEnergyLevel()-Simulation.startEnergyLevel);
        parent2.updateEnergyLevel(parent2.getEnergyLevel()-Simulation.startEnergyLevel);

        parent1.updateKidsNumber(parent1.getKidsNumber()+1);
        parent2.updateKidsNumber(parent2.getKidsNumber()+1);
    }
}
