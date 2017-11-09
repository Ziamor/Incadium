package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.incadium.components.Render.AnimationComponent;
import com.ziamor.incadium.components.Render.TextureRegionComponent;


public class AnimationSystem extends IteratingSystem {
    private ComponentMapper<AnimationComponent> animationComponentMapper;
    private ComponentMapper<TextureRegionComponent> textureRegionComponentMapper;

    public AnimationSystem() {
        super(Aspect.all(AnimationComponent.class));
    }

    @Override
    protected void process(int entityId) {
        final AnimationComponent animationComponent = animationComponentMapper.get(entityId);
        TextureRegionComponent textureRegionComponent = textureRegionComponentMapper.get(entityId);

        // Make sure the entity has a texture region component
        if (textureRegionComponent == null)
            textureRegionComponent = textureRegionComponentMapper.create(entityId);

        animationComponent.time += world.getDelta();
        textureRegionComponent.region = animationComponent.animation.getKeyFrame(animationComponent.time, true);
    }
}
