package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
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
import com.ziamor.incadium.components.Render.RenderPositionComponent;
import com.ziamor.incadium.components.Render.TextureRegionComponent;
import com.ziamor.incadium.components.Render.shaders.OutlineShaderComponent;


public class OutLineRenderSystem extends IteratingSystem {
    @Wire
    SpriteBatch batch;

    private ComponentMapper<TextureRegionComponent> textureRegionComponentComponentMapper;
    private ComponentMapper<RenderPositionComponent> renderPositionComponentMapper;
    private ComponentMapper<OutlineShaderComponent> outlineShaderComponentMapper;

    Mesh mesh;

    ShaderProgram outlineShader;

    String shaderFileName = "outline";

    public OutLineRenderSystem() {
        super(Aspect.all(OutlineShaderComponent.class, TextureRegionComponent.class, RenderPositionComponent.class).exclude(NotVisableComponent.class));
        mesh = new com.badlogic.gdx.graphics.Mesh(true, 6, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

        String vertexShader = Gdx.files.internal("shaders\\" + shaderFileName + "\\vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders\\" + shaderFileName + "\\fragment.glsl").readString();
        outlineShader = new ShaderProgram(vertexShader, fragmentShader);
        if(outlineShader.getLog().length() > 0)
            Gdx.app.debug("Outline Render System",outlineShader.getLog());
    }

    @Override
    protected void begin() {
        super.begin();
    }

    @Override
    protected void end() {
        super.end();
    }

    @Override
    protected void process(int entityId) {
        final TextureRegionComponent textureRegionComponent = textureRegionComponentComponentMapper.get(entityId);
        final RenderPositionComponent renderPositionComponent = renderPositionComponentMapper.get(entityId);
        final OutlineShaderComponent outlineShaderComponent = outlineShaderComponentMapper.get(entityId);

        if (outlineShader.isCompiled()) {
            outlineShader.begin();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
            outlineShader.setUniformMatrix("u_projTrans", batch.getProjectionMatrix());

            Texture texture = textureRegionComponent.region.getTexture();
            texture.bind();
            outlineShader.setUniformi("u_texture", 0);

            float pixelWidth = 1f / (float) texture.getWidth();
            float pixelHeight = 1f / (float) texture.getHeight();
            outlineShader.setUniform2fv("u_pixelSize", new float[]{pixelWidth, pixelHeight}, 0, 2);

            outlineShader.setUniformf("u_OutlineColor", Color.ORANGE);

            getTextureRegionMesh(renderPositionComponent, textureRegionComponent);
            mesh.render(outlineShader, GL20.GL_TRIANGLES);
            outlineShader.end();
        }
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
}
