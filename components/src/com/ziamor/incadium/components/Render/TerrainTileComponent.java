package com.ziamor.incadium.components.Render;

import com.artemis.Component;
import com.artemis.annotations.Transient;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
@Transient
public class TerrainTileComponent extends Component {
    public TextureRegion region;

    //TODO load textures correctly, take memory allocation and deallocation into consideration
    public void setRegion(String fileName, int x, int y) {
        region = new TextureRegion(new Texture(fileName), x * 32, y * 32, 32, 32);
    }

    public void setRegion(Texture tex, int x, int y) {
        region = new TextureRegion(tex, x * 32, y * 32, 32, 32);
    }
}
