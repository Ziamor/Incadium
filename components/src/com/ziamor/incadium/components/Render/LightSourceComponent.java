package com.ziamor.incadium.components.Render;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;


public class LightSourceComponent extends Component {
    public Color lightColor;
    public float size;

    public void set(Color lightColor, float size) {
        this.lightColor = lightColor;
        this.size = size;
    }
}
