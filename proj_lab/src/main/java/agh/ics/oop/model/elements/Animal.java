package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.maps.MoveValidator;

import java.util.List;
import java.util.Random;

public class Animal implements WorldElement,Comparable<Animal> {
    private MapDirection orientation;
    private Vector2d position;
    private final Genome genome;
    //te ponizej do konstruktora
    private int energy;
    private int lifeSpan;
    private List<Animal> children;

    static final Vector2d DEFAULT_POSITION=new Vector2d(2,2);

    public Animal(Genome genome,Vector2d position){
        this.genome = genome;
        this.position=position;
        this.orientation=MapDirection.NORTH; // to ma być losowe
    }
    public Animal(Genome genome){
        this(genome,DEFAULT_POSITION);
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

    public void turn(){
        orientation = orientation.turn(genome.getCurrent());
    }


    public void move(MoveValidator validator){
        turn(); // mozna to zlaczyc z nextIndex w sumie chyba ze bedziemy gdzies uzywac samego turn
        genome.nextIndex();
        Vector2d newPosition = this.position.add(this.orientation.toUnitVector());
        if (validator.canMoveTo(newPosition)) {
            this.position = newPosition;
        }
    }
    public void eat(Grass grass){
        /*
        myslalem w ten sposob, ze moze np trawa by przetrzymywala ile ma energii
         */
        this.energy += grass.getEnergy();
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
