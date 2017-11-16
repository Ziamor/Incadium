package com.ziamor.incadium.systems.Asset;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;


public abstract class AssetResolver extends IteratingSystem {
    @Wire
    AssetManager assetManager;

    protected String logTag = "Asset Resolver";
    public AssetResolver(Aspect.Builder aspect) {
        super(aspect);
    }

    protected abstract void load(int entityId);

    @Override
    protected final void process(int entityId) {
        if (assetManager == null) {
            Gdx.app.debug(logTag, "Can not load texture, asset manager not initialized");
            return;
        }
        load(entityId);
    }
}
