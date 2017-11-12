package com.ziamor.incadium.systems.Movement;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.ziamor.incadium.components.Movement.FollowTargetComponent;
import com.ziamor.incadium.components.MonsterComponent;
import com.ziamor.incadium.components.Movement.MovementComponent;
import com.ziamor.incadium.components.Movement.MovementLerpComponent;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.components.TurnComponent;
import com.ziamor.incadium.systems.Util.MapSystem;
import com.ziamor.incadium.utils.DijkstraMap;


public class FollowSystem extends IteratingSystem {
    private MapSystem mapSystem;
    private ComponentMapper<FollowTargetComponent> followTargetComponentMapper;
    private ComponentMapper<TransformComponent> transformComponentMapper;
    private ComponentMapper<MovementComponent> movementComponentMapper;
    private ComponentMapper<TurnComponent> turnComponentMapper;

    private PathFindingSystem pathFindingSystem;

    public FollowSystem() {
        super(Aspect.all(FollowTargetComponent.class, TransformComponent.class, MonsterComponent.class, TurnComponent.class).exclude(MovementLerpComponent.class));
    }

    @Override
    protected void process(int entityId) {
        final FollowTargetComponent followTargetComponent = followTargetComponentMapper.get(entityId);
        final TurnComponent turnComponent = turnComponentMapper.get(entityId);

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

        if (turnComponent.finishedTurn)
            return;

        TransformComponent transformComponent = transformComponentMapper.get(entityId);
        MovementComponent movementComponent = movementComponentMapper.get(entityId);
        int x = (int) transformComponent.x;
        int y = (int) transformComponent.y;

        if (pathFindingSystem.weights != null) {
            Vector2 targetPos = DijkstraMap.gradientDecent(pathFindingSystem.weights, new Vector2(x, y));

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
}
