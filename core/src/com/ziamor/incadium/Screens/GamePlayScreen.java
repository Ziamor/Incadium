package com.ziamor.incadium.Screens;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.Entity;
import com.artemis.SuperMapper;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.io.JsonArtemisSerializer;
import com.artemis.io.SaveFileFormat;
import com.artemis.link.EntityLinkManager;
import com.artemis.managers.TagManager;
import com.artemis.managers.WorldSerializationManager;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
import com.ziamor.incadium.IncadiumInvocationStrategy;
import com.ziamor.incadium.SystemSetupBuilder;
import com.ziamor.incadium.components.ItemComponent;
import com.ziamor.incadium.components.MapComponent;
import com.ziamor.incadium.components.MonsterComponent;
import com.ziamor.incadium.components.Movement.AttackLerpComponent;
import com.ziamor.incadium.components.Movement.MovementLerpComponent;
import com.ziamor.incadium.components.Movement.PlayerControllerComponent;
import com.ziamor.incadium.components.NonComponents.HealthBarUI;
import com.ziamor.incadium.Incadium;
import com.ziamor.incadium.components.Render.LightSourceComponent;
import com.ziamor.incadium.components.Render.RenderPositionComponent;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.systems.ActiveEntitySystem;
import com.ziamor.incadium.systems.Asset.MapResolverSystem;
import com.ziamor.incadium.systems.Render.GroundRenderSystem;
import com.ziamor.incadium.systems.Render.LightRenderSystem;
import com.ziamor.incadium.systems.Render.OutLineRenderSystem;
import com.ziamor.incadium.systems.Render.RenderPositionSystem;
import com.ziamor.incadium.components.TurnComponent;
import com.ziamor.incadium.components.TurnTakerComponent;
import com.ziamor.incadium.systems.Asset.AnimationResolverSystem;
import com.ziamor.incadium.systems.Asset.ShaderResolverSystem;
import com.ziamor.incadium.systems.Asset.TextureRegionResolverSystem;
import com.ziamor.incadium.systems.Combat.AttackCoolDownSystem;
import com.ziamor.incadium.systems.Combat.AttackSystem;
import com.ziamor.incadium.systems.Combat.TookDamageSystem;
import com.ziamor.incadium.systems.Movement.PathFindingSystem;
import com.ziamor.incadium.systems.Render.AnimationSystem;
import com.ziamor.incadium.systems.Render.RenderPositionInitSystem;
import com.ziamor.incadium.systems.Render.SlimeAnimationControllerSystem;
import com.ziamor.incadium.systems.Render.VisibilitySystem;
import com.ziamor.incadium.systems.Render.TargetCameraSystem;
import com.ziamor.incadium.systems.UI.AttackCooldownBarRender;
import com.ziamor.incadium.systems.UI.HealthBarUISystem;
import com.ziamor.incadium.systems.Combat.DeathSystem;
import com.ziamor.incadium.systems.Movement.FollowSystem;
import com.ziamor.incadium.systems.Stats.HealthSystem;
import com.ziamor.incadium.systems.Combat.LootSystem;
import com.ziamor.incadium.systems.UI.SelectSystem;
import com.ziamor.incadium.systems.Util.MapSystem;
import com.ziamor.incadium.systems.Movement.MovementSystem;
import com.ziamor.incadium.systems.Movement.PlayerControllerSystem;
import com.ziamor.incadium.systems.Debug.PlayerStateSystem;
import com.ziamor.incadium.systems.Render.RenderSystem;
import com.ziamor.incadium.systems.Render.TerrainRenderSystem;
import com.ziamor.incadium.systems.Util.DurationManagerSystem;
import com.ziamor.incadium.components.NonComponents.Gradient;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GamePlayScreen implements Screen {
    public  static  final float map_width = 16, map_height = 9; // This prob should'nt be static
    final int viabilityRange = 8;
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
    Label lbFPS;

    IncadiumInvocationStrategy incadiumInvocationStrategy;
    WorldSerializationManager worldSerializationManager;
    int frame = 0;

    boolean saveGameOnExit = false;

    private ComponentMapper<MovementLerpComponent> movementLerpComponentMapper;
    private ComponentMapper<AttackLerpComponent> attackLerpComponentMapper;

    private boolean playerTurn = true;
    public static FrameBuffer fbWorld; // This should not be static, find a better way to pass it to the light system

    boolean drawGrid = false;

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
        camera.update();

        this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Make sure everything is loaded
        assetManager.finishLoading();

        constructUI();

        buildWorld();

        JsonArtemisSerializer backend = new JsonArtemisSerializer(world);
        backend.prettyPrint(true);
        worldSerializationManager.setSerializer(backend);

        inputMultiplexer.addProcessor(new GestureDetector(world.getSystem(SelectSystem.class)));
        inputMultiplexer.addProcessor(new GestureDetector(world.getSystem(PlayerControllerSystem.class)));

        E.E().mapResolverComponent(0x123);

        movementLerpComponentMapper = world.getMapper(MovementLerpComponent.class);
        attackLerpComponentMapper = world.getMapper(AttackLerpComponent.class);
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
    }

    @Override
    public void render(float delta) {
        assetManager.update();

        lbFPS.setText("FPS: " + Gdx.graphics.getFramesPerSecond());

        fbWorld.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        batch.setColor(Color.WHITE);
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        executeTurn(delta);
        fbWorld.end();

        if (drawGrid) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            for (int i = 0; i < Gdx.graphics.getHeight(); i++) {
                shapeRenderer.line(0, i, Gdx.graphics.getWidth(), i);
            }
            for (int i = 0; i < Gdx.graphics.getWidth(); i++) {
                shapeRenderer.line(i, 0, i, Gdx.graphics.getHeight());
            }
            shapeRenderer.end();
        }

        stage.act(delta);
        stage.draw();

        //Gdx.app.log("", "frame: " + frame++);
    }

    protected void executeTurn(float delta) {
        world.setDelta(delta);

        incadiumInvocationStrategy.phase = IncadiumInvocationStrategy.Phase.Render;
        world.process();

        incadiumInvocationStrategy.phase = IncadiumInvocationStrategy.Phase.Turn;
        ComponentMapper<TurnComponent> turnComponentMapper = world.getMapper(TurnComponent.class);

        Entity playerEnt = world.getSystem(TagManager.class).getEntity("player");

        TurnComponent playerTurnComponent = null;

        if (playerEnt != null) {
            IntBag npcLerp = world.getAspectSubscriptionManager().get(Aspect.all(MonsterComponent.class, AttackLerpComponent.class)).getEntities();
            // Execute the players turn
            if (playerTurn) {
                if (npcLerp.isEmpty()) {
                    playerTurnComponent = turnComponentMapper.get(playerEnt);
                    if (playerTurnComponent == null)
                        turnComponentMapper.create(playerEnt);

                    // If player isn't finished with their turn
                    if (playerTurnComponent != null && !playerTurnComponent.finishedTurn)
                        world.process();

                    // Players turn is done
                    if (playerTurnComponent != null && playerTurnComponent.finishedTurn) {
                        turnComponentMapper.remove(playerEnt);
                        playerTurn = false;
                    }
                }
            } else {
                MovementLerpComponent playerMovementLerp = movementLerpComponentMapper.get(playerEnt);
                AttackLerpComponent playerAttackLerpComponent = attackLerpComponentMapper.get(playerEnt);
                if (playerMovementLerp == null && playerAttackLerpComponent == null) {
                    // Execute NPC turn when it's not the players turn
                    if (playerTurnComponent == null && !playerTurn) {
                        IntBag turnTakersIDs = world.getAspectSubscriptionManager().get(Aspect.one(TurnTakerComponent.class).exclude(PlayerControllerComponent.class)).getEntities();
                        for (int i = 0; i < turnTakersIDs.size(); i++) {
                            int currentEnt = turnTakersIDs.get(i);
                            turnComponentMapper.create(currentEnt);
                            world.process();
                            turnComponentMapper.remove(currentEnt);
                        }
                        playerTurn = true;
                    }
                }
            }
        }
    }

    protected void loadFromSave(){
         try {
            if (Gdx.files.isLocalStorageAvailable()) {
                FileHandle file = Gdx.files.internal("level.json");
                String data = file.readString(); //TODO load files correctly, android was having issues with directly loading the file into the input stream
                InputStream is = new ByteArrayInputStream(data.getBytes());
                SaveFileFormat load = worldSerializationManager.load(is, SaveFileFormat.class);
                is.close();
            } else
                Gdx.app.debug("Main", "Internal file storage not available");
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
        fbWorld = new FrameBuffer(Pixmap.Format.RGBA8888, viewport.getScreenWidth(), viewport.getScreenHeight(), false);
        if (world != null) {
            LightRenderSystem lightRenderSystem = world.getSystem(LightRenderSystem.class);
            if (lightRenderSystem != null)
                lightRenderSystem.resize(width, height);
        }
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
        if (saveGameOnExit) {
            IntBag entities = world.getAspectSubscriptionManager().get(Aspect.one(PlayerControllerComponent.class, MonsterComponent.class, MapComponent.class, ItemComponent.class)).getEntities();
            try {
                FileHandle file = Gdx.files.local("/level.json");
                OutputStream out = new FileOutputStream(file.file());
                worldSerializationManager.save(out, new SaveFileFormat(entities));
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stage.dispose();
        // Note to future self, Incadium disposes of asset manager and sprite batch for you
    }

    public void buildWorld() {
        worldSerializationManager = new WorldSerializationManager();
        SystemSetupBuilder systemSetupBuilder = new SystemSetupBuilder();

        // Mandatory
        systemSetupBuilder.add(new SuperMapper(), "mandatory");
        systemSetupBuilder.add(new TagManager(), "mandatory");
        systemSetupBuilder.add(new EntityLinkManager(), "mandatory");
        systemSetupBuilder.add(worldSerializationManager, "mandatory");

        systemSetupBuilder.add(new TextureRegionResolverSystem(), "mandatory");
        systemSetupBuilder.add(new AnimationResolverSystem(), "mandatory");
        systemSetupBuilder.add(new ShaderResolverSystem(), "mandatory");
        systemSetupBuilder.add(new MapResolverSystem(), "mandatory");

        // Setup Systems
        systemSetupBuilder.add(new MapSystem(), "render");
        // Render Systems;
        systemSetupBuilder.add(new RenderPositionInitSystem(), "render");
        systemSetupBuilder.add(new RenderPositionSystem(), "render");
        systemSetupBuilder.add(new VisibilitySystem(viabilityRange), "render");

        systemSetupBuilder.add(new SlimeAnimationControllerSystem(), "render");
        systemSetupBuilder.add(new AnimationSystem(), "render");
        systemSetupBuilder.add(new GroundRenderSystem(), "render");
        systemSetupBuilder.add(new TerrainRenderSystem(), "render");
        systemSetupBuilder.add(new RenderSystem(), "render");
        systemSetupBuilder.add(new LightRenderSystem(), "render");
        systemSetupBuilder.add(new OutLineRenderSystem(), "render");
        systemSetupBuilder.add(new TargetCameraSystem(), "render"); //TODO should this be earlier?

        // Input Systems
        systemSetupBuilder.add(new PlayerControllerSystem(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), "turn");
        //new TurnSchedulerSystem(),
        // Movement Systems
        systemSetupBuilder.add(new DurationManagerSystem(), "render");
        systemSetupBuilder.add(new ActiveEntitySystem(), "turn");
        systemSetupBuilder.add(new PathFindingSystem(), "turn");
        systemSetupBuilder.add(new FollowSystem(), "turn");
        systemSetupBuilder.add(new MovementSystem(), "turn");
        // Attack Systems
        systemSetupBuilder.add(new AttackCoolDownSystem(), "render");
        systemSetupBuilder.add(new TookDamageSystem(), "render");
        systemSetupBuilder.add(new AttackSystem(), "turn");
        //Health System
        systemSetupBuilder.add(new HealthSystem(), "turn");
        systemSetupBuilder.add(new LootSystem(), "turn");
        systemSetupBuilder.add(new DeathSystem(), "turn");
        //UI
        systemSetupBuilder.add(new SelectSystem(viewport), "render");
        systemSetupBuilder.add(new HealthBarUISystem(healthBarUI), "render");
        systemSetupBuilder.add(new AttackCooldownBarRender(attackCoolDownBar), "render");
        //Debug Systems
        systemSetupBuilder.add(new PlayerStateSystem(), "turn");
        //new DrawCurrentTurnTakerSystem(shapeRenderer)

        config = new WorldConfigurationBuilder()
                .with(systemSetupBuilder.getSystemArray())
                .build()
                .register(batch)
                .register(shapeRenderer)
                .register(camera)
                .register(viewport)
                .register(assetManager);
        incadiumInvocationStrategy = new IncadiumInvocationStrategy();
        config.setInvocationStrategy(incadiumInvocationStrategy);
        world = new World(config);

        incadiumInvocationStrategy.setMandatorySystems(systemSetupBuilder.getGroup("mandatory"));
        incadiumInvocationStrategy.setRenderSystems(systemSetupBuilder.getGroup("render"));
        incadiumInvocationStrategy.setTurnSystems(systemSetupBuilder.getGroup("turn"));
    }
}
