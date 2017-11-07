package com.ziamor.incadium.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.ziamor.incadium.components.DeadComponent;
import com.ziamor.incadium.components.HealthComponent;


public class DeathSystem extends IteratingSystem {
private ComponentMapper<HealthComponent> healthComponentMapper;
    public DeathSystem() {
        super(Aspect.all(HealthComponent.class));
    }

    @Override
    protected void process(int entityId) {
        HealthComponent healthComponent = healthComponentMapper.get(entityId);

        if (healthComponent.currentHealth < 0) {
            Gdx.app.log("", "Dead");
            E.E(entityId).deadComponent();
            E.E(entityId).deleteFromWorld();
            //engine.removeEntity(entity);
        }
    }
}
