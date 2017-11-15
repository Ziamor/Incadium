package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.incadium.components.Movement.AttackLerpComponent;
import com.ziamor.incadium.components.Render.AnimationComponent;
import com.ziamor.incadium.components.Render.SlimeAnimation;


public class SlimeAnimationControllerSystem extends IteratingSystem {
    private ComponentMapper<AnimationComponent> animationComponentMapper;
    private ComponentMapper<AttackLerpComponent> attackLerpComponentMapper;

    public SlimeAnimationControllerSystem() {
        super(Aspect.all(SlimeAnimation.class, AnimationComponent.class));
    }

    @Override
    protected void process(int entityId) {
        final AnimationComponent animationComponent = animationComponentMapper.get(entityId);
        final AttackLerpComponent attackLerpComponent = attackLerpComponentMapper.get(entityId);
        if (attackLerpComponent != null)
            animationComponent.currentAnimation = animationComponent.animation.get("attack");
        else
            animationComponent.currentAnimation = animationComponent.animation.get("walk");
    }
}
