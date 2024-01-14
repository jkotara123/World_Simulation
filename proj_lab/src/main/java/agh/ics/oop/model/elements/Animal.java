package agh.ics.oop.model.elements;

import agh.ics.oop.SimulationParameters;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.maps.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Animal implements WorldElement,Comparable<Animal> {
    private MapDirection orientation;
    private Vector2d position;
    private final Genome genome;
    private int energy;
    private int lifeSpan;
    private final List<Animal> children = new ArrayList<>();
    private int grassEaten;
    private int deathDay=-1;

    public Animal(Genome genome, Vector2d position, int energy){
        Random rn = new Random();
        this.genome = genome;
        this.position=position;
        this.orientation=MapDirection.values()[rn.nextInt(4)];
        this.energy = energy;
        this.lifeSpan = 0;
    }
    public Animal(Animal animal1, Animal animal2, SimulationParameters simulationParameters){
        this.energy = simulationParameters.energyParameters().energyToReproduce()*2;
        this.position = animal1.getPosition();
        if(Objects.equals(simulationParameters.mutationVariant(), "Lekka korekta")) this.genome = new Variant1Genome(animal1.genome,animal2.genome,animal1.energy,animal2.energy,simulationParameters.minChildrenMutations(),simulationParameters.maxChildrenMutations());
        else this.genome = new DefaultGenome(animal1.genome,animal2.genome,animal1.energy,animal2.energy,simulationParameters.minChildrenMutations(),simulationParameters.maxChildrenMutations());
        Random rn = new Random();
        this.orientation=MapDirection.values()[rn.nextInt(4)];
        this.lifeSpan = 0;
        animal1.changeEnergy(simulationParameters.energyParameters().energyToReproduce());
        animal2.changeEnergy(simulationParameters.energyParameters().energyToReproduce());
        animal1.children.add(this);
        animal2.children.add(this);

    }
    public MapDirection getOrientation() {
        return this.orientation;
    }
    @Override
    public Vector2d getPosition() {
        return position;
    }
    public int getEnergy(){
        return energy;
    }
    public int getLifeSpan(){
        return lifeSpan;
    }
    @Override
    public String toString(){
        return String.valueOf(energy);
    }
    @Override
    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    private void turn(){
        orientation = orientation.turn(genome.getCurrent());
    }

    public void turnAround(){
        orientation=orientation.oppositeDirection();
    }

    public void move(WorldMap map){
        turn();
        if(map.canMove(this)){
            moveTo(this.position.add(this.orientation.toUnitVector()));
        }
        else{
        Vector2d newPosition= map.nextPosition(this);
        this.moveTo(newPosition);
        }
        genome.nextIndex();
        this.changeEnergy(-map.getEnergyParameters().energyToMove());
        getOlder();
    }

    private void moveTo(Vector2d position){
        this.position = position;
    }
    public void eat(int energyFromEating){
        this.grassEaten+=1;
        this.changeEnergy(energyFromEating);
    }

    private void changeEnergy(int value){
        this.energy += value;
    }
    private void getOlder(){
        this.lifeSpan+=1;
    }

    public Genome getGenome(){
        return genome;
    }
    public List<Animal> getChildren(){
        return children;
    }

    public int countDescendants(){
        if (this.children.isEmpty()) return 0;
        int counter = 0;
        for(Animal child : children)    counter+=child.countDescendants();
        return counter;
    }
    @Override
    public int compareTo(Animal other) {
        if(this.energy>other.energy){
            return 1;
        }
        else if (this.energy == other.energy) {
            if(this.lifeSpan > other.lifeSpan){
                return 1;
            }
            else if(this.lifeSpan == other.lifeSpan){
                if(this.children.size()>other.children.size()){
                    return 1;
                }
                else if(this.children.size() == other.children.size()){
                    Random rn = new Random();
                    return rn.nextInt()%2 == 0 ? -1 : 0;
                }
            }
        }
        return -1;
    }
    public void setDeathDay(int day){
        this.deathDay=day;
    }
}
