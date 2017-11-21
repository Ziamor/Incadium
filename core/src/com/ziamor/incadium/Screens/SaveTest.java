package com.ziamor.incadium.Screens;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziamor.incadium.Incadium;
import com.ziamor.incadium.components.Asset.TextureResolverComponent;
import com.ziamor.incadium.components.MonsterComponent;
import com.ziamor.incadium.components.Movement.PlayerControllerComponent;
import com.ziamor.incadium.components.Render.TextureComponent;
import com.ziamor.incadium.components.TargetCameraFocusComponent;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.components.TurnComponent;
import com.ziamor.incadium.systems.Asset.AnimationResolverSystem;
import com.ziamor.incadium.systems.Asset.TextureResolverSystem;
import com.ziamor.incadium.systems.Movement.FollowSystem;
import com.ziamor.incadium.systems.Movement.MovementSystem;
import com.ziamor.incadium.systems.Movement.PathFindingSystem;
import com.ziamor.incadium.systems.Movement.PlayerControllerSystem;
import com.ziamor.incadium.systems.Render.AnimationSystem;
import com.ziamor.incadium.systems.Render.RenderSystem;
import com.ziamor.incadium.systems.Render.SlimeAnimationControllerSystem;
import com.ziamor.incadium.systems.Render.TerrainRenderSystem;
import com.ziamor.incadium.systems.Render.TargetCameraSystem;
import com.ziamor.incadium.systems.Util.DurationManagerSystem;
import com.ziamor.incadium.systems.Util.MapSystem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class SaveTest implements Screen {
    final float map_width = 16, map_height = 9;
    Incadium incadium;
    SpriteBatch batch;

    AssetManager assetManager;

    OrthographicCamera camera;
    Viewport viewport;
    WorldConfiguration config;

    World world;

    private ComponentMapper<TransformComponent> transformComponentMapper;
    private ComponentMapper<TextureComponent> textureComponentMapper;
    private ComponentMapper<TextureResolverComponent> textureResolverComponentMapper;
    private ComponentMapper<TargetCameraFocusComponent> targetCameraFocusComponentMapper;

    WorldSerializationManager worldSerializationManager;

    int ePlayer;

    public SaveTest(final Incadium incadium) {
        this.incadium = incadium;
        assetManager = incadium.assetManager;
        batch = incadium.batch;
        camera = new OrthographicCamera();
        viewport = new FitViewport(map_width, map_height, camera);
        camera.translate(map_width / 2, map_height / 2);

        worldSerializationManager = new WorldSerializationManager();
        config = new WorldConfigurationBuilder().with(
                new SuperMapper(),
                new TagManager(),
                new EntityLinkManager(),
                worldSerializationManager,
                new MapSystem(),
                new TextureResolverSystem(),
                new AnimationResolverSystem(),
                new SlimeAnimationControllerSystem(),
                new AnimationSystem(),
                new TerrainRenderSystem(),
                new RenderSystem(),
                new TargetCameraSystem(),
                new PlayerControllerSystem(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()),
                new DurationManagerSystem(),
                new PathFindingSystem(),
                new FollowSystem(),
                new MovementSystem()
        ).build().register(assetManager);
        world = new World(config);

        JsonArtemisSerializer backend = new JsonArtemisSerializer(world);
        backend.prettyPrint(true);
        worldSerializationManager.setSerializer(backend);

        transformComponentMapper = world.getMapper(TransformComponent.class);
        textureComponentMapper = world.getMapper(TextureComponent.class);
        textureResolverComponentMapper = world.getMapper(TextureResolverComponent.class);
        targetCameraFocusComponentMapper = world.getMapper(TargetCameraFocusComponent.class);

        boolean initEnt = false;
        if (initEnt) {
            ePlayer = world.create();
            E.E(ePlayer).tag("player")
                    .textureResolverComponent("player.png")
                    .transformComponent(1, 1, 4)
                    .movementComponent()
                    .attackDamageComponent(25)
                    .healthComponentHealthStat(500, 500)
                    .playerControllerComponent()
                    .turnTakerComponent()
                    .healthBarUIComponent(ePlayer)
                    .turnComponent()
                    .factionComponent(0)
                    .targetCameraFocusComponent();
        }

        world.process();
        FileHandle file = Gdx.files.local("/level.json");

        boolean loadfile = true;
        boolean savefile = false;

        if (loadfile) {
            try {
                InputStream is = new FileInputStream(file.file());
                SaveFileFormat load = worldSerializationManager.load(is, SaveFileFormat.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (savefile) {
            world.process();
            IntBag entities = world.getAspectSubscriptionManager().get(Aspect.one(PlayerControllerComponent.class, MonsterComponent.class)).getEntities();
            try {
                OutputStream out = new FileOutputStream(file.file());
                worldSerializationManager.save(out, new SaveFileFormat(entities));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        ePlayer = world.getSystem(TagManager.class).getEntityId("player");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        assetManager.update();
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        world.setDelta(delta);
        TurnComponent turnComponent = world.getMapper(TurnComponent.class).create(ePlayer);
        turnComponent.finishedTurn = false;
        world.process();
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
