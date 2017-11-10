package com.ziamor.incadium.components.Combat;


import com.artemis.Component;

public class AttackCoolDownComponent extends Component {
    public float life, elapsed;

    public void set(float life){
        if(life <0)
            this.life = 0;
        else
            this.life = life;

        this.elapsed = 0;
    }
}
