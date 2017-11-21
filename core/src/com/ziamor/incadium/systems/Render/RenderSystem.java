package com.ziamor.incadium.systems.Render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.ziamor.incadium.components.Movement.AttackLerpComponent;
import com.ziamor.incadium.components.Render.MeshComponent;
import com.ziamor.incadium.components.Render.NotVisableComponent;
import com.ziamor.incadium.components.Render.ShaderComponent;
import com.ziamor.incadium.components.Render.TextureRegionComponent;
import com.ziamor.incadium.components.Movement.MovementLerpComponent;
import com.ziamor.incadium.components.Render.TextureComponent;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.systems.Util.SortedIteratingSystem;

import java.util.Comparator;

public class RenderSystem extends SortedIteratingSystem {
    private ComponentMapper<TextureComponent> textureComponentComponentMapper;
    private ComponentMapper<TextureRegionComponent> textureRegionComponentComponentMapper;
    private ComponentMapper<TransformComponent> transformComponentComponentMapper;
    private ComponentMapper<MovementLerpComponent> movementLerpComponentComponentMapper;
    private ComponentMapper<AttackLerpComponent> attackLerpComponentMapper;
    private ComponentMapper<ShaderComponent> shaderComponentMapper;
    private ComponentMapper<MeshComponent> meshComponentMapper;
    @Wire
    private SpriteBatch batch;
    private Vector2 pos;
    TagManager tagManager;

    public RenderSystem() {
        super(Aspect.all(TransformComponent.class).one(TextureComponent.class, TextureRegionComponent.class).exclude(NotVisableComponent.class));
        this.pos = new Vector2();
    }

    @Override
    protected void process(int entityId) {
        final TextureComponent textureComponent = textureComponentComponentMapper.get(entityId);
        final TextureRegionComponent textureRegionComponent = textureRegionComponentComponentMapper.get(entityId);
        final TransformComponent transformComponent = transformComponentComponentMapper.get(entityId);
        final MovementLerpComponent movementLerpComponent = movementLerpComponentComponentMapper.get(entityId);
        final AttackLerpComponent attackLerpComponent = attackLerpComponentMapper.get(entityId);
        final ShaderComponent shaderComponent = shaderComponentMapper.get(entityId);

        // Get the render position
        if (movementLerpComponent != null)
            pos = movementLerpComponent.getCurrentPos(); // The sprite is moving between tiles
        else if (attackLerpComponent != null)
            pos = attackLerpComponent.getCurrentPos(); // The sprite is moving between tiles
        else {
            pos.set(transformComponent.x, transformComponent.y);
        }

        if (shaderComponent != null && textureComponent != null) {
            final MeshComponent meshComponent = meshComponentMapper.get(entityId);
            shaderComponent.time += world.getDelta();
            ShaderProgram shader = shaderComponent.shaderProgram;

            batch.end(); // TODO find a better way to render shader in between batchs
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
            textureComponent.texture.bind();
            shader.begin();
            //shader.pedantic = false;
            shader.setUniformMatrix("u_projTrans", batch.getProjectionMatrix());
            shader.setUniformi("u_texture", 0);
            shader.setUniformf("time", shaderComponent.time);
            shader.setUniformf("blink_rate", 0.25f);

            meshComponent.mesh.render(shader, GL20.GL_TRIANGLES);
            shader.end();
            batch.begin();
        } else {
            if (textureComponent != null) {
                if (textureComponent.texture == null) {
                    Gdx.app.debug("Render System", "Missing texture");
                    return;
                }
                batch.draw(textureComponent.texture, pos.x, pos.y, 1, 1);
            }
            if (textureRegionComponent != null) {
                if (textureRegionComponent.region == null) {
                    Gdx.app.debug("Render System", "Missing texture region");
                    return;
                }
                batch.draw(textureRegionComponent.region, pos.x, pos.y, 1, 1);
            }
        }
    }

    public Comparator<Integer> getComparator() {
        return new Comparator<Integer>() {
            @Override
            public int compare(Integer e1, Integer e2) {
                TransformComponent e1T = transformComponentComponentMapper.get(e1);
                TransformComponent e2T = transformComponentComponentMapper.get(e2);
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
}

