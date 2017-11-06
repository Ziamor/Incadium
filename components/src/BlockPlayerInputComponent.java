package com.ziamor.incadium.components;

import com.artemis.Component;
import com.badlogic.gdx.utils.TimeUtils;

public class BlockPlayerInputComponent extends Component {
    public float duration;
    public long startTime;

    public void set(float dur) {
        this.duration = dur;
        startTime = TimeUtils.millis();
    }
}
