package com.ziamor.incadium.systems.UI;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.ziamor.incadium.components.Combat.AttackCoolDownComponent;
import com.ziamor.incadium.components.Movement.PlayerControllerComponent;


public class AttackCooldownBarRender extends IteratingSystem {
    private ProgressBar bar;

    private ComponentMapper<AttackCoolDownComponent> attackCoolDownComponentMapper;

    public AttackCooldownBarRender(ProgressBar bar) {
        super(Aspect.all(PlayerControllerComponent.class));
        this.bar = bar;
    }

    @Override
    protected void process(int entityId) {
        final AttackCoolDownComponent attackCoolDownComponent = attackCoolDownComponentMapper.get(entityId);
        if (bar != null) {
            if (attackCoolDownComponent != null) {
                bar.setValue(attackCoolDownComponent.elapsed);
                bar.setRange(0, attackCoolDownComponent.life);
            } else {
                bar.setValue(bar.getMaxValue());
            }
        }
    }
}
