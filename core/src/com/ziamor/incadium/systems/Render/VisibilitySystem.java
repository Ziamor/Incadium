package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.annotations.EntityId;
import com.artemis.managers.TagManager;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.ziamor.incadium.components.Render.GroundTileComponent;
import com.ziamor.incadium.components.Render.TerrainTileComponent;
import com.ziamor.incadium.components.Render.TextureComponent;
import com.ziamor.incadium.components.Render.TextureRegionComponent;
import com.ziamor.incadium.components.TransformComponent;


public class VisibilitySystem extends IteratingSystem {
    @EntityId
    protected int playerID = -1;
    protected float renderRadius;
    private TagManager tagManager;
    private TransformComponent playerTransformComponent;

    private ComponentMapper<TransformComponent> transformComponentMapper;

    public VisibilitySystem(float renderRadius) {
        super(Aspect.all(TransformComponent.class).one(TextureComponent.class, TextureRegionComponent.class, TerrainTileComponent.class, GroundTileComponent.class));
        this.renderRadius = renderRadius;
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

        // Can't do anything without the player entity reference
        if (playerID == -1 || playerTransformComponent == null)
            return;

        float x = transformComponent.x - playerTransformComponent.x;
        float y = transformComponent.y - playerTransformComponent.y;

        double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

        if (distance > renderRadius) {
            E.E(entityId).notVisableComponent();
        } else
            E.E(entityId).removeNotVisableComponent();
    }
}
