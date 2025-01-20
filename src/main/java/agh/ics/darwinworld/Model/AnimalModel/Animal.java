package agh.ics.darwinworld.Model.AnimalModel;

import agh.ics.darwinworld.Model.Enums.MapDirection;
import agh.ics.darwinworld.Model.Util.Vector2d;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.WorldElement;
import agh.ics.darwinworld.Model.WorldModel.Abstracts.WorldMap;

import java.util.Random;

public class Animal implements WorldElement {
    protected MapDirection direction;
    protected Vector2d position;
    private Genome genome;
    protected int energyLevel;
    private int age;
    private int kidsNumber;
    private int familyNumber;
    private Animal parent1;
    private Animal parent2;
    private int currentGene;
    boolean highlighted;
    int plantsEaten;

    public Animal(Vector2d initialPosition, Genome genome, int basicEnergyLevel, int age, Animal parent1, Animal parent2, int currentGene) {
        Random rand = new Random();
        this.position = initialPosition;
        this.genome = genome;
        this.energyLevel = basicEnergyLevel;
        this.age = age;
        this.direction = MapDirection.NORTH;
        this.kidsNumber = 0;
        this.familyNumber = 0;
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.currentGene = currentGene;
        int random = rand.nextInt(8);
        for(int i = 0; i < random; i++){
            this.direction = direction.next();
        }
        this.highlighted = false;
        this.plantsEaten = 0;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public Genome getGenome(){
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

    public int getFamilyNumber(){
        return this.familyNumber;
    }

    public void updateFamilyNumber(int familyNumber){
        this.familyNumber = familyNumber;
    }

    public Animal getParent1(){
        return this.parent1;
    }

    public Animal getParent2(){
        return this.parent2;
    }

    public int getCurrentGene(){
        return this.currentGene;
    }

    public void updateCurrentGene(int currentGene){
        this.currentGene = currentGene;
    }

    public int getPlantsEaten(){return plantsEaten;}

    public void eatPlant(int energyFromPlant){
        this.updateEnergyLevel(this.getEnergyLevel() + energyFromPlant);
        this.plantsEaten++;
    }

    public boolean isHighlighted(){
        return highlighted;
    }

    public void highlight(){
        highlighted=true;
    }

    public void unhighlight(){
        highlighted=false;
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
        int x = newposition.getX();
        int y = newposition.getY();

//        System.out.println("najpierw: " + newposition);
        if (x < 0){
            newposition = new Vector2d(map.getWidth()-1, y);
//            System.out.println("potem: " + newposition);
        }
        else if (x > map.getWidth()){
            newposition = new Vector2d(0, y);
//            System.out.println("potem: " + newposition);
        }

        System.out.println("najpierw: " + newposition);
        if (y < 0 || y >= map.getHeight()){
            newposition = this.position;
            this.direction = direction.opposite();
            System.out.println("potem: " + newposition);
        }

        this.position = newposition;
    }

}
