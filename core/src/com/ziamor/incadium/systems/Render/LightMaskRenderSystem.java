package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziamor.incadium.components.Render.LightSourceComponent;
import com.ziamor.incadium.components.Render.NotVisableComponent;
import com.ziamor.incadium.components.Render.RenderPositionComponent;


public class LightMaskRenderSystem extends BaseEntitySystem {
    @Wire
    SpriteBatch batch;
    @Wire
    OrthographicCamera camera;
    @Wire
    AssetManager assetManager;

    FrameBuffer fbLightMaskMap;

    private ComponentMapper<RenderPositionComponent> renderPositionComponentMapper;
    private ComponentMapper<LightSourceComponent> lightSourceComponentMapper;

    Texture lightMaskStencil128, lightMaskStencil256, lightMaskStencil512;

    float light_flicker_time, light_flicker_length = 1;

    public LightMaskRenderSystem(Viewport viewport) {
        super(Aspect.all());
        fbLightMaskMap = new FrameBuffer(Pixmap.Format.RGBA8888, viewport.getScreenWidth(), viewport.getScreenHeight(), false);
    }

    @Override
    protected void begin() {
        //TODO, load textures better
        if (lightMaskStencil128 == null)
            lightMaskStencil128 = assetManager.get("light_stencil128.png", Texture.class);
        if (lightMaskStencil256 == null)
            lightMaskStencil256 = assetManager.get("light_stencil256.png", Texture.class);
        if (lightMaskStencil512 == null)
            lightMaskStencil512 = assetManager.get("light_stencil256.png", Texture.class);

        fbLightMaskMap.begin();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        light_flicker_time += world.getDelta();
    }

    @Override
    protected void end() {
        batch.end();
        //PixmapIO.writePNG(new FileHandle("light mask.png"), ScreenUtils.getFrameBufferPixmap(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        fbLightMaskMap.end();
    }

    @Override
    protected void processSystem() {
        float lightOffset = 0.5f;
        float lightFlickerNoise = MathUtils.random(-1, 1);

        IntBag lightID = world.getAspectSubscriptionManager().get(Aspect.all(LightSourceComponent.class, RenderPositionComponent.class).exclude(NotVisableComponent.class)).getEntities();
        for (int i = 0; i < lightID.size(); i++) {
            RenderPositionComponent renderPositionComponent = renderPositionComponentMapper.get(lightID.get(i));
            final LightSourceComponent lightSourceComponent = lightSourceComponentMapper.get(lightID.get(i));
            if (renderPositionComponent != null && lightSourceComponent != null) {
                float lightSize = lightSourceComponent.size;
                if (lightSourceComponent.enableFlicker) {
                    float lightFlicker = lightSourceComponent.flickerSize * (float) Math.sin(light_flicker_time / light_flicker_length) + lightSourceComponent.flickerNoiseRange * lightFlickerNoise;
                    lightSize = lightSourceComponent.size - lightSourceComponent.flickerSize + lightFlicker;
                }
                Texture lightMaskStencil;
                if (lightSize <= 4.0f)
                    lightMaskStencil = lightMaskStencil128;
                else if (lightSize <= 8.0f)
                    lightMaskStencil = lightMaskStencil256;
                else
                    lightMaskStencil = lightMaskStencil512;

                batch.draw(lightMaskStencil, renderPositionComponent.x - lightSize / 2 + lightOffset, renderPositionComponent.y - lightSize / 2 + lightOffset, lightSize, lightSize);
            }
        }
    }

    public Texture getLightMask() {
        return fbLightMaskMap.getColorBufferTexture();
    }
}
