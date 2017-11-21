package com.ziamor.incadium.systems.Util;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.incadium.components.Combat.TookDamageComponent;
import com.ziamor.incadium.components.Movement.AttackLerpComponent;
import com.ziamor.incadium.components.Movement.MovementLerpComponent;

public class DurationManagerSystem extends IteratingSystem {
    private ComponentMapper<MovementLerpComponent> movementLerpComponentMapper;
    private ComponentMapper<AttackLerpComponent> attackLerpComponentMapper;

    public DurationManagerSystem() {
        super(Aspect.one(MovementLerpComponent.class, AttackLerpComponent.class));
    }

    @Override
    protected void process(int entityId) {
        final MovementLerpComponent movementLerpComponent = movementLerpComponentMapper.get(entityId);
        final AttackLerpComponent attackLerpComponent = attackLerpComponentMapper.get(entityId);

        if (movementLerpComponent != null) {
            movementLerpComponent.elapsed += world.getDelta();

            if (movementLerpComponent.isFinished())
                movementLerpComponentMapper.remove(entityId);
        }

        if (attackLerpComponent != null) {
            attackLerpComponent.elapsed += world.getDelta();

            if (attackLerpComponent.isFinished())
                attackLerpComponentMapper.remove(entityId);
        }
    }
}
