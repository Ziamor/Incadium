package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.ziamor.incadium.components.Render.NotVisableComponent;
import com.ziamor.incadium.components.Render.TerrainTileComponent;
import com.ziamor.incadium.components.TransformComponent;

import java.lang.reflect.Array;

public class TerrainRenderSystem extends IteratingSystem {
    ComponentMapper<TransformComponent> transformComponentComponentMapper;
    ComponentMapper<TerrainTileComponent> terrainTileComponentComponentMapper;
    @Wire
    private SpriteBatch batch;

    public TerrainRenderSystem() {
        super(Aspect.all(TransformComponent.class, TerrainTileComponent.class).exclude(NotVisableComponent.class));
    }

    @Override
    protected void process(int e) {
        TerrainTileComponent terrainTileComponent = terrainTileComponentComponentMapper.get(e);
        TransformComponent transformComponent = transformComponentComponentMapper.get(e);

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
