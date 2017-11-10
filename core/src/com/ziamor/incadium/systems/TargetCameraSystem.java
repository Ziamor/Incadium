package com.ziamor.incadium.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.EntityId;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.ziamor.incadium.components.Movement.MovementLerpComponent;
import com.ziamor.incadium.components.TargetCameraFocusComponent;
import com.ziamor.incadium.components.TransformComponent;


public class TargetCameraSystem extends BaseEntitySystem {
    private OrthographicCamera camera;

    private ComponentMapper<TransformComponent> transformComponentMapper;
    private ComponentMapper<MovementLerpComponent> movementLerpComponentMapper;
    @EntityId
    private int target = -1;

    public TargetCameraSystem(OrthographicCamera camera) {
        super(Aspect.all(TargetCameraFocusComponent.class, TransformComponent.class));
        this.camera = camera;
    }

    @Override
    protected void processSystem() {
        if (target != -1 && camera != null) {
            final TransformComponent transformComponent = transformComponentMapper.get(target);
            final MovementLerpComponent movementLerpComponent = movementLerpComponentMapper.get(target);

            if (movementLerpComponent != null) {
                Vector2 pos = movementLerpComponent.getCurrentPos();
                camera.position.x = pos.x;
                camera.position.y = pos.y;
            } else if (transformComponent != null) {
                camera.position.x = transformComponent.x;
                camera.position.y = transformComponent.y;
            }
        }
    }

    @Override
    protected void inserted(int entityId) {
        super.inserted(entityId);
        target = entityId;
    }

    @Override
    protected void removed(int entityId) {
        super.removed(entityId);
        if (target == entityId)
            target = -1;
    }
}
