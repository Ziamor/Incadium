package com.ziamor.incadium.systems.Asset;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ziamor.incadium.components.Asset.ShaderResolverComponent;
import com.ziamor.incadium.components.Render.shaders.BlinkShaderComponent;
import com.ziamor.incadium.components.Render.shaders.OutlineShaderComponent;
import com.ziamor.incadium.components.Render.shaders.ShaderComponent;

public class ShaderResolverSystem extends AssetResolverSystem {
    private ComponentMapper<ShaderComponent> shaderComponentMapper;
    private ComponentMapper<ShaderResolverComponent> shaderResolverComponentMapper;
    private ComponentMapper<OutlineShaderComponent> outlineShaderComponentMapper;
    private ComponentMapper<BlinkShaderComponent> blinkShaderComponentMapper;

    public ShaderResolverSystem() {
        super(Aspect.all(ShaderResolverComponent.class)); //TODO transform component mandatory? Cant render a shader without a position
    }

    @Override
    protected void load(int entityId) {
        final ShaderResolverComponent shaderResolverComponent = shaderResolverComponentMapper.get(entityId);
        final ShaderComponent shaderComponent = shaderComponentMapper.create(entityId);
        Class<? extends Component> shaderComponentClass = shaderResolverComponent.shaderComponentClass;

        if (shaderComponentClass == OutlineShaderComponent.class) {
            ShaderProgram shaderProgram = createShaderProgram("outline");
            outlineShaderComponentMapper.create(entityId).shaderProgram = shaderProgram;
        } else if (shaderComponentClass == BlinkShaderComponent.class) {
            ShaderProgram shaderProgram = createShaderProgram("blink");
            blinkShaderComponentMapper.create(entityId).shaderProgram = shaderProgram;
        }
        shaderResolverComponentMapper.remove(entityId);
    }

    protected ShaderProgram createShaderProgram(String name) {
        String vertexShader = Gdx.files.internal("shaders\\" + name + "\\vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders\\" + name + "\\fragment.glsl").readString();
        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);

        if (shader.getLog().length() != 0)
            Gdx.app.debug("Shader Resolver System", shader.getLog());

        return shader;
    }
}