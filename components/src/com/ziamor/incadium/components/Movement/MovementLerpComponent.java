package com.ziamor.incadium.components.Movement;

import com.artemis.Component;
import com.artemis.annotations.Transient;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Interpolation;
@Transient
public class MovementLerpComponent extends Component {
    private float startX, startY, targetX, targetY;
    public float elapsed = 0;
    private float life;

    public Interpolation interpolator = Interpolation.linear;

    public void set(float startX, float startY, float targetX, float targetY, float life) {
        this.startX = startX;
        this.startY = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.life = life;
    }

    public Vector2 getCurrentPos() {
        Vector2 vec = new Vector2();
        float progress = Math.min(1f, elapsed / life);
        float alpha = interpolator.apply(progress);
        vec.x = startX + (targetX - startX) * alpha;
        vec.y = startY + (targetY - startY) * alpha;
        return vec;
    }

    public boolean isFinished() {
        float progress = Math.min(1f, elapsed / life);
        return progress >= 1;
    }
}
