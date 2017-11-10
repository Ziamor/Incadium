package com.ziamor.incadium.components.Combat;

import com.artemis.Component;
import com.artemis.annotations.EntityId;


public class AttackTargetComponent extends Component {
    @EntityId
    public int target;

    public void set(int ent) {
        this.target = ent;
    }
}
