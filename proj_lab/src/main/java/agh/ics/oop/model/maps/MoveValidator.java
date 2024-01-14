package agh.ics.oop.model.maps;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;

public interface MoveValidator {

    boolean canMove(Animal animal);
    Vector2d nextPosition(Animal animal);
}
