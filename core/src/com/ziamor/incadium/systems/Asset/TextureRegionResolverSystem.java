package com.ziamor.incadium.systems.Asset;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.ziamor.incadium.components.Asset.TextureRegionResolverComponent;
import com.ziamor.incadium.components.Render.TextureRegionComponent;

public class TextureRegionResolverSystem extends AssetResolver {
    private ComponentMapper<TextureRegionResolverComponent> textureRegionResolverComponentMapper;
    private ComponentMapper<TextureRegionComponent> textureRegionComponentMapper;

    public TextureRegionResolverSystem() {
        super(Aspect.all(TextureRegionResolverComponent.class).exclude(TextureRegionComponent.class));
        this.logTag = "Texture Region Resolver System";
    }

    @Override
    protected void load(int entityId) {
        final TextureRegionResolverComponent textureRegionResolverComponent = textureRegionResolverComponentMapper.get(entityId);

        if (textureRegionResolverComponent.path == null || textureRegionResolverComponent.path == "") {
            Gdx.app.debug(logTag, "Empty texture name is invalid");
            return;
        }

        if (assetManager.isLoaded(textureRegionResolverComponent.path)) {
            Texture texture = assetManager.get(textureRegionResolverComponent.path, Texture.class);
            textureRegionComponentMapper.create(entityId).set(texture, textureRegionResolverComponent.px, textureRegionResolverComponent.py, textureRegionResolverComponent.w, textureRegionResolverComponent.h);
        } else {
            assetManager.load(textureRegionResolverComponent.path, Texture.class);
        }
    }
}
