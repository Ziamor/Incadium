package com.ziamor.incadium.systems.Asset;

import com.artemis.Aspect;
import com.ziamor.incadium.components.Asset.MapResolverComponent;

public class MapResolverSystem extends AssetResolver {
    public MapResolverSystem() {
        super(Aspect.all(MapResolverComponent.class));
    }

    @Override
    protected void load(int entityId) {

    }
}
