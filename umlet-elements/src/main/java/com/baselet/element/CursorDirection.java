package com.baselet.element;

import com.baselet.control.enums.Direction;
import com.baselet.element.interfaces.CursorOwn;

import java.util.Arrays;
import java.util.Collection;

public class CursorDirection {
    private CursorOwn cursor;
    private Direction[] directions;

    public CursorDirection() {

    }

    public CursorDirection(CursorOwn cursor, Direction ... directions) {
        this.cursor = cursor;
        this.directions = directions;
    }

    public CursorOwn getCursor() {
        return cursor;
    }

    public void setCursor(CursorOwn cursor) {
        this.cursor = cursor;
    }

    public Direction[] getDirections() {
        return directions;
    }

    public void setDirections(Direction[] directions) {
        this.directions = directions;
    }

    public boolean contains(Collection<Direction> directions) {
        return directions.containsAll(Arrays.asList(this.directions));
    }
}
