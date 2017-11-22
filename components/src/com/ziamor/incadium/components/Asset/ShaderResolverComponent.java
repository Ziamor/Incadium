package com.ziamor.incadium.components.Asset;

import com.artemis.Component;


public class ShaderResolverComponent extends Component {
    public String vertexShader = "vertex.glsl";
    public String fragmentShader = "fragment.glsl";

    public void set(String vertexShader, String fragmentShader) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }
}
