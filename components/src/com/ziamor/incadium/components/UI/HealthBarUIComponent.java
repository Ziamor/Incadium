package com.ziamor.incadium.components.UI;

import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.ziamor.incadium.components.NonComponents.HealthBarUI;

public class HealthBarUIComponent extends Component {
    @EntityId
    public int target = -1;

    public void set(int target) {
        this.target = target;
    }
}
