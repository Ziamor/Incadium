package com.ziamor.incadium.systems.Asset;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ziamor.incadium.components.Asset.ShaderResolverComponent;
import com.ziamor.incadium.components.Render.MeshComponent;
import com.ziamor.incadium.components.Render.ShaderComponent;

public class ShaderResolverSystem extends AssetResolverSystem {
    private ComponentMapper<ShaderComponent> shaderComponentMapper;
    private ComponentMapper<ShaderResolverComponent> shaderResolverComponentMapper;
    private ComponentMapper<MeshComponent> meshComponentMapper;

    public ShaderResolverSystem() {
        super(Aspect.all(ShaderResolverComponent.class).exclude(ShaderComponent.class)); //TODO transform component mandatory? Cant render a shader without a position
    }

    @Override
    protected void load(int entityId) {
        final ShaderResolverComponent shaderResolverComponent = shaderResolverComponentMapper.get(entityId);
        final MeshComponent meshComponent = meshComponentMapper.get(entityId);

        final ShaderComponent shaderComponent = shaderComponentMapper.create(entityId);
        String vertexShader = Gdx.files.internal("vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("fragment.glsl").readString();
        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        shaderComponent.shaderProgram = shader;

        if(meshComponent == null){
            meshComponentMapper.create(entityId);
        }
    }
}
