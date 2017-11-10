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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziamor.incadium.DecorFactory;
import com.ziamor.incadium.ItemFactory;
import com.ziamor.incadium.components.NonComponents.HealthBarUI;
import com.ziamor.incadium.Incadium;
import com.ziamor.incadium.components.Movement.MovementLerpComponent;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.systems.Combat.AttackSystem;
import com.ziamor.incadium.systems.Render.AnimationSystem;
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
import com.artemis.E;
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

    int ePlayer;
    //TODO remove
    ProgressBar healthBar;
    HealthBarUI healthBarUI;

    Texture playerTex, batText, slimeTex;

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
                new HealthBarUISystem(),
                //Debug Systems
                new PlayerStateSystem()
        ).build();
        world = new World(config);

        constructUI();

        ePlayer = world.createEntity().getId();
        E.E(ePlayer).tag("player")
                .textureComponent(assetManager.get("player.png", Texture.class))
                .transformComponent(3, 3, 4)
                .movementComponent()
                .attackDamageComponent(50f)
                .healthComponentHealthStat(100f, 100f)
                .playerControllerComponent()
                .turnTakerComponent()
                .healthBarUIComponent(ePlayer, healthBarUI)
                .turnComponent()
                .factionComponent(0);

        E.E().transformComponent(2, 2, 4)
                .textureComponent(assetManager.get("bat.png", Texture.class))
                .healthComponentHealthStat(100f, 100f)
                .movementComponent()
                .turnTakerComponent()
                .monsterComponent()
                .followTargetComponent(ePlayer)
                .lootableComponent()
                .attackDamageComponent(20f)
                .factionComponent(1);

        Texture slimeTexture = assetManager.get("Slime.png", Texture.class);
        TextureRegion[][] tmp = TextureRegion.split(slimeTexture, slimeTexture.getWidth() / 4, slimeTexture.getHeight());
        Animation<TextureRegion> walkAnimation = new Animation<TextureRegion>(0.1f, tmp[0]);

        E.E().transformComponent(3, 2, 4)
                .animationComponent(walkAnimation, 0)
                .healthComponentHealthStat(100f, 100f)
                .movementComponent()
                .turnTakerComponent()
                .monsterComponent()
                .followTargetComponent(ePlayer)
                .lootableComponent()
                .attackDamageComponent(15f)
                .factionComponent(1);
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
        /*healthBar = new ProgressBar(0, 100, 1, false, skin);
        healthBar.setAnimateDuration(1f);
        table.add(healthBar);*/
        healthBarUI = new HealthBarUI(skin, new Gradient(Color.RED, Color.GREEN).addPoint(Color.YELLOW, 0.5f), 100);
        table.add(healthBarUI);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        TransformComponent transformComponent = E.E(ePlayer).getTransformComponent();
        MovementLerpComponent movementLerpComponent = E.E(ePlayer).getMovementLerpComponent();

        if (movementLerpComponent != null) {
            Vector2 pos = movementLerpComponent.getCurrentPos();
            camera.position.x = pos.x;
            camera.position.y = pos.y;
        } else if (transformComponent != null) {
            camera.position.x = transformComponent.x;
            camera.position.y = transformComponent.y;
        }

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
