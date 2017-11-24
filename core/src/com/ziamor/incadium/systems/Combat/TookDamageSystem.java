package com.ziamor.incadium.systems.Combat;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.incadium.components.Asset.ShaderResolverComponent;
import com.ziamor.incadium.components.Combat.TookDamageComponent;
import com.ziamor.incadium.components.Render.shaders.BlinkShaderComponent;
import com.ziamor.incadium.components.Render.shaders.OutlineShaderComponent;
import com.ziamor.incadium.components.Render.shaders.ShaderComponent;


public class TookDamageSystem extends IteratingSystem {

    private ComponentMapper<TookDamageComponent> tookDamageComponentMapper;
    private ComponentMapper<ShaderResolverComponent> shaderResolverComponentMapper;
    private ComponentMapper<ShaderComponent> shaderComponentMapper;
    private ComponentMapper<BlinkShaderComponent> blinkShaderComponentMapper;

    public TookDamageSystem() {
        super(Aspect.all(TookDamageComponent.class));
    }

    @Override
    protected void process(int entityId) {
        final TookDamageComponent tookDamageComponent = tookDamageComponentMapper.get(entityId);

        tookDamageComponent.elapsed += world.getDelta();

        if (tookDamageComponent.elapsed >= tookDamageComponent.life) {
            tookDamageComponentMapper.remove(entityId);
            blinkShaderComponentMapper.remove(entityId);
        } else {
            final ShaderResolverComponent shaderResolverComponent = shaderResolverComponentMapper.get(entityId);
            if (shaderResolverComponent == null)
                shaderResolverComponentMapper.create(entityId).set(BlinkShaderComponent.class);
        }
    }
}
