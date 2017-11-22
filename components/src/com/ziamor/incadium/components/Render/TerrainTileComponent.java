package com.ziamor.incadium.components.Render;

import com.artemis.Component;
import com.artemis.annotations.Transient;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
@Transient
public class TerrainTileComponent extends Component {
    public TextureRegion region;

    public void setRegion(Texture tex, int x, int y) {
        region = new TextureRegion(tex, x * 32, y * 32, 32, 32);
    }
}
