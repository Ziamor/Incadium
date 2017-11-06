package com.ziamor.incadium.components;

import com.artemis.Component;

public class TransformComponent extends Component {
    public float x, y, z;

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
