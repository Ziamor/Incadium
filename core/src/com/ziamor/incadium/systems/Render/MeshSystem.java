package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ziamor.incadium.components.Render.MeshComponent;
import com.ziamor.incadium.components.TransformComponent;


public class MeshSystem extends IteratingSystem {
    private ComponentMapper<MeshComponent> meshComponentMapper;
    private ComponentMapper<TransformComponent> transformComponentMapper;

    public MeshSystem() {
        super(Aspect.all(MeshComponent.class, TransformComponent.class));
    }

    @Override
    protected void process(int entityId) {
        final MeshComponent meshComponent = meshComponentMapper.get(entityId);
        final TransformComponent transformComponent = transformComponentMapper.get(entityId);

        if (meshComponent.mesh == null) {
            meshComponent.mesh = new com.badlogic.gdx.graphics.Mesh(true, 6, 0,
                    new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
                    new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
        }

        float x = transformComponent.x;
        float y = transformComponent.y;
        float width = 1;
        float height = 1;

        float[] verts = new float[30];
        int i = 0;

        //Top Left Vertex Triangle 1
        verts[i++] = x;   //X
        verts[i++] = y + height; //Y
        verts[i++] = 0;    //Z
        verts[i++] = 0f;   //U
        verts[i++] = 0f;   //V

        //Top Right Vertex Triangle 1
        verts[i++] = x + width;
        verts[i++] = y + height;
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
        verts[i++] = x + width;
        verts[i++] = y + height;
        verts[i++] = 0;
        verts[i++] = 1f;
        verts[i++] = 0f;

        //Bottom Right Vertex Triangle 2
        verts[i++] = x + width;
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

        meshComponent.mesh.setVertices(verts);
    }
}
