package com.ziamor.incadium.systems.Stats;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.systems.IteratingSystem;
import com.ziamor.incadium.components.Stats.HealthComponent;


public class HealthSystem extends IteratingSystem {
    private ComponentMapper<HealthComponent> healthComponentMapper;

    public HealthSystem() {
        super(Aspect.all(HealthComponent.class));
    }

    @Override
    protected void process(int entityId) {
        HealthComponent healthComponent = healthComponentMapper.get(entityId);

        // Set entities with 0 health as dead
        if (healthComponent.currentHealth <= 0)
            E.E(entityId).deadComponent();
    }
}
