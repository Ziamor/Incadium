package com.ziamor.incadium;

import com.artemis.ComponentMapper;
import com.artemis.SuperMapper;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziamor.incadium.components.MovementComponent;
import com.ziamor.incadium.components.MovementLerpComponent;
import com.ziamor.incadium.components.PlayerControllerComponent;
import com.ziamor.incadium.components.TextureComponent;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.components.TurnComponent;
import com.ziamor.incadium.systems.AttackSystem;
import com.ziamor.incadium.systems.MapSystem;
import com.ziamor.incadium.systems.MovementSystem;
import com.ziamor.incadium.systems.PlayerControllerSystem;
import com.ziamor.incadium.systems.RenderSystem;
import com.artemis.E;
import com.ziamor.incadium.systems.TerrainRenderSystem;

public class IncadiumGame extends ApplicationAdapter {
    public static final int SO_TURN = 0;
    public static final int SO_INPUT = 1;
    public static final int SO_MOVEMENT = 2;
    public static final int SO_COMBAT = 3;
    public static final int SO_RENDER = 4;

    final float map_width = 16, map_height = 9;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;

    OrthographicCamera camera;
    Viewport viewport;

    ComponentMapper<TextureComponent> textureComponentComponentMapper;
    ComponentMapper<TransformComponent> transformComponentComponentMapper;
    ComponentMapper<PlayerControllerComponent> playerControllerComponentComponentMapper;
    ComponentMapper<MovementComponent> movementComponentComponentMapper;
    ComponentMapper<TurnComponent> turnComponentComponentMapper;

    World world;

    int ePlayer;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera();
        viewport = new FitViewport(map_width, map_height, camera);
        camera.translate(map_width / 2, map_height / 2);

        WorldConfiguration config = new WorldConfigurationBuilder().with(new SuperMapper(), new MapSystem(), new TerrainRenderSystem(batch), new RenderSystem(batch), new PlayerControllerSystem(), new MovementSystem(), new AttackSystem()).build();
        world = new World(config);

        textureComponentComponentMapper = world.getMapper(TextureComponent.class);
        transformComponentComponentMapper = world.getMapper(TransformComponent.class);
        playerControllerComponentComponentMapper = world.getMapper(PlayerControllerComponent.class);
        movementComponentComponentMapper = world.getMapper(MovementComponent.class);
        turnComponentComponentMapper = world.getMapper(TurnComponent.class);

        ePlayer = world.create();
        E.E(ePlayer).textureComponent("player.png")
                .transformComponent(3, 3, 1)
                .movementComponent()
                .attackDamageComponent(20f)
                .healthComponentHealthStat(100f, 100f)
                .playerControllerComponent()
                .turnComponent();

        E.E().transformComponent(2, 2, 1).textureComponent("bat.png").healthComponentHealthStat(100f, 100f).movementComponent().turnComponent().monsterComponent();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        TransformComponent transformComponent = transformComponentComponentMapper.get(ePlayer);
        MovementLerpComponent movementLerpComponent = E.E(ePlayer).getMovementLerpComponent();

        if (movementLerpComponent != null) {
            Vector2 pos = movementLerpComponent.getCurrentPos();
            camera.position.x = pos.x;
            camera.position.y = pos.y;
        } else {
            camera.position.x = transformComponent.x;
            camera.position.y = transformComponent.y;
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        world.setDelta(Gdx.graphics.getDeltaTime());
        world.process();
        batch.end();

      /*  if (dijkstrasMap != null) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            //DijkstraMap.renderDijkstraMap(dijkstrasMap, shapeRenderer);

            /*Gradient g = new Gradient(new Color(0.3f, 0, 0.8f, 0.75f), new Color(1f, 0.6f, 0, 0.75f));
            g.addPoint(new Color(1f, 0.2f, 0.2f, 0.75f), 0.75f);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            float stop = 8;
            for (int i = 0; i < stop; i++) {
                shapeRenderer.setColor(g.getColor((float) i / stop));
                shapeRenderer.rect(i, 0, 1, 1);
            }
            shapeRenderer.end();

            Random rand = new Random(0x2398f3);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            BSPLeafIterator iterator = map.bsp.getLeafIterator();
            while (iterator.hasNext()) {
                Rectangle area = iterator.next().area;
                Color c = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.25f);
                shapeRenderer.setColor(c);
                shapeRenderer.rect(area.x, area.y, area.width, area.height);
            }
            shapeRenderer.end();
        }*/
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
