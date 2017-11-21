package com.ziamor.incadium.systems.Combat;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.systems.IteratingSystem;
import com.ziamor.incadium.components.Combat.AttackCoolDownComponent;


public class AttackCoolDownSystem extends IteratingSystem {
    private ComponentMapper<AttackCoolDownComponent> attackCoolDownComponentMapper;

    public AttackCoolDownSystem() {
        super(Aspect.all(AttackCoolDownComponent.class));
    } //TODO add turn component as a requirement?

    @Override
    protected void process(int entityId) {
        final AttackCoolDownComponent attackCoolDownComponent = attackCoolDownComponentMapper.get(entityId);

        attackCoolDownComponent.elapsed += world.getDelta();

        if (attackCoolDownComponent.elapsed >= attackCoolDownComponent.life)
            E.E(entityId).removeAttackCoolDownComponent();
    }
}
