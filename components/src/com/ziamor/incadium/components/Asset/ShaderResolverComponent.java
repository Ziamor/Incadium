package com.ziamor.incadium.components.Asset;

import com.artemis.Component;


public class ShaderResolverComponent extends Component {
    public Class<? extends Component> shaderComponentClass;

    public void set(Class<? extends Component> shaderComponentClass) {
        this.shaderComponentClass = shaderComponentClass;
    }
}
