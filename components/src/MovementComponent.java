package com.ziamor.incadium.components;

import com.artemis.Component;

public class MovementComponent extends Component {
    public enum Direction {
        NORTH, EAST, SOUTH, WEST, NONE
    }

    public Direction direction = Direction.NONE;

    public void set(Direction dir) {
        this.direction = dir;
    }
}
