package com.ziamor.incadium.components.Render;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;


public class LightSourceComponent extends Component {
    public static Color DEFAULT_LIGHT = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    public static float DEFAULT_SIZE = 1.0f;

    public Color lightColor;
    public float size;

    public LightSourceComponent() {
        lightColor = DEFAULT_LIGHT;
        size = DEFAULT_SIZE;
    }

    public void set(Color lightColor, float size) {
        this.lightColor = lightColor;
        this.size = size;
    }
}
