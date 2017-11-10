package com.ziamor.incadium.systems.UI;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.incadium.components.NonComponents.HealthBarUI;
import com.ziamor.incadium.components.Stats.HealthComponent;
import com.ziamor.incadium.components.UI.HealthBarUIComponent;


public class HealthBarUISystem extends IteratingSystem {
    private ComponentMapper<HealthBarUIComponent> healthBarUIComponentMapper;
    private ComponentMapper<HealthComponent> healthComponentMapper;

    private HealthBarUI bar;

    public HealthBarUISystem(HealthBarUI healthBarUI) {
        super(Aspect.one(HealthBarUIComponent.class));
        this.bar = healthBarUI;
    }

    @Override
    protected void process(int entityId) {
        final HealthBarUIComponent healthBarComponentUI = healthBarUIComponentMapper.get(entityId);

        if (bar != null && healthBarComponentUI.target != -1) {
            final HealthComponent healthComponent = healthComponentMapper.get(healthBarComponentUI.target);
            if (healthComponent != null) {
                bar.setMax(healthComponent.maxHealth);
                bar.setValue(healthComponent.currentHealth);
            }
        }
    }
}
