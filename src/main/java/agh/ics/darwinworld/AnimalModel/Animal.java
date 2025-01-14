package agh.ics.darwinworld.AnimalModel;

import agh.ics.darwinworld.Enums.MapDirection;
import agh.ics.darwinworld.SimulationModel.Simulation;
import agh.ics.darwinworld.Util.Vector2d;
import agh.ics.darwinworld.WorldModel.WorldElement;
import agh.ics.darwinworld.WorldModel.WorldMap;

import java.util.HashSet;
import java.util.Random;

public class Animal implements WorldElement {
    private MapDirection direction;
    private Vector2d position;
    private String genome;
    private int energyLevel;
    private int age;
    private int kidsNumber;

    public Animal(Vector2d initialPosition, String genome, int basicEnergyLevel, int age) {
        Random rand = new Random();
        this.position = initialPosition;
        this.genome = genome;
        this.energyLevel = basicEnergyLevel;
        this.age = age;
        this.direction = MapDirection.NORTH;
        this.kidsNumber = 0;
        int random = rand.nextInt(8);
        for(int i = 0; i < random; i++){
            this.direction = direction.next();
        }
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public String getGenome(){
        return this.genome;
    }

    public int getEnergyLevel(){
        return this.energyLevel;
    }

    public void updateEnergyLevel(int newEnergyLevel){
        this.energyLevel = newEnergyLevel;
    }

    public int getAge(){
        return this.age;
    }

    public void updateAge(int age){
        this.age = age;
    }

    public int getKidsNumber(){
        return this.kidsNumber;
    }

    public void updateKidsNumber(int kidsNumber){
        this.kidsNumber = kidsNumber;
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

        this.position = newposition;
        
    }

}
