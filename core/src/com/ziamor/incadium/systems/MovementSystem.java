package com.ziamor.incadium.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.EntitySubscription;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Rectangle;
import com.ziamor.incadium.components.AttackTargetComponent;
import com.ziamor.incadium.components.MonsterComponent;
import com.ziamor.incadium.components.MovementComponent;
import com.ziamor.incadium.components.MovementLerpComponent;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.components.TurnComponent;

public class MovementSystem extends IteratingSystem {
    MapSystem mapSystem;
    ComponentMapper<MovementComponent> movementComponentComponentMapper;
    ComponentMapper<MovementLerpComponent> movementLerpComponentComponentMapper;
    ComponentMapper<TransformComponent> transformComponentComponentMapper;
    ComponentMapper<TurnComponent> turnComponentComponentMapper;
    EntitySubscription monsterEntities;

    private float lerp_life = 0.2f;

    public MovementSystem() {
        super(Aspect.all(MovementComponent.class, TransformComponent.class, TurnComponent.class));
    }

    protected IntBag getMonsters() {
        if (monsterEntities == null)
            monsterEntities = world.getAspectSubscriptionManager().get(Aspect.all(MonsterComponent.class));
        return monsterEntities.getEntities();
    }

    @Override
    protected void process(int entity) {
        TransformComponent transformComponent = transformComponentComponentMapper.get(entity);
        MovementComponent movementComponent = movementComponentComponentMapper.get(entity);
        TurnComponent turn = turnComponentComponentMapper.get(entity);

        //if (!turn.finishedTurn)
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
                IntBag monsterIDs = getMonsters();
                for (int monster : monsterIDs.getData()) {
                    TransformComponent entityTransform = transformComponentComponentMapper.get(entity);
                    TransformComponent monsterTransform = transformComponentComponentMapper.get(monster);
                    if (entity == monster || entityTransform == null || monsterTransform == null)
                        continue;
//TODO add counding box xomponent?
                    Rectangle entityRect = new Rectangle(targetX, targetY, 1, 1);
                    Rectangle monsterRect = new Rectangle(monsterTransform.x, monsterTransform.y, 1, 1);
                    if (entityRect.overlaps(monsterRect)) {
                        target = monster;
                        break;
                    }

                }
                if (target != -1) {
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
