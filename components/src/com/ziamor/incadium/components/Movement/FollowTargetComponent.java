package com.ziamor.incadium.components.Movement;

import com.artemis.Component;

public class FollowTargetComponent extends Component {
    //TODO use link relationships for target
    public int target = -1;

    public void set(int ent) {
        this.target = ent;
    }
}
