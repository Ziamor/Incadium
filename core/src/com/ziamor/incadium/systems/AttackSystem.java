package com.ziamor.incadium.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.ziamor.incadium.components.AttackDamageComponent;
import com.ziamor.incadium.components.AttackTargetComponent;
import com.ziamor.incadium.components.DeadComponent;
import com.ziamor.incadium.components.HealthComponent;
import com.ziamor.incadium.components.PlayerControllerComponent;


public class AttackSystem extends IteratingSystem {
    private ComponentMapper<AttackTargetComponent> attackTargetComponentMapper;
    private ComponentMapper<AttackDamageComponent> attackDamageComponentMapper;
    private ComponentMapper<HealthComponent> healthComponentMapper;
    private ComponentMapper<PlayerControllerComponent> playerControllerComponentMapper;

    public AttackSystem() {
        super(Aspect.all(AttackTargetComponent.class, AttackDamageComponent.class).exclude(DeadComponent.class));
    }

    @Override
    protected void process(int entityId) {
        AttackTargetComponent attackTargetComponent = attackTargetComponentMapper.get(entityId);
        AttackDamageComponent attackDamageComponent = attackDamageComponentMapper.get(entityId);

        //TODO link relationship
        HealthComponent targetHealth = healthComponentMapper.get(attackTargetComponent.target);
        if (targetHealth != null) {
            targetHealth.currentHealth -= attackDamageComponent.damage;
            Gdx.app.log("", "Current target health: " + targetHealth.currentHealth);
            E.E(entityId).removeAttackTargetComponent();

            if (playerControllerComponentMapper.get(entityId) != null) {
                E.E(entityId).blockPlayerInputComponent(1f);
            }
        }
    }
}
