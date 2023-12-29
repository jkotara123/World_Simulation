package agh.ics.oop.model.maps;

import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.List;

public record Boundary(Vector2d lowerLeft, Vector2d upperRight) {
    //List<VecallPositions =
    public List<Vector2d> allPositions(){
        List<Vector2d> positions = new ArrayList<>();
        for (int x = lowerLeft.x(); x < upperRight.x()+1; x++) {
            for (int y = lowerLeft.y(); y < upperRight.y()+1; y++) {
                positions.add(new Vector2d(x, y));
            }
        }
        return positions;
    }
}
