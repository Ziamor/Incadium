package com.ziamor.incadium.systems.Movement;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.math.Vector2;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.systems.Util.MapSystem;
import com.ziamor.incadium.utils.DijkstraMap;


public class PathFindingSystem extends BaseEntitySystem {
    private ComponentMapper<TransformComponent> transformComponentMapper;

    private TagManager tagManager;
    private MapSystem mapSystem;

    private float prevX = -1, prevY = -1;

    public int[][] weights;
    public PathFindingSystem() {
        super(Aspect.all());
    }

    @Override
    protected void processSystem() {
        int playerID = tagManager.getEntityId("player");

        if (playerID != -1) {
            final TransformComponent transformComponent = transformComponentMapper.get(playerID);
            if (transformComponent != null) {
                if (transformComponent.x != prevX || transformComponent.y != prevY) {
                    prevX = transformComponent.x;
                    prevY = transformComponent.y;

                    weights = DijkstraMap.getDijkstraMap(mapSystem, (int) transformComponent.x, (int) transformComponent.y);
                }
            }
        }
    }
}
