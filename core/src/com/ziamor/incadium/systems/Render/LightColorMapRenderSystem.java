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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziamor.incadium.components.Render.LightSourceComponent;
import com.ziamor.incadium.components.Render.NotVisableComponent;
import com.ziamor.incadium.components.Render.RenderPositionComponent;


public class LightColorMapRenderSystem extends BaseEntitySystem {
    @Wire
    SpriteBatch batch;
    @Wire
    OrthographicCamera camera;
    @Wire
    AssetManager assetManager;
    @Wire
    FitViewport viewport;
    private ComponentMapper<RenderPositionComponent> renderPositionComponentMapper;
    private ComponentMapper<LightSourceComponent> lightSourceComponentMapper;

    FrameBuffer fbLightColorMap;
    Texture lightColorStencil128, lightColorStencil256, lightColorStencil512;

    float light_flicker_time, light_flicker_length = 1;

    public LightColorMapRenderSystem(Viewport viewport) {
        super(Aspect.all());
        fbLightColorMap = new FrameBuffer(Pixmap.Format.RGBA8888, viewport.getScreenWidth(), viewport.getScreenHeight(), false);
    }

    @Override
    protected void begin() {
        //TODO, load textures better
        if (lightColorStencil128 == null)
            lightColorStencil128 = assetManager.get("light_color_stencil128.png", Texture.class);
        if (lightColorStencil256 == null)
            lightColorStencil256 = assetManager.get("light_color_stencil256.png", Texture.class);
        if (lightColorStencil512 == null)
            lightColorStencil512 = assetManager.get("light_color_stencil256.png", Texture.class);

        fbLightColorMap.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    protected void end() {
        batch.end();
        //PixmapIO.writePNG(new FileHandle("light color.png"), ScreenUtils.getFrameBufferPixmap(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        fbLightColorMap.end();
        viewport.apply();
    }

    @Override
    protected void processSystem() {
        float lightOffset = 0.5f;
        float lightFlickerNoise = MathUtils.random(-1, 1);

        IntBag lightID = world.getAspectSubscriptionManager().get(Aspect.all(LightSourceComponent.class, RenderPositionComponent.class).exclude(NotVisableComponent.class)).getEntities();
        for (int i = 0; i < lightID.size(); i++) {
            final RenderPositionComponent renderPositionComponent = renderPositionComponentMapper.get(lightID.get(i));
            final LightSourceComponent lightSourceComponent = lightSourceComponentMapper.get(lightID.get(i));
            if (renderPositionComponent != null && lightSourceComponent != null) {//TODO this should not be null do to the aspect builder but it is, figure out why
                batch.setColor(lightSourceComponent.lightColor);
                float lightSize = lightSourceComponent.size;
                if (lightSourceComponent.enableFlicker) {
                    float lightFlicker = lightSourceComponent.flickerSize * (float) Math.sin(light_flicker_time / light_flicker_length) + lightSourceComponent.flickerNoiseRange * lightFlickerNoise;
                    lightSize = lightSourceComponent.size - lightSourceComponent.flickerSize + lightFlicker;
                }

                Texture lightColorStencil;
                if (lightSize <= 4.0f)
                    lightColorStencil = lightColorStencil128;
                else if (lightSize <= 4.0f)
                    lightColorStencil = lightColorStencil256;
                else
                    lightColorStencil = lightColorStencil512;
                batch.draw(lightColorStencil, renderPositionComponent.x - lightSize / 2 + lightOffset, renderPositionComponent.y - lightSize / 2 + lightOffset, lightSize, lightSize);
            }
        }
    }

    public Texture getLightMap() {
        return fbLightColorMap.getColorBufferTexture();
    }
}
