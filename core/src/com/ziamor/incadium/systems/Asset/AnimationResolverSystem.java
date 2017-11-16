package com.ziamor.incadium.systems.Asset;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import com.ziamor.incadium.components.Asset.AnimationResolverComponent;
import com.ziamor.incadium.components.Render.AnimationComponent;


public class AnimationResolverSystem extends AssetResolver {
    @Wire
    AssetManager assetManager;
    private ComponentMapper<AnimationResolverComponent> animationResolverComponentMapper;
    private ComponentMapper<AnimationComponent> animationComponentMapper;

    public AnimationResolverSystem() {
        super(Aspect.all(AnimationResolverComponent.class).exclude(AnimationComponent.class));
        this.logTag = "Animation Resolver System";
    }

    @Override
    protected void load(int entityId) {

        final AnimationResolverComponent animationResolverComponent = animationResolverComponentMapper.get(entityId);
        if (animationResolverComponent.path == null || animationResolverComponent.path == "") {
            Gdx.app.debug(logTag, "Empty animation path");
            return;
        }

        if (assetManager.isLoaded(animationResolverComponent.path)) {
            Texture texture = assetManager.get(animationResolverComponent.path);
            TextureRegion[][] splitRegions = TextureRegion.split(texture, texture.getWidth() / animationResolverComponent.numFrameWidth, texture.getHeight() / animationResolverComponent.numFrameHeight);

            Animation<TextureRegion> walkAnimation = new Animation<TextureRegion>(animationResolverComponent.speed, splitRegions[0]);
            Animation<TextureRegion> attackAnimation = new Animation<TextureRegion>(animationResolverComponent.speed, splitRegions[1]);

            ObjectMap<String, Animation<TextureRegion>> animation = new ObjectMap<String, Animation<TextureRegion>>();
            animation.put("walk", walkAnimation);
            animation.put("attack", attackAnimation);

            animationComponentMapper.create(entityId).set(animation, 0);
        } else {
            assetManager.load(animationResolverComponent.path, Texture.class);
        }
    }
}
