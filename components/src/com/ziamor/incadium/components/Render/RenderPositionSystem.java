package com.ziamor.incadium.components.Render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.ziamor.incadium.components.Movement.AttackLerpComponent;
import com.ziamor.incadium.components.Movement.MovementLerpComponent;
import com.ziamor.incadium.components.TransformComponent;


public class RenderPositionSystem extends IteratingSystem {
    private ComponentMapper<RenderPositionComponent> renderPositionComponentMapper;
    private ComponentMapper<TransformComponent> transformComponentMapper;
    private ComponentMapper<MovementLerpComponent> movementLerpComponentComponentMapper;
    private ComponentMapper<AttackLerpComponent> attackLerpComponentMapper;

    public RenderPositionSystem() {
        super(Aspect.all(RenderPositionComponent.class, TransformComponent.class));
    }

    @Override
    protected void process(int entityId) {
        final RenderPositionComponent renderPositionComponent = renderPositionComponentMapper.get(entityId);
        final TransformComponent transformComponent = transformComponentMapper.get(entityId);
        final MovementLerpComponent movementLerpComponent = movementLerpComponentComponentMapper.get(entityId);
        final AttackLerpComponent attackLerpComponent = attackLerpComponentMapper.get(entityId);

        Vector2 pos;
        // Get the render position
        if (movementLerpComponent != null)
            pos = movementLerpComponent.getCurrentPos(); // The sprite is moving between tiles
        else if (attackLerpComponent != null)
            pos = attackLerpComponent.getCurrentPos(); // The sprite is moving between tiles
        else {
            pos = new Vector2(transformComponent.x, transformComponent.y);
        }

        renderPositionComponent.set(pos.x, pos.y, transformComponent.z);
    }
}
