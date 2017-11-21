package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.incadium.components.Render.RenderPositionComponent;
import com.ziamor.incadium.components.Render.TextureComponent;
import com.ziamor.incadium.components.Render.TextureRegionComponent;
import com.ziamor.incadium.components.TransformComponent;


public class RenderPositionInitSystem extends IteratingSystem {
    private ComponentMapper<RenderPositionComponent> renderPositionComponentMapper;

    public RenderPositionInitSystem() {
        super(Aspect.all(TransformComponent.class).one(TextureComponent.class, TextureRegionComponent.class).exclude(RenderPositionComponent.class));
    }

    @Override
    protected void process(int entityId) {
        renderPositionComponentMapper.create(entityId);
    }
}
