package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ziamor.incadium.components.Render.GroundTileComponent;
import com.ziamor.incadium.components.Render.NotVisableComponent;
import com.ziamor.incadium.components.Render.TerrainTileComponent;
import com.ziamor.incadium.components.TransformComponent;

public class TerrainRenderSystem extends IteratingSystem {
    ComponentMapper<TransformComponent> transformComponentComponentMapper;
    ComponentMapper<TerrainTileComponent> terrainTileComponentComponentMapper;
    ComponentMapper<GroundTileComponent> groundTileComponentComponentMapper;

    private SpriteBatch batch;

    public TerrainRenderSystem(SpriteBatch batch) {
        super(Aspect.all(TransformComponent.class).one(TerrainTileComponent.class, GroundTileComponent.class).exclude(NotVisableComponent.class));
        this.batch = batch;
    }

    @Override
    protected void process(int e) {
        TerrainTileComponent terrainTileComponent = terrainTileComponentComponentMapper.get(e);
        TransformComponent transformComponent = transformComponentComponentMapper.get(e);
        GroundTileComponent groundTileComponent = groundTileComponentComponentMapper.get(e);

        if (groundTileComponent != null)
            batch.draw(groundTileComponent.groundTexture, transformComponent.x, transformComponent.y, 1, 1);
        if (terrainTileComponent != null)
            batch.draw(terrainTileComponent.region, transformComponent.x, transformComponent.y, 1, 1);
    }

    @Override
    protected void begin() {
        super.begin();
        batch.begin();
    }

    @Override
    protected void end() {
        super.end();
        batch.end();
    }
}
