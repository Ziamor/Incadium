package com.ziamor.incadium.systems.Movement;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Rectangle;
import com.ziamor.incadium.components.FactionComponent;
import com.ziamor.incadium.components.MonsterComponent;
import com.ziamor.incadium.components.Movement.MovementComponent;
import com.ziamor.incadium.components.Movement.MovementLerpComponent;
import com.ziamor.incadium.components.Movement.PlayerControllerComponent;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.components.TurnComponent;
import com.ziamor.incadium.systems.Util.MapSystem;

public class MovementSystem extends IteratingSystem {
    MapSystem mapSystem;
    ComponentMapper<MovementComponent> movementComponentComponentMapper;
    ComponentMapper<MovementLerpComponent> movementLerpComponentComponentMapper;
    ComponentMapper<TransformComponent> transformComponentComponentMapper;
    ComponentMapper<TurnComponent> turnComponentComponentMapper;
    private ComponentMapper<FactionComponent> factionComponentMapper;

    private float lerp_life = 0.2f;

    public MovementSystem() {
        super(Aspect.all(MovementComponent.class, TransformComponent.class, TurnComponent.class));
    }

    @Override
    protected void process(int entity) {
        TransformComponent transformComponent = transformComponentComponentMapper.get(entity);
        MovementComponent movementComponent = movementComponentComponentMapper.get(entity);
        TurnComponent turn = turnComponentComponentMapper.get(entity);
        final FactionComponent factionComponent = factionComponentMapper.get(entity);

        if (!turn.finishedTurn) {
            float x_offset = 0;
            float y_offset = 0;
            switch (movementComponent.direction) {
                case NORTH:
                    y_offset += 1;
                    break;
                case SOUTH:
                    y_offset -= 1;
                    break;
                case EAST:
                    x_offset += 1;
                    break;
                case WEST:
                    x_offset -= 1;
                    break;
            }
            if (x_offset != 0 || y_offset != 0) {
                float targetX = transformComponent.x + x_offset;
                float targetY = transformComponent.y + y_offset;
                // Check if the map tile doesn't block
                if (!mapSystem.isBlocking((int) targetX, (int) targetY)) {

                    int target = -1;
                    IntBag monsterIDs = world.getAspectSubscriptionManager().get(Aspect.one(MonsterComponent.class, PlayerControllerComponent.class)).getEntities();
                    for (int i = 0; i < monsterIDs.size(); i++) {
                        int monster = monsterIDs.get(i);
                        TransformComponent entityTransform = transformComponentComponentMapper.get(entity);
                        TransformComponent monsterTransform = transformComponentComponentMapper.get(monster);

                        if (entity == monster || entityTransform == null || monsterTransform == null)
                            continue;
                        //TODO add bounding box component?
                        Rectangle entityRect = new Rectangle(targetX, targetY, 1, 1);
                        Rectangle monsterRect = new Rectangle(monsterTransform.x, monsterTransform.y, 1, 1);
                        if (entityRect.overlaps(monsterRect)) {
                            target = monster;
                            break;
                        }

                    }
                    if (target != -1) {
                        //TODO write this check better
                        FactionComponent monsterFactionComponent = factionComponentMapper.get(target);
                        if (factionComponent != null && monsterFactionComponent != null && factionComponent.factionID != monsterFactionComponent.factionID)
                            E.E(entity).attackTargetComponentTarget(target);
                    } else {
                        movementLerpComponentComponentMapper.create(entity).set(transformComponent.x, transformComponent.y, transformComponent.x + x_offset, transformComponent.y + y_offset, lerp_life);
                        transformComponent.x += x_offset;
                        transformComponent.y += y_offset;
                    }
                    turn.finishedTurn = true;
                }
                movementComponent.direction = MovementComponent.Direction.NONE;
            }
        }
    }
}
