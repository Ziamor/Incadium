package com.ziamor.incadium.components.Render;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Texture;

import javax.xml.soap.Text;

public class TextureComponent extends Component {
    public Texture texture = null;

    public void set(Texture texture) {
        this.texture = texture;
    }
}
