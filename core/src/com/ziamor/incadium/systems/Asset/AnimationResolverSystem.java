package com.ziamor.incadium.systems.Asset;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import com.ziamor.incadium.components.Asset.AnimationResolverComponent;
import com.ziamor.incadium.components.Render.AnimationComponent;


public class AnimationResolverSystem extends AssetResolverSystem {
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
            /*TextureRegion[][] splitRegions = TextureRegion.split(texture, texture.getWidth() / animationResolverComponent.numFrameWidth, texture.getHeight() / animationResolverComponent.numFrameHeight);

            Animation<TextureRegion> walkAnimation = new Animation<TextureRegion>(animationResolverComponent.speed, splitRegions[0]);
            Animation<TextureRegion> attackAnimation = new Animation<TextureRegion>(animationResolverComponent.speed, splitRegions[1]);

            ObjectMap<String, Animation<TextureRegion>> animation = new ObjectMap<String, Animation<TextureRegion>>();
            animation.put("walk", walkAnimation);
            animation.put("attack", attackAnimation);*/


            int numFrameWidth = animationResolverComponent.numFrameWidth;
            int numFrameHeight = animationResolverComponent.numFrameHeight;
            int frameWidth = texture.getWidth() / numFrameWidth;
            int frameHeight = texture.getHeight() / numFrameHeight;

            ObjectMap<String, Animation<TextureRegion>> animation = new ObjectMap<String, Animation<TextureRegion>>();
            for (int i = 0; i < animationResolverComponent.animationMetaData.length; i++) {
                int[] frames = animationResolverComponent.animationMetaData[i].frames;
                TextureRegion[] regions = new TextureRegion[frames.length];
                for (int j = 0; j < frames.length; j++) {
                    int x = (frames[j] % numFrameWidth) * frameWidth;
                    int y = (frames[j] / numFrameWidth) * frameHeight;
                    regions[j] = new TextureRegion(texture, x, y, frameWidth, frameHeight);
                }
                Animation<TextureRegion> splitAnimation = new Animation<TextureRegion>(animationResolverComponent.animationMetaData[i].speed, regions);
                animation.put(animationResolverComponent.animationMetaData[i].name, splitAnimation);
            }
            animationComponentMapper.create(entityId).set(animation, 0);
        } else {
            assetManager.load(animationResolverComponent.path, Texture.class);
        }
    }
}
