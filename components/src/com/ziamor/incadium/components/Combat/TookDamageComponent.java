package com.ziamor.incadium.components.Combat;

import com.artemis.Component;


public class TookDamageComponent extends Component {
    public float life;
    public float elapsed;

    public void set(float life) {
        this.life = life;
    }
}
