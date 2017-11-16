package com.ziamor.incadium.components.Render;

import com.artemis.Component;
import com.artemis.annotations.Transient;
import com.badlogic.gdx.graphics.Texture;

import javax.xml.soap.Text;

@Transient
public class TextureComponent extends Component {
    public Texture texture = null;

    public void set(Texture texture) {
        this.texture = texture;
    }
}
