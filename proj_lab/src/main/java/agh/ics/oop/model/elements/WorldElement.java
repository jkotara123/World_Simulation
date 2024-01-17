package agh.ics.oop.model.elements;

import agh.ics.oop.EnergyParameters;
import agh.ics.oop.model.Vector2d;
import javafx.scene.image.Image;

public interface WorldElement {
    Vector2d getPosition();
    @Override
    String toString();
    boolean isAt(Vector2d position);
    boolean isAnAnimal();
    Image toImage(EnergyParameters parameters, int CELLSIZE);
}
