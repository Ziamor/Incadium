package com.ziamor.incadium.components.Movement;

import com.artemis.Component;
import com.artemis.annotations.EntityId;

public class FollowTargetComponent extends Component {
    @EntityId
    public int target = -1;

    public void set(int ent) {
        this.target = ent;
    }
}
