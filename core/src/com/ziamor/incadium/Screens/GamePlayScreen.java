package com.ziamor.incadium.Screens;

import com.artemis.SuperMapper;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.link.EntityLinkManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziamor.incadium.DecorFactory;
import com.ziamor.incadium.ItemFactory;
import com.ziamor.incadium.components.NonComponents.HealthBarUI;
import com.ziamor.incadium.Incadium;
import com.ziamor.incadium.systems.Combat.AttackSystem;
import com.ziamor.incadium.systems.Render.AnimationSystem;
import com.ziamor.incadium.systems.TargetCameraSystem;
import com.ziamor.incadium.systems.UI.HealthBarUISystem;
import com.ziamor.incadium.systems.Util.BlockPlayerInputSystem;
import com.ziamor.incadium.systems.Combat.DeathSystem;
import com.ziamor.incadium.systems.Movement.FollowSystem;
import com.ziamor.incadium.systems.Stats.HealthSystem;
import com.ziamor.incadium.systems.Combat.LootSystem;
import com.ziamor.incadium.systems.Util.MapSystem;
import com.ziamor.incadium.systems.Movement.MovementSystem;
import com.ziamor.incadium.systems.Movement.PlayerControllerSystem;
import com.ziamor.incadium.systems.Debug.PlayerStateSystem;
import com.ziamor.incadium.systems.Render.RenderSystem;
import com.ziamor.incadium.systems.Render.TerrainRenderSystem;
import com.ziamor.incadium.systems.Util.MovementLerpSystem;
import com.ziamor.incadium.systems.Util.TurnSchedulerSystem;
import com.ziamor.incadium.components.NonComponents.Gradient;

public class GamePlayScreen implements Screen {
    public static final int SO_TURN = 0;
    public static final int SO_INPUT = 1;
    public static final int SO_MOVEMENT = 2;
    public static final int SO_COMBAT = 3;
    public static final int SO_RENDER = 4;

    final float map_width = 16, map_height = 9;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    AssetManager assetManager;
    OrthographicCamera camera;
    Viewport viewport;

    World world;

    Stage stage;
    Table table;
    Skin skin;

    HealthBarUI healthBarUI;
    Label lbFPS;

    public GamePlayScreen(final Incadium incadium) {
        batch = incadium.batch;
        shapeRenderer = incadium.shapeRenderer;
        assetManager = incadium.assetManager;
        skin = incadium.skin;

        camera = new OrthographicCamera();
        viewport = new FitViewport(map_width, map_height, camera);
        camera.translate(map_width / 2, map_height / 2);

        // Sanity check to force anything unloaded to finish
        assetManager.finishLoading();

        DecorFactory.setTexture(assetManager.get("Decor.png", Texture.class));
        ItemFactory.setTexture(assetManager.get("Item.png", Texture.class));

        constructUI();

        WorldConfiguration config = new WorldConfigurationBuilder().with(
                new SuperMapper(),
                new TagManager(),
                new EntityLinkManager(),
                // Setup Systems
                new MapSystem(),
                // Render Systems
                new AnimationSystem(),
                new TerrainRenderSystem(batch),
                new RenderSystem(batch),
                new TargetCameraSystem(camera),
                // Input Systems
                new PlayerControllerSystem(),
                new BlockPlayerInputSystem(),
                new TurnSchedulerSystem(),
                // Movement Systems
                new MovementSystem(),
                new MovementLerpSystem(),
                new FollowSystem(),
                // Attack Systems
                new AttackSystem(),
                //Health System
                new HealthSystem(),
                new LootSystem(),
                new DeathSystem(),
                //UI
                new HealthBarUISystem(healthBarUI),
                //Debug Systems
                new PlayerStateSystem()
        ).build().register(assetManager);
        world = new World(config);
    }

    public void constructUI() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setFillParent(true);
        table.top();
        table.left();
        stage.addActor(table);
        table.add(new Label("Health:", skin));
        healthBarUI = new HealthBarUI(skin, new Gradient(Color.RED, Color.GREEN).addPoint(Color.YELLOW, 0.5f), 100);
        table.add(healthBarUI);
        lbFPS = new Label("FPS: ",skin);
        table.add().expandX();
        table.add(lbFPS).right();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        lbFPS.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        world.setDelta(delta);
        world.process();
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
