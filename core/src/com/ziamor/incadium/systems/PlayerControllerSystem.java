package com.ziamor.incadium.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ziamor.incadium.IncadiumGame;
import com.ziamor.incadium.components.AttackTargetComponent;
import com.ziamor.incadium.components.BlockPlayerInputComponent;
import com.ziamor.incadium.components.MovementComponent;
import com.ziamor.incadium.components.MovementLerpComponent;
import com.ziamor.incadium.components.PlayerControllerComponent;

public class PlayerControllerSystem extends IteratingSystem {
    ComponentMapper<MovementComponent> movementComponentComponentMapper;

    public PlayerControllerSystem() {
        super(Aspect.all(PlayerControllerComponent.class, MovementComponent.class).exclude(BlockPlayerInputComponent.class, MovementLerpComponent.class, AttackTargetComponent.class));
    }

    @Override
    protected void process(int entity) {
        MovementComponent movementComponent = movementComponentComponentMapper.get(entity);

        if (Gdx.input.isKeyPressed(Input.Keys.W))
            movementComponent.direction = MovementComponent.Direction.NORTH;
        else if (Gdx.input.isKeyPressed(Input.Keys.S))
            movementComponent.direction = MovementComponent.Direction.SOUTH;
        else if (Gdx.input.isKeyPressed(Input.Keys.D))
            movementComponent.direction = MovementComponent.Direction.EAST;
        else if (Gdx.input.isKeyPressed(Input.Keys.A))
            movementComponent.direction = MovementComponent.Direction.WEST;
    }
}
