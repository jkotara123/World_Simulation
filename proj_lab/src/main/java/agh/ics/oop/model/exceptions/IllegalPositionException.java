package agh.ics.oop.model.exceptions;

import agh.ics.oop.model.Vector2d;

public class IllegalPositionException extends Exception{
    public IllegalPositionException(Vector2d position){
        super("Position "+position+" is either occupied or out of bounds");
    }
}
