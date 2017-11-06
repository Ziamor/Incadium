package com.ziamor.incadium.components;

import com.artemis.Component;

public class FollowTargetComponent extends Component {
    //TODO use link relationships for target
    public int target;

    public void set(int ent) {
        this.target = ent;
    }
}
