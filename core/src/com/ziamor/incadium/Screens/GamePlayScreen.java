package com.ziamor.incadium.Screens;

import com.artemis.Aspect;
import com.artemis.AspectSubscriptionManager;
import com.artemis.ComponentManager;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.Entity;
import com.artemis.EntityManager;
import com.artemis.SuperMapper;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.link.EntityLinkManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziamor.incadium.DecorFactory;
import com.ziamor.incadium.ItemFactory;
import com.ziamor.incadium.IncadiumInvocationStrategy;
import com.ziamor.incadium.components.Combat.DeadComponent;
import com.ziamor.incadium.components.MonsterComponent;
import com.ziamor.incadium.components.Movement.IdleTurnComponent;
import com.ziamor.incadium.components.Movement.PlayerControllerComponent;
import com.ziamor.incadium.components.NonComponents.HealthBarUI;
import com.ziamor.incadium.Incadium;
import com.ziamor.incadium.components.TurnComponent;
import com.ziamor.incadium.components.TurnTakerComponent;
import com.ziamor.incadium.systems.Combat.AttackCoolDownSystem;
import com.ziamor.incadium.systems.Combat.AttackSystem;
import com.ziamor.incadium.systems.Debug.DrawCurrentTurnTakerSystem;
import com.ziamor.incadium.systems.Movement.PathFindingSystem;
import com.ziamor.incadium.systems.Render.AnimationSystem;
import com.ziamor.incadium.systems.Render.VisibilitySystem;
import com.ziamor.incadium.systems.TargetCameraSystem;
import com.ziamor.incadium.systems.UI.AttackCooldownBarRender;
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
    final float map_width = 16, map_height = 9;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    AssetManager assetManager;
    OrthographicCamera camera;
    Viewport viewport;
    InputMultiplexer inputMultiplexer;

    WorldConfiguration config;
    World world;

    Stage stage;
    Table table;
    Skin skin;

    HealthBarUI healthBarUI;
    ProgressBar attackCoolDownBar;
    Touchpad touchpad;
    Label lbFPS;

    IncadiumInvocationStrategy incadiumInvocationStrategy;

    int frame = 0;

    public GamePlayScreen(final Incadium incadium) {
        batch = incadium.batch;
        shapeRenderer = incadium.shapeRenderer;
        assetManager = incadium.assetManager;
        skin = incadium.skin;

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        camera = new OrthographicCamera();
        viewport = new FitViewport(map_width, map_height, camera);
        camera.translate(map_width / 2, map_height / 2);

        // Sanity check to force anything unloaded to finish
        assetManager.finishLoading();

        DecorFactory.setTexture(assetManager.get("Decor.png", Texture.class));
        ItemFactory.setTexture(assetManager.get("Item.png", Texture.class));

        constructUI();

        config = new WorldConfigurationBuilder().with(
                new SuperMapper(),
                new TagManager(),
                new EntityLinkManager(),
                // Setup Systems
                new MapSystem(),
                // Render Systems
                new VisibilitySystem(5),
                new AnimationSystem(),
                new TerrainRenderSystem(batch),
                new RenderSystem(batch),
                new TargetCameraSystem(camera),
                // Input Systems
                new PlayerControllerSystem(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()),
                new BlockPlayerInputSystem(),
                //new TurnSchedulerSystem(),
                // Movement Systems
                new MovementLerpSystem(),
                new PathFindingSystem(),
                new FollowSystem(),
                new MovementSystem(),
                // Attack Systems
                new AttackCoolDownSystem(),
                new AttackSystem(),
                //Health System
                new HealthSystem(),
                new LootSystem(),
                new DeathSystem(),
                //UI
                new HealthBarUISystem(healthBarUI),
                new AttackCooldownBarRender(attackCoolDownBar),
                //Debug Systems
                new PlayerStateSystem(),
                new DrawCurrentTurnTakerSystem(shapeRenderer)
        ).build().register(assetManager);
        incadiumInvocationStrategy = new IncadiumInvocationStrategy();
        config.setInvocationStrategy(incadiumInvocationStrategy);
        world = new World(config);

        incadiumInvocationStrategy.setMandatorySystems(SuperMapper.class, TagManager.class, EntityLinkManager.class, ComponentManager.class, EntityManager.class, AspectSubscriptionManager.class);

        incadiumInvocationStrategy.setRenderSystems(MapSystem.class, RenderSystem.class,
                TargetCameraSystem.class, TerrainRenderSystem.class, AnimationSystem.class,
                VisibilitySystem.class, MovementLerpSystem.class, HealthBarUISystem.class, AttackCooldownBarRender.class, AttackCoolDownSystem.class);//TODO move lerp anc cooldown system

        incadiumInvocationStrategy.setTurnSystems(PlayerControllerSystem.class, BlockPlayerInputSystem.class, TurnSchedulerSystem.class, MovementSystem.class,
                FollowSystem.class, PathFindingSystem.class, DrawCurrentTurnTakerSystem.class, AttackSystem.class, HealthSystem.class, DeathSystem.class);

        incadiumInvocationStrategy.setPostTurnSystems();
        inputMultiplexer.addProcessor(new GestureDetector(world.getSystem(PlayerControllerSystem.class)));
    }

    public void constructUI() {
        stage = new Stage();
        inputMultiplexer.addProcessor(stage);
        table = new Table();
        table.setFillParent(true);
        table.top();
        table.left();
        stage.addActor(table);
        table.add(new Label("Health:", skin));
        healthBarUI = new HealthBarUI(skin, new Gradient(Color.RED, Color.GREEN).addPoint(Color.YELLOW, 0.5f), 100);
        table.add(healthBarUI);
        lbFPS = new Label("FPS: ", skin);
        table.add().expandX();
        table.add(lbFPS).right();
        table.row();
        attackCoolDownBar = new ProgressBar(0, 0.25f, 0.01f, false, skin);
        table.add(new Label("Attack CD:", skin));
        table.add(attackCoolDownBar);
        table.row().expandY();
        touchpad = new Touchpad(100, skin);
        //table.add(touchpad);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        lbFPS.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        world.setDelta(delta);

        incadiumInvocationStrategy.phase = IncadiumInvocationStrategy.Phase.Render;
        world.process();

        incadiumInvocationStrategy.phase = IncadiumInvocationStrategy.Phase.Turn;
        ComponentMapper<TurnComponent> turnComponentMapper = world.getMapper(TurnComponent.class);
        ComponentMapper<DeadComponent> deadComponentMapper = world.getMapper(DeadComponent.class);

        Entity playerEnt = world.getSystem(TagManager.class).getEntity("player");

        TurnComponent playerTurnComponent = null;

        // Execute the players turn
        if (playerEnt != null) {
            playerTurnComponent = turnComponentMapper.get(playerEnt);
            if (playerTurnComponent == null)
                turnComponentMapper.create(playerEnt);

            // If player isn't finished with their turn
            if (playerTurnComponent != null && !playerTurnComponent.finishedTurn)
                world.process();

            // Players turn is done
            if (playerTurnComponent != null && playerTurnComponent.finishedTurn) {
                turnComponentMapper.remove(playerEnt);
                playerTurnComponent = null;
            }

        }

        // Execute NPC turn when it's not the players turn
        if (playerTurnComponent == null) {
            IntBag turnTakersIDs = world.getAspectSubscriptionManager().get(Aspect.one(TurnTakerComponent.class).exclude(PlayerControllerComponent.class)).getEntities();
            for (int i = 0; i < turnTakersIDs.size(); i++) {
                int currentEnt = turnTakersIDs.get(i);
                turnComponentMapper.create(currentEnt);
                world.process();
                turnComponentMapper.remove(currentEnt);
            }
        }

        incadiumInvocationStrategy.phase = IncadiumInvocationStrategy.Phase.POST_TURN;
        world.process();

        stage.act(delta);
        stage.draw();

        //Gdx.app.log("", "frame: " + frame++);
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
