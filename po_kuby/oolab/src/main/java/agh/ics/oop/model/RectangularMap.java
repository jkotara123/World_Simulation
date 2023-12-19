package agh.ics.oop.model;

import agh.ics.oop.model.interfaces.WorldMap;

public class RectangularMap extends AbstractWorldMap implements WorldMap {
    private final Boundary bounds;
    public RectangularMap(int width,int height,int mapID){
        super(mapID);
        this.bounds=new Boundary(new Vector2d(0,0),new Vector2d(width-1,height-1));
    }
    @Override
    public boolean canMoveTo(Vector2d position) {
        return super.canMoveTo(position) && position.follows(bounds.lowerLeft()) && position.precedes(bounds.upperRight());
    }
    @Override
    public Boundary getCurrentBounds() {
        return bounds;
    }
}
