package com.ziamor.incadium.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.ziamor.incadium.components.PlayerControllerComponent;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.components.TurnComponent;


public class PlayerStateSystem extends BaseEntitySystem {
    Entity player = null;
    private ComponentMapper<TurnComponent> turnComponentMapper;
    private ComponentMapper<TransformComponent> transformComponentMapper;

    public PlayerStateSystem() {
        super(Aspect.all(PlayerControllerComponent.class));
    }

    @Override
    protected void processSystem() {
        if (player == null) {
            player = world.getSystem(TagManager.class).getEntity("player");
        }
        final TurnComponent turnComponent = turnComponentMapper.get(player);
        final TransformComponent transformComponent = transformComponentMapper.get(player);
    }
}
