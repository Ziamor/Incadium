package com.ziamor.incadium.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Texture;

public class TextureComponent extends Component {
    public Texture texture = null;

    public void set(String fileName) {
        texture = new Texture(fileName);
    }
}
