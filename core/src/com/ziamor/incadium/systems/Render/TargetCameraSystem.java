package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.EntityId;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.ziamor.incadium.components.Movement.MovementLerpComponent;
import com.ziamor.incadium.components.Render.RenderPositionComponent;
import com.ziamor.incadium.components.TargetCameraFocusComponent;
import com.ziamor.incadium.components.TransformComponent;


public class TargetCameraSystem extends BaseEntitySystem {
    @Wire
    private OrthographicCamera camera;

    private ComponentMapper<RenderPositionComponent> renderPositionComponentMapper;
    @EntityId
    private int target = -1;

    float offset = 0.5f; // TODO get this from somewhere
    public TargetCameraSystem() {
        super(Aspect.all(TargetCameraFocusComponent.class, RenderPositionComponent.class));
    }

    @Override
    protected void processSystem() {
        if (target != -1 && camera != null) {
            final RenderPositionComponent renderPositionComponent = renderPositionComponentMapper.get(target);
            camera.position.x = renderPositionComponent.x + offset;
            camera.position.y = renderPositionComponent.y + offset;
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
