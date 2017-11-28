package com.ziamor.incadium.systems.UI;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziamor.incadium.components.Asset.ShaderResolverComponent;
import com.ziamor.incadium.components.Render.RenderPositionComponent;
import com.ziamor.incadium.components.Render.shaders.OutlineShaderComponent;
import com.ziamor.incadium.components.UI.SelectableComponent;
import com.ziamor.incadium.components.UI.SelectedComponent;


public class SelectSystem extends BaseEntitySystem implements GestureDetector.GestureListener {

    private ComponentMapper<RenderPositionComponent> renderPositionComponentMapper;//TODO remove if we move the select system
    private ComponentMapper<SelectedComponent> selectedComponentMapper;
    private ComponentMapper<OutlineShaderComponent> outlineShaderComponentMapper;

    Viewport viewport;

    public SelectSystem(Viewport viewport) {
        super(Aspect.all(SelectableComponent.class));
        this.viewport = viewport;
    }

    @Override
    protected void processSystem() {
    }


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        // Un-select any entities currently selected
        IntBag selectedIDs = world.getAspectSubscriptionManager().get(Aspect.one(SelectedComponent.class)).getEntities();
        for (int i = 0; i < selectedIDs.size(); i++) {
            selectedComponentMapper.remove(selectedIDs.get(i));
            outlineShaderComponentMapper.remove(selectedIDs.get(i));
        }
        //Convert the screen space coordinates
        Vector2 screenPos = viewport.unproject(new Vector2(x, y));
        //Find the first selectable entity on mouse pos
        IntBag selectablesIDs = world.getAspectSubscriptionManager().get(Aspect.one(SelectableComponent.class)).getEntities();
        for (int i = 0; i < selectablesIDs.size(); i++) {
            final RenderPositionComponent renderPositionComponent = renderPositionComponentMapper.get(selectablesIDs.get(i));
            if (renderPositionComponent != null) {
                Rectangle entityRect = new Rectangle(renderPositionComponent.x, renderPositionComponent.y, 1, 1);
                if (entityRect.contains(screenPos)) {
                    selectedComponentMapper.create(selectablesIDs.get(i));
                    outlineShaderComponentMapper.create(selectablesIDs.get(i));
                    return true;
                }

            }
        }

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
