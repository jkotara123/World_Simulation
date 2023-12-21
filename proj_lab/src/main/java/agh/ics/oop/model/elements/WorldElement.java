package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;

public interface WorldElement {
    Vector2d getPosition();
    @Override
    String toString();
    boolean isAt(Vector2d position);
}
