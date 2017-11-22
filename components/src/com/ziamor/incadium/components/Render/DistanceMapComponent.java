package com.ziamor.incadium.components.Render;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Texture;


public class DistanceMapComponent extends Component {
    public Texture distanceMap;

    public void set(Texture distanceMap) {
        this.distanceMap = distanceMap;
    }
}
