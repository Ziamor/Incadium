package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ziamor.incadium.components.Render.NotVisableComponent;
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
    private Vector2 pos;

    public RenderSystem(SpriteBatch batch) {
        super(Aspect.all(TransformComponent.class).one(TextureComponent.class, TextureRegionComponent.class).exclude(NotVisableComponent.class));
        this.batch = batch;
        this.pos = new Vector2();
    }

    @Override
    protected void process(int e) {
        TextureComponent textureComponent = textureComponentComponentMapper.get(e);
        TextureRegionComponent textureRegionComponent = textureRegionComponentComponentMapper.get(e);
        TransformComponent transformComponent = transformComponentComponentMapper.get(e);
        MovementLerpComponent movementLerpComponent = movementLerpComponentComponentMapper.get(e);
        
        // Get the render position
        if (movementLerpComponent != null)
            pos = movementLerpComponent.getCurrentPos(); // The sprite is moving between tiles
        else {
            pos.set(transformComponent.x, transformComponent.y);
        }

        if (textureComponent != null) {
            if (textureComponent.texture == null) {
                Gdx.app.debug("Render System", "Missing texture");
                return;
            }
            batch.draw(textureComponent.texture, pos.x, pos.y, 1, 1);
        }
        if (textureRegionComponent != null) {
            if (textureRegionComponent.region == null) {
                Gdx.app.debug("Render System", "Missing texture region");
                return;
            }
            batch.draw(textureRegionComponent.region, pos.x, pos.y, 1, 1);
        }
    }

    public Comparator<Integer> getComparator() {
        return new Comparator<Integer>() {
            @Override
            public int compare(Integer e1, Integer e2) {
                TransformComponent e1T = transformComponentComponentMapper.get(e1);
                TransformComponent e2T = transformComponentComponentMapper.get(e2);
                if (e1T == null)
                    return -1;
                if (e2T == null)
                    return 1;
                return (int) Math.signum(e1T.z - e2T.z);
            }
        };
    }
}

