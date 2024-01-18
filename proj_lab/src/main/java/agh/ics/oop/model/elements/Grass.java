package agh.ics.oop.model.elements;

import agh.ics.oop.parameters.EnergyParameters;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.maps.Boundary;
import javafx.scene.image.Image;

public class Grass implements WorldElement {
    private final Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;
    }
    @Override
    public Vector2d getPosition() {
        return position;
    }

    public boolean isOnEquator(Boundary equator){
        return (this.position.follows(equator.lowerLeft()) && this.position.precedes(equator.upperRight()));
    }
    @Override
    public String toString() {
        return "*";
    }
    @Override
    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    @Override
    public boolean isAnAnimal() {
        return false;
    }
    public Image toImage(EnergyParameters parameters, int CELLSIZE) {
        return new Image("images/grass.png",CELLSIZE*(float)3/5,CELLSIZE*(float)3/5,false,true);
    }

}
