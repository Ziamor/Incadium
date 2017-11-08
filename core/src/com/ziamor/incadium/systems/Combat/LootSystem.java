package com.ziamor.incadium.systems.Combat;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.incadium.ItemFactory;
import com.ziamor.incadium.components.Combat.DeadComponent;
import com.ziamor.incadium.components.Combat.LootableComponent;
import com.ziamor.incadium.components.TransformComponent;

// https://www.codeproject.com/Articles/420046/Loot-Tables-Random-Maps-and-Monsters-Part-I
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
