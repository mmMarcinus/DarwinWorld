package agh.ics.darwinworld.Enums;

import agh.ics.darwinworld.Util.Vector2d;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    NORTH_WEST,
    SOUTH,
    SOUTH_EAST,
    SOUTH_WEST,
    EAST,
    WEST;

    public String toString(){
        return switch(this){
            case NORTH -> "N";
            case NORTH_EAST -> "NE";
            case NORTH_WEST -> "NW";
            case SOUTH -> "S";
            case SOUTH_EAST -> "SE";
            case SOUTH_WEST -> "SW";
            case EAST -> "E";
            case WEST -> "W";
        };
    }


    public MapDirection previous() {
        return switch (this) {
            case NORTH -> NORTH_WEST;
            case NORTH_WEST -> WEST;
            case WEST -> SOUTH_WEST;
            case SOUTH_WEST -> SOUTH;
            case SOUTH -> SOUTH_EAST;
            case SOUTH_EAST -> EAST;
            case EAST -> NORTH_EAST;
            case NORTH_EAST -> NORTH;
        };
    }
  
    public MapDirection next(){
        return switch(this){
            case NORTH -> NORTH_EAST;
            case NORTH_EAST -> EAST;
            case EAST -> SOUTH_EAST;
            case SOUTH_EAST -> SOUTH;
            case SOUTH -> SOUTH_WEST;
            case SOUTH_WEST -> WEST;
            case WEST -> NORTH_WEST;
            case NORTH_WEST -> NORTH;
        };
    }

    public Vector2d toUnitVector(){
        return switch(this){
            case NORTH -> new Vector2d(0,1);
            case NORTH_EAST -> new Vector2d(1,1);
            case EAST -> new Vector2d(1,0);
            case SOUTH_EAST -> new Vector2d(1,-1);
            case SOUTH -> new Vector2d(0,-1);
            case SOUTH_WEST -> new Vector2d(-1,-1);
            case WEST -> new Vector2d(-1,0);
            case NORTH_WEST -> new Vector2d(-1,1);
        };
    }
}
