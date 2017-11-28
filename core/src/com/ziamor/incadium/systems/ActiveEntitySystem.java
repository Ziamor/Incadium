package com.ziamor.incadium.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.annotations.EntityId;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.IteratingSystem;
import com.ziamor.incadium.components.ActiveEntityComponent;
import com.ziamor.incadium.components.ActiveRangeComponent;
import com.ziamor.incadium.components.Movement.FollowTargetComponent;
import com.ziamor.incadium.components.TransformComponent;


public class ActiveEntitySystem extends IteratingSystem {
    @Wire
    TagManager tagManager;

    private ComponentMapper<TransformComponent> transformComponentMapper;
    private ComponentMapper<ActiveEntityComponent> activeEntityComponentMapper;
    private ComponentMapper<ActiveRangeComponent> activeRangeComponentMapper;
    @EntityId
    protected int playerID = -1;

    protected TransformComponent playerTransformComponent;

    protected int activeRadius = 6;

    public ActiveEntitySystem() {
        super(Aspect.all(ActiveRangeComponent.class));
    }

    @Override
    protected void begin() {
        super.begin();

        //Try to get a player reference
        if (playerID == -1)
            playerID = tagManager.getEntityId("player");

        // Check if we have a player reference
        if (playerID != -1) {
            playerTransformComponent = transformComponentMapper.get(playerID);
        } else
            playerTransformComponent = null;
    }


    @Override
    protected void process(int entityId) {
        final TransformComponent transformComponent = transformComponentMapper.get(entityId);
        final ActiveRangeComponent activeRangeComponent = activeRangeComponentMapper.get(entityId);

        // Can't do anything without the player entity reference
        if (playerID == -1 || playerTransformComponent == null)
            return;

        float x = transformComponent.x - playerTransformComponent.x;
        float y = transformComponent.y - playerTransformComponent.y;

        double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

        if (distance > activeRangeComponent.radius) {
            activeEntityComponentMapper.remove(entityId);
        } else
            activeEntityComponentMapper.create(entityId);
    }
}
