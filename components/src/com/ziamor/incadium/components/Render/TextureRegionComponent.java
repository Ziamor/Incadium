package com.ziamor.incadium.components.Render;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureRegionComponent extends Component {
    public TextureRegion region;

    public void set(Texture texture, int px, int py, int w, int h) {
        this.region = new TextureRegion(texture, px, py, w, h);
    }
}
