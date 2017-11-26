package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.ziamor.incadium.components.Render.GroundTileComponent;
import com.ziamor.incadium.components.Render.NotVisableComponent;
import com.ziamor.incadium.components.TransformComponent;

import java.lang.reflect.Array;

public class GroundRenderSystem extends IteratingSystem {
    ComponentMapper<TransformComponent> transformComponentComponentMapper;
    ComponentMapper<GroundTileComponent> groundTileComponentComponentMapper;
    @Wire
    private SpriteBatch batch;
    public GroundRenderSystem() {
        super(Aspect.all(TransformComponent.class, GroundTileComponent.class).exclude(NotVisableComponent.class));
    }

    @Override
    protected void process(int e) {
        TransformComponent transformComponent = transformComponentComponentMapper.get(e);
        GroundTileComponent groundTileComponent = groundTileComponentComponentMapper.get(e);

        batch.draw(groundTileComponent.groundTexture, transformComponent.x, transformComponent.y, 1, 1);
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
