package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ziamor.incadium.components.Render.TextureRegionComponent;
import com.ziamor.incadium.components.Movement.MovementLerpComponent;
import com.ziamor.incadium.components.Render.TextureComponent;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.systems.Util.SortedIteratingSystem;

import java.util.Comparator;

public class RenderSystem extends SortedIteratingSystem {
    ComponentMapper<TextureComponent> textureComponentComponentMapper;
    ComponentMapper<TextureRegionComponent> textureRegionComponentComponentMapper;
    ComponentMapper<TransformComponent> transformComponentComponentMapper;
    ComponentMapper<MovementLerpComponent> movementLerpComponentComponentMapper;

    private SpriteBatch batch;

    public RenderSystem(SpriteBatch batch) {
        super(Aspect.all(TransformComponent.class).one(TextureComponent.class, TextureRegionComponent.class));
        this.batch = batch;
    }

    @Override
    protected void process(int e) {
        TextureComponent textureComponent = textureComponentComponentMapper.get(e);
        TextureRegionComponent textureRegionComponent = textureRegionComponentComponentMapper.get(e);
        TransformComponent transformComponent = transformComponentComponentMapper.get(e);
        MovementLerpComponent movementLerpComponent = movementLerpComponentComponentMapper.get(e);

        if (movementLerpComponent != null) {
            Vector2 pos = movementLerpComponent.getCurrentPos();
            batch.draw(textureComponent.texture, pos.x, pos.y, 1, 1);
            if (movementLerpComponent.isFinished())
                movementLerpComponentComponentMapper.remove(e);
        } else {
            if (textureRegionComponent != null)
                batch.draw(textureRegionComponent.region, transformComponent.x, transformComponent.y, 1, 1);
            if (textureComponent != null)
                batch.draw(textureComponent.texture, transformComponent.x, transformComponent.y, 1, 1);
        }

    }

    public Comparator<Integer> getComparator() {
        return new Comparator<Integer>() {
            @Override
            public int compare(Integer e1, Integer e2) {
                return (int) Math.signum(transformComponentComponentMapper.get(e1).z - transformComponentComponentMapper.get(e2).z);
            }
        };
    }
}

