package com.ziamor.incadium.systems.Combat;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.ziamor.incadium.components.Asset.ShaderResolverComponent;
import com.ziamor.incadium.components.Combat.TookDamageComponent;
import com.ziamor.incadium.components.Render.ShaderComponent;


public class TookDamageSystem extends IteratingSystem {

    private ComponentMapper<TookDamageComponent> tookDamageComponentMapper;
    private ComponentMapper<ShaderResolverComponent> shaderResolverComponentMapper;
    private ComponentMapper<ShaderComponent> shaderComponentMapper;

    public TookDamageSystem() {
        super(Aspect.all(TookDamageComponent.class));
    }

    @Override
    protected void process(int entityId) {
        final TookDamageComponent tookDamageComponent = tookDamageComponentMapper.get(entityId);

        tookDamageComponent.elapsed += world.getDelta();

        if (tookDamageComponent.elapsed >= tookDamageComponent.life) {
            tookDamageComponentMapper.remove(entityId);
            shaderResolverComponentMapper.remove(entityId);
            shaderComponentMapper.remove(entityId);
        } else {
            final ShaderResolverComponent shaderResolverComponent = shaderResolverComponentMapper.get(entityId);
            if (shaderResolverComponent == null)
                shaderResolverComponentMapper.create(entityId);
        }
    }
}
