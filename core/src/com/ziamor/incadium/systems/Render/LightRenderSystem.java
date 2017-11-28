package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ziamor.incadium.Screens.GamePlayScreen;
import com.ziamor.incadium.components.Render.LightSourceComponent;
import com.ziamor.incadium.components.Render.NotVisableComponent;
import com.ziamor.incadium.components.Render.RenderPositionComponent;


public class LightRenderSystem extends BaseEntitySystem {
    @Wire
    OrthographicCamera camera;
    @Wire
    SpriteBatch batch;
    @Wire
    AssetManager assetManager;
    @Wire
    FitViewport viewport;

    boolean load;
    Mesh frameBufferMesh;
    ShaderProgram ambientLightShader;
    String ambientLightShaderFileName = "ambient light";

    Color ambientLightColor;

    Texture lightMaskStencil128, lightMaskStencil256, lightMaskStencil512, lightColorStencil128, lightColorStencil256, lightColorStencil512;

    FrameBuffer fbLightMaskMap, fbLightColorMap;

    private ComponentMapper<RenderPositionComponent> renderPositionComponentMapper;
    private ComponentMapper<LightSourceComponent> lightSourceComponentMapper;

    float light_flicker_time, light_flicker_length = 1;

    float lightOffset = 0.5f;
    float lightFlickerNoise = 0;

    public LightRenderSystem() {
        super(Aspect.all());
        load = true;
        frameBufferMesh = new com.badlogic.gdx.graphics.Mesh(true, 6, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

        String vertexShader = Gdx.files.internal("shaders\\" + ambientLightShaderFileName + "\\vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders\\" + ambientLightShaderFileName + "\\fragment.glsl").readString();

        ambientLightShader = new ShaderProgram(vertexShader, fragmentShader);
        ambientLightColor = new Color(0.2f, 0.16f, 0.3f, 1.0f);
    }

    @Override
    protected void processSystem() {
        if (load) {
            lightMaskStencil128 = assetManager.get("light_stencil128.png", Texture.class);
            lightMaskStencil256 = assetManager.get("light_stencil256.png", Texture.class);
            lightMaskStencil512 = assetManager.get("light_stencil256.png", Texture.class);
            lightColorStencil128 = assetManager.get("light_color_stencil128.png", Texture.class);
            lightColorStencil256 = assetManager.get("light_color_stencil256.png", Texture.class);
            lightColorStencil512 = assetManager.get("light_color_stencil256.png", Texture.class);

            fbLightMaskMap = new FrameBuffer(Pixmap.Format.RGBA8888, viewport.getScreenWidth(), viewport.getScreenHeight(), false);
            fbLightColorMap = new FrameBuffer(Pixmap.Format.RGBA8888, viewport.getScreenWidth(), viewport.getScreenHeight(), false);

            load = false;
        }
        lightFlickerNoise = MathUtils.random(-1, 1);
        renderLightMask();
        renderLightColorMap();
        renderLight();
        viewport.apply();
    }

    protected void renderLightColorMap() {
        light_flicker_time += world.getDelta();

        fbLightColorMap.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        //TODO figure out if I can cache subscription managers
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
        batch.end();
        //PixmapIO.writePNG(new FileHandle("light color.png"), ScreenUtils.getFrameBufferPixmap(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        fbLightColorMap.end();
    }

    protected void renderLightMask() {
        fbLightMaskMap.begin();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

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

        batch.end();
        //PixmapIO.writePNG(new FileHandle("light mask.png"), ScreenUtils.getFrameBufferPixmap(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        fbLightMaskMap.end();
    }

    protected void renderLight() {
        ambientLightShader.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ambientLightShader.setUniformMatrix("u_projTrans", camera.projection);

        fbLightColorMap.getColorBufferTexture().bind(2);
        ambientLightShader.setUniformi("u_lightColorMap", 2);

        fbLightMaskMap.getColorBufferTexture().bind(1);
        ambientLightShader.setUniformi("u_lightmap", 1);

        GamePlayScreen.fbWorld.getColorBufferTexture().bind(0);
        ambientLightShader.setUniformi("u_texture", 0);

        ambientLightShader.setUniformf("u_ambientColor", ambientLightColor);

        float x = GamePlayScreen.map_width / -2;
        float y = GamePlayScreen.map_height / 2;
        float width = GamePlayScreen.map_width;
        float height = -GamePlayScreen.map_height;
        float fx2 = x + width;
        float fy2 = y + height;

        float[] verts = new float[30];
        int i = 0;

        //Top Left Vertex Triangle 1
        verts[i++] = x;   //X
        verts[i++] = fy2; //Y
        verts[i++] = 0;    //Z
        verts[i++] = 0f;   //U
        verts[i++] = 0f;   //V

        //Top Right Vertex Triangle 1
        verts[i++] = fx2;
        verts[i++] = fy2;
        verts[i++] = 0;
        verts[i++] = 1f;
        verts[i++] = 0f;

        //Bottom Left Vertex Triangle 1
        verts[i++] = x;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = 0f;
        verts[i++] = 1f;

        //Top Right Vertex Triangle 2
        verts[i++] = fx2;
        verts[i++] = fy2;
        verts[i++] = 0;
        verts[i++] = 1f;
        verts[i++] = 0f;

        //Bottom Right Vertex Triangle 2
        verts[i++] = fx2;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = 1f;
        verts[i++] = 1f;

        //Bottom Left Vertex Triangle 2
        verts[i++] = x;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = 0f;
        verts[i] = 1f;

        frameBufferMesh.setVertices(verts);
        frameBufferMesh.render(ambientLightShader, GL20.GL_TRIANGLES);
        ambientLightShader.end();
    }

    public void resize(int width, int height) {
        fbLightMaskMap = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        fbLightColorMap = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
    }
}
