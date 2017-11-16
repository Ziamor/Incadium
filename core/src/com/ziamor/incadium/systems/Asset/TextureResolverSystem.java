package com.ziamor.incadium.systems.Asset;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.ziamor.incadium.components.Asset.TextureResolverComponent;
import com.ziamor.incadium.components.Render.TextureComponent;


public class TextureResolverSystem extends IteratingSystem {
    @Wire
    AssetManager assetManager;
    private ComponentMapper<TextureResolverComponent> textureResolverComponentMapper;
    private ComponentMapper<TextureComponent> textureComponentMapper;

    public TextureResolverSystem() {
        super(Aspect.all(TextureResolverComponent.class).exclude(TextureComponent.class));
    }

    @Override
    protected void process(int entityId) {
        if (assetManager == null) {
            Gdx.app.debug("Texture Resolver System", "Can not load texture, asset manager not initialized");
            return;
        }

        final TextureResolverComponent textureResolverComponent = textureResolverComponentMapper.get(entityId);

        if (textureResolverComponent.path == null || textureResolverComponent.path == "") {
            Gdx.app.debug("Texture Resolver System", "Empty texture name is invalid");
            return;
        }

        if (assetManager.isLoaded(textureResolverComponent.path)) {
            textureComponentMapper.create(entityId).set(assetManager.get(textureResolverComponent.path, Texture.class));
        } else {
            assetManager.load(textureResolverComponent.path, Texture.class);
        }
    }
}
