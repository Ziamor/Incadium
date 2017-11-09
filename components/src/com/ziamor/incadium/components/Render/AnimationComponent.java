package com.ziamor.incadium.components.Render;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class AnimationComponent extends Component {
    public Animation<TextureRegion> animation;
    public float time = 0;

    public void set(Animation<TextureRegion> animation, float startTime) {
        this.animation = animation;
        if (startTime < 0)
            this.time = 0;
        else
            this.time = startTime;
    }
}
