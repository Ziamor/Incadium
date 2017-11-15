package com.ziamor.incadium.systems.Combat;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.ziamor.incadium.components.Combat.DeadComponent;
import com.ziamor.incadium.components.Movement.AttackLerpComponent;
import com.ziamor.incadium.components.Movement.MovementLerpComponent;


public class DeathSystem extends IteratingSystem {
    public DeathSystem() {
        super(Aspect.all(DeadComponent.class).exclude(MovementLerpComponent.class, AttackLerpComponent.class));
    }

    @Override
    protected void process(int entityId) {
        Gdx.app.log("", "Dead");
        E.E(entityId).deleteFromWorld();
    }
}
