package com.ziamor.incadium.components.UI;

import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.ziamor.incadium.components.NonComponents.HealthBarUI;

public class HealthBarUIComponent extends Component {
    @EntityId
    public int target = -1;
    public HealthBarUI bar = null;

    public void set(int target, HealthBarUI bar) {
        this.target = target;
        this.bar = bar;
    }
}
