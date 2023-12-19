package agh.ics.oop.model.enums;

import agh.ics.oop.model.Vector2d;

public enum MapDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "Północ";
            case SOUTH -> "Południe";
            case EAST -> "Wschód";
            case WEST -> "Zachód";
        };
    }
    public MapDirection next(){
        return MapDirection.values()[(this.ordinal()+1)%4];
    }
    public MapDirection previous(){
        MapDirection[] values = MapDirection.values();
        return values[(this.ordinal()+3)%4];
    }
    public Vector2d toUnitVector(){
        return switch(this){
            case NORTH -> new Vector2d(0,1);
            case EAST -> new Vector2d(1,0);
            case SOUTH -> new Vector2d(0,-1);
            case WEST -> new Vector2d(-1,0);
        };
    }
}
