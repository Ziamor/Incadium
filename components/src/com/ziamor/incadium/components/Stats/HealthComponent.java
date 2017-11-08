package com.ziamor.incadium.components.Stats;

import com.artemis.Component;

public class HealthComponent extends Component {
    public float maxHealth;
    public float currentHealth;

    public void setHealthStat(float maxHp, float currentHp) {
        this.maxHealth = maxHp;
        this.currentHealth = currentHp;
    }

    public void setMaxHp(float maxHp) {
        this.maxHealth = maxHp;
        this.currentHealth = maxHealth;
    }
}
