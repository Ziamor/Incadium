package com.ziamor.incadium.components.Render;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;


public class AnimationComponent extends Component {
    public ObjectMap<String, Animation<TextureRegion>> animation;
    public float time = 0;
    public Animation<TextureRegion> currentAnimation;

    public void set(ObjectMap<String, Animation<TextureRegion>> animation, float startTime) {
        this.animation = animation;
        if (startTime < 0)
            this.time = 0;
        else
            this.time = startTime;
    }
}
