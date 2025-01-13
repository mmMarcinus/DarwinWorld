package agh.ics.darwinworld.AnimalModel;

import agh.ics.darwinworld.Enums.MapDirection;
import agh.ics.darwinworld.SimulationModel.Simulation;
import agh.ics.darwinworld.Util.Vector2d;
import agh.ics.darwinworld.WorldModel.WorldMap;

import java.util.HashSet;
import java.util.Random;

public class Animal {
    private MapDirection direction;
    private Vector2d position;
    private String genome;
    private int energyLevel;

    public Animal(Vector2d initialPosition, String genome, int basicEnergyLevel) {
        Random rand = new Random();
        this.position = initialPosition;
        this.genome = genome;
        this.energyLevel = basicEnergyLevel;
        this.direction = MapDirection.NORTH;
        int random = rand.nextInt(8);
        for(int i = 0; i < random; i++){
            this.direction = direction.next();
        }
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public MapDirection getOrientation() {
        return this.direction;
    }

    public String getGenome(){
        return this.genome;
    }

    public int getEnergyLevel(){
        return this.energyLevel;
    }

    @Override
    public String toString(){
        return this.direction.toString();
    }

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    public void move(String movement, WorldMap map){
        //movement jest juz charem ktory zawiera informacje o ilosci obrotu w danym dniu
        Vector2d newposition;

        for(int i = 0; i < Integer.valueOf(movement); i++){
            this.direction = direction.next();
        }

        newposition = this.position.add(this.direction.toUnitVector());

        if (map.canMoveTo(newposition)){
            this.position = newposition;
        }

    }

    //consume


    //podajemy animale ktore wczesniej sprawdzamy ze sa zdolne do rozmnazania i stoja na tym samym polu
    public void reproduce(Animal parent1, Animal parent2){
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
        int energyLevel = 10;

        Animal addedAnimal = new Animal(position, youngGenome, energyLevel);
        Simulation.animals.add(addedAnimal);
        Simulation.worldMap.place(addedAnimal);

    }

}
