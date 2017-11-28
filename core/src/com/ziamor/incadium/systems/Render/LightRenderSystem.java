package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ziamor.incadium.Screens.GamePlayScreen;


public class LightRenderSystem extends BaseEntitySystem {
    final float map_width = 16, map_height = 9;
    @Wire
    OrthographicCamera camera;
    Mesh frameBufferMesh;
    ShaderProgram ambientLightShader;
    String ambientLightShaderFileName = "ambient light";

    LightMaskRenderSystem lightMaskRenderSystem;
    LightColorMapRenderSystem lightColorMapRenderSystem;

    Color ambientLightColor;

    public LightRenderSystem() {
        super(Aspect.all());
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
        ambientLightShader.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ambientLightShader.setUniformMatrix("u_projTrans", camera.projection);

        lightColorMapRenderSystem.getLightMap().bind(2);
        ambientLightShader.setUniformi("u_lightColorMap", 2);

        lightMaskRenderSystem.getLightMask().bind(1);
        ambientLightShader.setUniformi("u_lightmap", 1);

        GamePlayScreen.fbWorld.getColorBufferTexture().bind(0);
        ambientLightShader.setUniformi("u_texture", 0);

        ambientLightShader.setUniformf("u_ambientColor", ambientLightColor);

        float x = map_width / -2;
        float y = map_height / 2;
        float width = map_width;
        float height = -map_height;
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
}
