package com.ziamor.incadium.systems.Movement;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziamor.incadium.components.Asset.ShaderResolverComponent;
import com.ziamor.incadium.components.Combat.AttackTargetComponent;
import com.ziamor.incadium.components.Movement.AttackLerpComponent;
import com.ziamor.incadium.components.Movement.MovementComponent;
import com.ziamor.incadium.components.Movement.MovementLerpComponent;
import com.ziamor.incadium.components.Movement.PlayerControllerComponent;
import com.ziamor.incadium.components.Render.RenderPositionComponent;
import com.ziamor.incadium.components.Render.shaders.OutlineShaderComponent;
import com.ziamor.incadium.components.UI.SelectableComponent;
import com.ziamor.incadium.components.UI.SelectedComponent;

public class PlayerControllerSystem extends IteratingSystem implements GestureDetector.GestureListener {
    private enum TouchArea {
        NONE, UP, DOWN, LEFT, RIGHT
    }

    private ComponentMapper<MovementComponent> movementComponentComponentMapper;
    private ComponentMapper<MovementLerpComponent> movementLerpComponentMapper;
    private ComponentMapper<RenderPositionComponent> renderPositionComponentMapper;//TODO remove if we move the select system
    private ComponentMapper<SelectedComponent> selectedComponentMapper;
    private ComponentMapper<ShaderResolverComponent> shaderResolverComponentMapper;
    private ComponentMapper<OutlineShaderComponent> outlineShaderComponentMapper;
    TouchArea touchArea;
    private float width, height;

    Viewport viewport;//TODO remove if we move the select system

    //TODO update resize
    public PlayerControllerSystem(int width, int height, Viewport viewport) {
        super(Aspect.all(PlayerControllerComponent.class, MovementComponent.class).exclude(MovementLerpComponent.class, AttackLerpComponent.class, AttackTargetComponent.class));
        this.viewport = viewport;
        touchArea = TouchArea.NONE;
        this.width = width;
        this.height = height;
    }

    @Override
    protected void process(int entity) {
        MovementComponent movementComponent = movementComponentComponentMapper.get(entity);
        final MovementLerpComponent movementLerpComponent = movementLerpComponentMapper.get(entity);
        if (Gdx.input.isKeyPressed(Input.Keys.W) || touchArea == TouchArea.UP)
            movementComponent.direction = MovementComponent.Direction.NORTH;
        else if (Gdx.input.isKeyPressed(Input.Keys.S) || touchArea == TouchArea.DOWN)
            movementComponent.direction = MovementComponent.Direction.SOUTH;
        else if (Gdx.input.isKeyPressed(Input.Keys.D) || touchArea == TouchArea.RIGHT)
            movementComponent.direction = MovementComponent.Direction.EAST;
        else if (Gdx.input.isKeyPressed(Input.Keys.A) || touchArea == TouchArea.LEFT)
            movementComponent.direction = MovementComponent.Direction.WEST;

        touchArea = TouchArea.NONE;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        //TODO does this really belong in the player controller? Think about making a select system
        // Unselect any entities currently selected
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
                    shaderResolverComponentMapper.create(selectablesIDs.get(i)).set(OutlineShaderComponent.class);
                    return true;
                }

            }
        }

        /*switch (Gdx.app.getType()) {
            case Android:*/
        Vector2 touchPos = new Vector2(x - width / 2, height / 2 - y);
        touchPos.nor();
        if (Math.abs(touchPos.x) >= Math.abs(touchPos.y)) {
            if (touchPos.x >= 0)
                touchArea = TouchArea.RIGHT;
            else
                touchArea = TouchArea.LEFT;
        } else {
            if (touchPos.y >= 0)
                touchArea = TouchArea.UP;
            else
                touchArea = TouchArea.DOWN;
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
