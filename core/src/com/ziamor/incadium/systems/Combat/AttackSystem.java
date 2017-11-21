package com.ziamor.incadium.systems.Combat;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.ziamor.incadium.components.Combat.AttackCoolDownComponent;
import com.ziamor.incadium.components.Combat.AttackDamageComponent;
import com.ziamor.incadium.components.Combat.AttackTargetComponent;
import com.ziamor.incadium.components.Combat.DeadComponent;
import com.ziamor.incadium.components.Movement.AttackLerpComponent;
import com.ziamor.incadium.components.Render.NotVisableComponent;
import com.ziamor.incadium.components.Stats.HealthComponent;
import com.ziamor.incadium.components.Movement.PlayerControllerComponent;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.components.TurnComponent;
import com.ziamor.incadium.systems.Render.VisibilitySystem;


public class AttackSystem extends IteratingSystem {
    private ComponentMapper<AttackTargetComponent> attackTargetComponentMapper;
    private ComponentMapper<AttackDamageComponent> attackDamageComponentMapper;
    private ComponentMapper<HealthComponent> healthComponentMapper;
    private ComponentMapper<PlayerControllerComponent> playerControllerComponentMapper;
    private ComponentMapper<TurnComponent> turnComponentMapper;
    private ComponentMapper<AttackLerpComponent> attackLerpComponentMapper;
    private ComponentMapper<TransformComponent> transformComponentMapper;

    private float life = 0.5f;

    public AttackSystem() {
        super(Aspect.all(AttackTargetComponent.class, AttackDamageComponent.class).exclude(DeadComponent.class, AttackCoolDownComponent.class, AttackLerpComponent.class));
    }

    @Override
    protected void process(int entityId) {
        final AttackTargetComponent attackTargetComponent = attackTargetComponentMapper.get(entityId);
        final AttackDamageComponent attackDamageComponent = attackDamageComponentMapper.get(entityId);
        final TurnComponent turnComponent = turnComponentMapper.get(entityId);
        final TransformComponent transformComponent = transformComponentMapper.get(entityId);

        if (E.E(attackTargetComponent.target) == null)
            E.E(entityId).removeAttackTargetComponent();
        //TODO link relationship
        HealthComponent targetHealth = healthComponentMapper.get(attackTargetComponent.target);
        if (targetHealth != null) {
            TransformComponent targetTransformComponent = transformComponentMapper.get(attackTargetComponent.target);
            targetHealth.currentHealth -= attackDamageComponent.damage;
            if (transformComponent != null && targetTransformComponent != null)
                attackLerpComponentMapper.create(entityId).set(transformComponent.x, transformComponent.y, targetTransformComponent.x, targetTransformComponent.y, life);
            if (playerControllerComponentMapper.get(entityId) != null) {
                E.E(entityId).attackCoolDownComponent(1f);
            }
            if (playerControllerComponentMapper.get(attackTargetComponent.target) != null) {
                E.E(attackTargetComponent.target).tookDamageComponent(1f);
            }
            attackTargetComponentMapper.remove(entityId);
        } else
            E.E(entityId).removeAttackTargetComponent();

        if (turnComponent != null)
            turnComponent.finishedTurn = true;
    }
}
