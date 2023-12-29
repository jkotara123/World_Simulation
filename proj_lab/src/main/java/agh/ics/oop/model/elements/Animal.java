package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.maps.AbstractWorldMap;
import agh.ics.oop.model.maps.MoveValidator;
import agh.ics.oop.model.maps.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement,Comparable<Animal> {
    private MapDirection orientation;
    private Vector2d position;
    private final Genome genome;
    private int energy;
    private int lifeSpan;
    private List<Animal> children = new ArrayList<>();

    public Animal(Genome genome,Vector2d position,int energy){
        Random rn = new Random();
        this.genome = genome;
        this.position=position;
        this.orientation=MapDirection.values()[rn.nextInt()%4];
        this.energy = energy;
        this.lifeSpan = 0;
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
        return orientation.toString();
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

    public void move(WorldMap a){
        turn();
        if(a.canMove(this)){
            moveTo(this.position.add(this.orientation.toUnitVector()));
        }
        else{
        Vector2d newPosition= a.nextPosition(this);
        this.moveTo(newPosition);
        }
        genome.nextIndex();

        changeEnergy(-a.getEnergyParameters().energyToMove());
        getOlder();
    }

    public void moveTo(Vector2d position){
        this.position = position;
    }

    public void changeEnergy(int value){
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
}
