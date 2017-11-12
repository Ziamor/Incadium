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
import com.ziamor.incadium.components.Stats.HealthComponent;
import com.ziamor.incadium.components.Movement.PlayerControllerComponent;
import com.ziamor.incadium.components.TurnComponent;


public class AttackSystem extends IteratingSystem {
    private ComponentMapper<AttackTargetComponent> attackTargetComponentMapper;
    private ComponentMapper<AttackDamageComponent> attackDamageComponentMapper;
    private ComponentMapper<HealthComponent> healthComponentMapper;
    private ComponentMapper<PlayerControllerComponent> playerControllerComponentMapper;
    private ComponentMapper<TurnComponent> turnComponentMapper;

    public AttackSystem() {
        super(Aspect.all(AttackTargetComponent.class, AttackDamageComponent.class).exclude(DeadComponent.class, AttackCoolDownComponent.class));
    }

    @Override
    protected void process(int entityId) {
        final AttackTargetComponent attackTargetComponent = attackTargetComponentMapper.get(entityId);
        final AttackDamageComponent attackDamageComponent = attackDamageComponentMapper.get(entityId);
        final TurnComponent turnComponent = turnComponentMapper.get(entityId);

        if (E.E(attackTargetComponent.target) == null)
            E.E(entityId).removeAttackTargetComponent();
        //TODO link relationship
        HealthComponent targetHealth = healthComponentMapper.get(attackTargetComponent.target);
        if (targetHealth != null) {
            targetHealth.currentHealth -= attackDamageComponent.damage;
            E.E(entityId).removeAttackTargetComponent();

            if (playerControllerComponentMapper.get(entityId) != null) {
                E.E(entityId).attackCoolDownComponent(1f);
            }
        } else
            E.E(entityId).removeAttackTargetComponent();

        if (turnComponent != null)
            turnComponent.finishedTurn = true;
    }
}
