package com.ziamor.incadium.systems.Movement;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ziamor.incadium.components.Combat.AttackTargetComponent;
import com.ziamor.incadium.components.BlockPlayerInputComponent;
import com.ziamor.incadium.components.Movement.MovementComponent;
import com.ziamor.incadium.components.Movement.MovementLerpComponent;
import com.ziamor.incadium.components.Movement.PlayerControllerComponent;

public class PlayerControllerSystem extends IteratingSystem implements GestureDetector.GestureListener {
    private enum TouchArea {
        NONE, UP, DOWN, LEFT, RIGHT
    }

    ComponentMapper<MovementComponent> movementComponentComponentMapper;
    private ComponentMapper<MovementLerpComponent> movementLerpComponentMapper;
    TouchArea touchArea;
    Rectangle upArea, downArea, leftArea, rightArea;

    public PlayerControllerSystem(int width, int height) {
        super(Aspect.all(PlayerControllerComponent.class, MovementComponent.class).exclude(BlockPlayerInputComponent.class, MovementLerpComponent.class, AttackTargetComponent.class));
        upArea = new Rectangle(width * 0.3f, 0, width * 0.3f, height * 0.5f);
        downArea = new Rectangle(width * 0.3f, height * 0.5f, width * 0.3f, height * 0.5f);
        rightArea = new Rectangle(width * 0.6f, 0, width * 0.3f, height);
        leftArea = new Rectangle(0, 0, width * 0.3f, height);
        touchArea = TouchArea.NONE;
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
        if (upArea.contains(x, y))
            touchArea = TouchArea.UP;
        else if (downArea.contains(x, y))
            touchArea = TouchArea.DOWN;
        else if (leftArea.contains(x, y))
            touchArea = TouchArea.LEFT;
        else if (rightArea.contains(x, y))
            touchArea = TouchArea.RIGHT;
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
