package com.ziamor.incadium.systems.Util;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Queue;
import com.ziamor.incadium.components.TurnComponent;
import com.ziamor.incadium.components.TurnTakerComponent;


public class TurnSchedulerSystem extends EntitySystem {
    public int currentTurnTaker = -1;
    private Queue<Entity> turnEntities;
    private ComponentMapper<TurnComponent> turnComponentMapper;
    private ComponentMapper<TurnTakerComponent> turnTakerComponentMapper;
    public TurnSchedulerSystem() {
        super(Aspect.all(TurnTakerComponent.class));
        turnEntities = new Queue<Entity>();
    }

    @Override
    public void removed(Entity e) {
        //processSystem will remove entities with no turn component. That includes removed entities by the world
        if (e.getId() == currentTurnTaker)
            currentTurnTaker = -1;
    }

    @Override
    public void inserted(Entity e) {
        turnEntities.addLast(e);
    }

    @Override
    protected void processSystem() {
        // Currently no entity is taking their turn
        if (currentTurnTaker == -1) {
            // There are entities in the queue ready to take their turn
            if (turnEntities.size > 0) {
                Entity nextTurnTaker = turnEntities.removeFirst();
                TurnTakerComponent turnTakerComponent = turnTakerComponentMapper.get(nextTurnTaker);
                // If the entity has a TurnTakerComponent, which it should, make it the current turn taker
                if (turnTakerComponent != null) {
                    currentTurnTaker = nextTurnTaker.getId();
                    E.E(currentTurnTaker).turnComponent();
                } else {
                    Gdx.app.debug("Turn Scheduler", "Current entity is missing its turn taker");
                    return; // Something went wrong
                }
            } else {
                Gdx.app.debug("Turn Scheduler", "The turn queue is empty");
                return; // There are no entites that need to take their turn;
            }
        }

        TurnComponent turnComponent = turnComponentMapper.get(currentTurnTaker);

        // Make sure the current entity is taking their turn
        if (turnComponent == null) {
            Gdx.app.debug("Turn Scheduler", "Current entity is missing its turn component");
            currentTurnTaker = -1;
            return; // The entity does not have a turn component so we don't need to process it
        }

        turnComponent.executionTime += world.getDelta();
        if (turnComponent.finishedTurn) {
            //Gdx.app.debug("Turn Scheduler System", "Turn time: " + turnComponent.executionTime + " Movement system time: " + turnComponent.movementSystemTime + " total ms:" + turnComponent.totalMovementSystemVisit);
            turnEntities.addLast(E.E(currentTurnTaker).removeTurnComponent().entity());
            currentTurnTaker = -1;
        }
    }
}
