package com.ziamor.incadium.systems.Util;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.incadium.components.Movement.MovementLerpComponent;


public class MovementLerpSystem extends IteratingSystem {
    private ComponentMapper<MovementLerpComponent> movementLerpComponentMapper;

    public MovementLerpSystem() {
        super(Aspect.all(MovementLerpComponent.class));
    }

    @Override
    protected void process(int entityId) {
        final MovementLerpComponent movementLerpComponent = movementLerpComponentMapper.get(entityId);

        movementLerpComponent.elapsed += world.getDelta();

        if (movementLerpComponent.isFinished())
            movementLerpComponentMapper.remove(entityId);
    }
}
