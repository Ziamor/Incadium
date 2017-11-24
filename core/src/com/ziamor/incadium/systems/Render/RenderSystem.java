package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ziamor.incadium.components.Render.NotVisableComponent;
import com.ziamor.incadium.components.Render.shaders.BlinkShaderComponent;
import com.ziamor.incadium.components.Render.shaders.OutlineShaderComponent;
import com.ziamor.incadium.components.Render.RenderPositionComponent;
import com.ziamor.incadium.components.Render.shaders.ShaderComponent;
import com.ziamor.incadium.components.Render.TextureRegionComponent;
import com.ziamor.incadium.systems.Util.SortedIteratingSystem;

import java.util.Comparator;

public class RenderSystem extends SortedIteratingSystem {
    private ComponentMapper<TextureRegionComponent> textureRegionComponentComponentMapper;
    private ComponentMapper<ShaderComponent> shaderComponentMapper;
    private ComponentMapper<RenderPositionComponent> renderPositionComponentMapper;
    private ComponentMapper<OutlineShaderComponent> outlineShaderComponentMapper;
    private ComponentMapper<BlinkShaderComponent> blinkShaderComponentMapper;
    @Wire
    private SpriteBatch batch;
    TagManager tagManager;
    private Mesh mesh;

    public RenderSystem() {
        super(Aspect.all(RenderPositionComponent.class, TextureRegionComponent.class).exclude(NotVisableComponent.class));
        mesh = new com.badlogic.gdx.graphics.Mesh(true, 6, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
    }

    @Override
    protected void process(int entityId) {
        final TextureRegionComponent textureRegionComponent = textureRegionComponentComponentMapper.get(entityId);
        final ShaderComponent shaderComponent = shaderComponentMapper.get(entityId);
        final RenderPositionComponent renderPositionComponent = renderPositionComponentMapper.get(entityId);

        if (shaderComponent != null) {// TODO remove empty shader(shader comp exist but no shaders attached to ent)
            final OutlineShaderComponent outlineShaderComponent = outlineShaderComponentMapper.get(entityId);
            final BlinkShaderComponent blinkShaderComponent = blinkShaderComponentMapper.get(entityId);
            batch.end(); //TODO find a better way to render shaders inside batch
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

            if (blinkShaderComponent != null)
                runBlinkShader(blinkShaderComponent, renderPositionComponent, textureRegionComponent);
            if (outlineShaderComponent != null)
                runOutLineShader(outlineShaderComponent, renderPositionComponent, textureRegionComponent);
            batch.begin();
        } else {
            if (textureRegionComponent.region == null) {
                Gdx.app.debug("Render System", "Missing texture region");
                return;
            }
            batch.draw(textureRegionComponent.region, renderPositionComponent.x, renderPositionComponent.y, 1, 1);
        }
    }

    public Comparator<Integer> getComparator() {
        return new Comparator<Integer>() {
            @Override
            public int compare(Integer e1, Integer e2) {
                RenderPositionComponent e1T = renderPositionComponentMapper.get(e1);
                RenderPositionComponent e2T = renderPositionComponentMapper.get(e2);
                if (e1T == null)
                    return -1;
                if (e2T == null)
                    return 1;
                return (int) Math.signum(e1T.z - e2T.z);
            }
        };
    }

    @Override
    protected void begin() {
        super.begin();

        batch.begin();
    }

    @Override
    protected void end() {
        super.end();
        batch.end();
    }

    private Mesh getTextureRegionMesh(RenderPositionComponent renderPositionComponent, TextureRegionComponent textureRegionComponent) {
        float x = renderPositionComponent.x;
        float y = renderPositionComponent.y;
        float width = 1; // TODO width and height should be passed as a param
        float height = 1;
        float fx2 = x + width;
        float fy2 = y + height;
        float u = textureRegionComponent.region.getU();
        float v = textureRegionComponent.region.getV2();
        float u2 = textureRegionComponent.region.getU2();
        float v2 = textureRegionComponent.region.getV();

        float[] verts = new float[30];
        int i = 0;

        //Top Left Vertex Triangle 1
        verts[i++] = x;   //X
        verts[i++] = fy2; //Y
        verts[i++] = 0;    //Z
        verts[i++] = u;   //U
        verts[i++] = v2;   //V

        //Top Right Vertex Triangle 1
        verts[i++] = fx2;
        verts[i++] = fy2;
        verts[i++] = 0;
        verts[i++] = u2;
        verts[i++] = v2;

        //Bottom Left Vertex Triangle 1
        verts[i++] = x;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = u;
        verts[i++] = v;

        //Top Right Vertex Triangle 2
        verts[i++] = fx2;
        verts[i++] = fy2;
        verts[i++] = 0;
        verts[i++] = u2;
        verts[i++] = v2;

        //Bottom Right Vertex Triangle 2
        verts[i++] = fx2;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = u2;
        verts[i++] = v;

        //Bottom Left Vertex Triangle 2
        verts[i++] = x;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = u;
        verts[i] = v;

        mesh.setVertices(verts);
        return mesh;
    }

    protected void runOutLineShader(OutlineShaderComponent outlineShaderComponent, RenderPositionComponent renderPositionComponent, TextureRegionComponent textureRegionComponent) {
        ShaderProgram shader = outlineShaderComponent.shaderProgram;
        if (shader.isCompiled()) {
            shader.begin();

            shader.setUniformMatrix("u_projTrans", batch.getProjectionMatrix());

            Texture texture = textureRegionComponent.region.getTexture();
            texture.bind();
            shader.setUniformi("u_texture", 0);

            float pixelWidth = 1f / (float) texture.getWidth();
            float pixelHeight = 1f / (float) texture.getHeight();
            shader.setUniform2fv("u_pixelSize", new float[]{pixelWidth, pixelHeight}, 0, 2);

            shader.setUniformf("u_OutlineColor", Color.ORANGE);

            getTextureRegionMesh(renderPositionComponent, textureRegionComponent);
            mesh.render(shader, GL20.GL_TRIANGLES);
            shader.end();
        }
    }

    protected void runBlinkShader(BlinkShaderComponent blinkShaderComponent, RenderPositionComponent renderPositionComponent, TextureRegionComponent textureRegionComponent) {
        ShaderProgram shader = blinkShaderComponent.shaderProgram;
        blinkShaderComponent.time += world.getDelta();

        textureRegionComponent.region.getTexture().bind();
        mesh = getTextureRegionMesh(renderPositionComponent, textureRegionComponent);

        shader.begin();
        //shader.pedantic = false;
        shader.setUniformMatrix("u_projTrans", batch.getProjectionMatrix());
        shader.setUniformi("u_texture", 0);
        shader.setUniformf("time", blinkShaderComponent.time);
        shader.setUniformf("blink_rate", 0.3f);

        mesh.render(shader, GL20.GL_TRIANGLES);
        shader.end();
    }
}

