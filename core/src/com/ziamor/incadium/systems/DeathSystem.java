package com.ziamor.incadium.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.ziamor.incadium.components.DeadComponent;
import com.ziamor.incadium.components.HealthComponent;


public class DeathSystem extends IteratingSystem {
    public DeathSystem() {
        super(Aspect.all(DeadComponent.class));
    }

    @Override
    protected void process(int entityId) {
        Gdx.app.log("", "Dead");
        E.E(entityId).deleteFromWorld();
    }
}
