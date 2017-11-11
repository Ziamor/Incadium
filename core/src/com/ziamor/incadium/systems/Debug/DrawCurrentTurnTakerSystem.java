package com.ziamor.incadium.systems.Debug;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.components.TurnComponent;


public class DrawCurrentTurnTakerSystem extends IteratingSystem {
    ShapeRenderer shapeRenderer;
    private ComponentMapper<TransformComponent> transformComponentMapper;

    public DrawCurrentTurnTakerSystem(ShapeRenderer shapeRenderer) {
        super(Aspect.all(TurnComponent.class, TurnComponent.class));
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    protected void process(int entityId) {
        final TransformComponent transformComponent = transformComponentMapper.get(entityId);
        shapeRenderer.setColor(1, 1, 0, 1);
        shapeRenderer.rect(transformComponent.x, transformComponent.y, 1, 1);
    }

    @Override
    protected void begin() {
        super.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    }

    @Override
    protected void end() {
        super.end();
        shapeRenderer.end();
    }
}
