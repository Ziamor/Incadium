package com.ziamor.incadium.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.ziamor.incadium.components.FollowTargetComponent;
import com.ziamor.incadium.components.MonsterComponent;
import com.ziamor.incadium.components.MovementComponent;
import com.ziamor.incadium.components.MovementLerpComponent;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.utils.DijkstraMap;


public class FollowSystem extends IteratingSystem {
    private MapSystem mapSystem;
    private ComponentMapper<FollowTargetComponent> followTargetComponentMapper;
    private ComponentMapper<TransformComponent> transformComponentMapper;
    private ComponentMapper<MovementComponent> movementComponentMapper;

    public FollowSystem() {
        super(Aspect.all(FollowTargetComponent.class, TransformComponent.class, MonsterComponent.class).exclude(MovementLerpComponent.class));
    }

    @Override
    protected void process(int entityId) {
        FollowTargetComponent followTargetComponent = followTargetComponentMapper.get(entityId);
        if (followTargetComponent.target == -1) {
            E.E(entityId).removeFollowTargetComponent();
            return;
        }
        TransformComponent targetTransformComponent = transformComponentMapper.get(followTargetComponent.target);

        // Make sure the target has a TransforComponent as we need its position
        if (targetTransformComponent == null) {
            E.E(entityId).removeFollowTargetComponent();
            return;
        }

        TransformComponent transformComponent = transformComponentMapper.get(entityId);
        MovementComponent movementComponent = movementComponentMapper.get(entityId);
        int x = (int) transformComponent.x;
        int y = (int) transformComponent.y;

        int[][] weights = DijkstraMap.getDijkstraMap(mapSystem, (int) targetTransformComponent.x, (int) targetTransformComponent.y);
        Vector2 targetPos = DijkstraMap.gradientDecent(weights, new Vector2(x, y));

        if (targetPos.x < x)
            movementComponent.direction = MovementComponent.Direction.WEST;
        else if (targetPos.x > x)
            movementComponent.direction = MovementComponent.Direction.EAST;
        else if (targetPos.y < y)
            movementComponent.direction = MovementComponent.Direction.SOUTH;
        else if (targetPos.y > y)
            movementComponent.direction = MovementComponent.Direction.NORTH;
    }
}
