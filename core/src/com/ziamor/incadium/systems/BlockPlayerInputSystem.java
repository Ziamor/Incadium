package com.ziamor.incadium.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.TimeUtils;
import com.ziamor.incadium.components.BlockPlayerInputComponent;
import com.ziamor.incadium.components.PlayerControllerComponent;


public class BlockPlayerInputSystem extends IteratingSystem {
    private ComponentMapper<BlockPlayerInputComponent> blockPlayerInputComponentMapper;

    public BlockPlayerInputSystem() {
        super(Aspect.all(PlayerControllerComponent.class, BlockPlayerInputComponent.class));
    }

    @Override
    protected void process(int entityId) {
        BlockPlayerInputComponent blockPlayerInputComponent = blockPlayerInputComponentMapper.get(entityId);
        float elapsed = TimeUtils.timeSinceMillis(blockPlayerInputComponent.startTime) / 1000;
        if (elapsed >= blockPlayerInputComponent.duration)
            E.E(entityId).removeBlockPlayerInputComponent();
    }
}
