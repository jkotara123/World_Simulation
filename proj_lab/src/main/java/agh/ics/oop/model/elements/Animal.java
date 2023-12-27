package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.maps.MoveValidator;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;
    private final Genome genome;

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
}
