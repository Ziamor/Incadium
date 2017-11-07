package com.ziamor.incadium.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.incadium.ItemFactory;
import com.ziamor.incadium.components.DeadComponent;
import com.ziamor.incadium.components.LootableComponent;
import com.ziamor.incadium.components.TransformComponent;


public class LootSystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> transformComponentMapper;

    public LootSystem() {
        super(Aspect.all(LootableComponent.class, TransformComponent.class, DeadComponent.class));
    }

    @Override
    protected void process(int entityId) {
        final TransformComponent transformComponent = transformComponentMapper.get(entityId);
        ItemFactory.Potion((int) transformComponent.x, (int) transformComponent.y);
    }
}
