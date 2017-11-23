package com.ziamor.incadium.systems.Asset;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.ziamor.incadium.components.Asset.MapResolverComponent;
import com.ziamor.incadium.components.MapComponent;

//TODO load map correctly
public class MapResolverSystem extends AssetResolverSystem {
    private ComponentMapper<MapComponent> mapComponentMapper;
    private ComponentMapper<MapResolverComponent> mapResolverComponentMapper;

    public MapResolverSystem() {
        super(Aspect.all(MapResolverComponent.class).exclude(MapComponent.class));
    }

    @Override
    protected void load(int entityId) {
        final MapResolverComponent mapResolverComponent = mapResolverComponentMapper.get(entityId);
        mapComponentMapper.create(entityId).set(mapResolverComponent.seed);
    }
}
