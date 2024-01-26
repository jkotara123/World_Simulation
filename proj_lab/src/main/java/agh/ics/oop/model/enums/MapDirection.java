package agh.ics.oop.model.enums; // czy bycie enumem wystarcza, żeby mieć osobny pakiet?

import agh.ics.oop.model.Vector2d;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    private static final Vector2d NORTH_VECTOR = new Vector2d(0,1); // parametr konstruktora by nie był wygodniejszy?
    private static final Vector2d NORTH_EAST_VECTOR = new Vector2d(1,1);
    private static final Vector2d EAST_VECTOR = new Vector2d(1,0);
    private static final Vector2d SOUTH_EAST_VECTOR = new Vector2d(1,-1);
    private static final Vector2d SOUTH_VECTOR = new Vector2d(0,-1);
    private static final Vector2d SOUTH_WEST_VECTOR = new Vector2d(-1,-1);
    private static final Vector2d WEST_VECTOR = new Vector2d(-1,0);
    private static final Vector2d NORTH_WEST_VECTOR = new Vector2d(-1,1);

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "N";
            case NORTH_EAST -> "NE";
            case EAST -> "E";
            case SOUTH_EAST -> "SE";
            case SOUTH -> "S";
            case SOUTH_WEST -> "SW";
            case WEST -> "W";
            case NORTH_WEST -> "NW";
        };
    }
    public MapDirection turn(int turningValue){
        return MapDirection.values()[(this.ordinal()+turningValue)%8];
    }
    public Vector2d toUnitVector(){
        return switch(this){
            case NORTH -> NORTH_VECTOR;
            case NORTH_EAST -> NORTH_EAST_VECTOR;
            case EAST -> EAST_VECTOR;
            case SOUTH_EAST -> SOUTH_EAST_VECTOR;
            case SOUTH -> SOUTH_VECTOR;
            case SOUTH_WEST -> SOUTH_WEST_VECTOR;
            case WEST -> WEST_VECTOR;
            case NORTH_WEST -> NORTH_WEST_VECTOR;
        };
    }
    public MapDirection oppositeDirection(){
        return this.turn(4);
    }
}
