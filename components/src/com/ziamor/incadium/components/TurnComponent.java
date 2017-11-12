package com.ziamor.incadium.components;

import com.artemis.Component;

public class TurnComponent extends Component {
    public boolean finishedTurn;
    public float executionTime = 0, movementSystemTime = 0;
    public int totalSystemVisit = 0, totalMovementSystemVisit = 0;
}
