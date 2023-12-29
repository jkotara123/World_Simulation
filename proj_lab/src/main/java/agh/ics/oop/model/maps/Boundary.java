package agh.ics.oop.model.maps;

import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class Boundary{
    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    private final List<Vector2d> positions;
    public Boundary(Vector2d lowerLeft, Vector2d upperRight){
        this.lowerLeft=lowerLeft;
        this.upperRight=upperRight;
        this.positions = new ArrayList<>();
        for (int x = lowerLeft.x(); x < upperRight.x()+1; x++) {
            for (int y = lowerLeft.y(); y < upperRight.y()+1; y++) {
                this.positions.add(new Vector2d(x, y));
            }
        }
    }
    public List<Vector2d> allPositions(){
        return this.positions;
    }
    public Vector2d lowerLeft(){
        return this.lowerLeft;
    }
    public Vector2d upperRight(){
        return this.upperRight;
    }
}
