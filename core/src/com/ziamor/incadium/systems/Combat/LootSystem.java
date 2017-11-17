package com.ziamor.incadium.systems.Combat;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.incadium.ItemFactory;
import com.ziamor.incadium.components.Combat.DeadComponent;
import com.ziamor.incadium.components.Combat.LootableComponent;
import com.ziamor.incadium.components.TransformComponent;

import java.util.Random;

// https://www.codeproject.com/Articles/420046/Loot-Tables-Random-Maps-and-Monsters-Part-I
public class LootSystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> transformComponentMapper;

    public LootSystem() {
        super(Aspect.all(LootableComponent.class, TransformComponent.class, DeadComponent.class));
    }

    @Override
    protected void process(int entityId) {
        final TransformComponent transformComponent = transformComponentMapper.get(entityId);
        Random rand = new Random();
        float num = rand.nextFloat();
        if (num < 0.25)
            ItemFactory.Potion((int) transformComponent.x, (int) transformComponent.y);
        else if (num < 0.5)
            ItemFactory.Sword((int) transformComponent.x, (int) transformComponent.y);
        else if (num < 0.75)
            ItemFactory.Coin((int) transformComponent.x, (int) transformComponent.y);
        else
            ItemFactory.Rune((int) transformComponent.x, (int) transformComponent.y);
    }
}
