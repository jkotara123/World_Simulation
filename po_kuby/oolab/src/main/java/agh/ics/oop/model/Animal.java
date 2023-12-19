package agh.ics.oop.model;

import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.enums.MoveDirection;
import agh.ics.oop.model.interfaces.MoveValidator;
import agh.ics.oop.model.interfaces.WorldElement;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;
    static final Vector2d DEFAULT_POSITION=new Vector2d(2,2);

    public Animal(Vector2d position){
        this.position=position;
        this.orientation=MapDirection.NORTH;
    }
    public Animal(){
        this(DEFAULT_POSITION);
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
        return switch(orientation){
            case NORTH -> "N";
            case EAST -> "E";
            case SOUTH -> "S";
            case WEST -> "W";
        };
    }
    @Override
    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }
    public void move(MoveDirection direction, MoveValidator validator){
        switch (direction){
            case LEFT ->  this.orientation=this.orientation.previous();
            case RIGHT -> this.orientation=this.orientation.next();
            case FORWARD -> {
                Vector2d newPosition = this.position.add(this.orientation.toUnitVector());
                if (validator.canMoveTo(newPosition)) this.position = newPosition;
            }
            case BACKWARD -> {
                Vector2d newPosition = this.position.subtract(this.orientation.toUnitVector());
                if (validator.canMoveTo(newPosition))    this.position=newPosition;
            }
        }
    }
}
