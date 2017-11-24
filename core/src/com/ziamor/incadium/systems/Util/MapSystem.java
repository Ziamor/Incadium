package com.ziamor.incadium.systems.Util;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.annotations.Wire;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ziamor.incadium.DecorFactory;
import com.ziamor.incadium.components.Asset.AnimationMetaData;
import com.ziamor.incadium.components.BlockingComponent;
import com.ziamor.incadium.components.MapComponent;
import com.ziamor.incadium.components.Render.GroundTileComponent;
import com.ziamor.incadium.components.Render.TerrainTileComponent;
import com.ziamor.incadium.components.Render.shaders.OutlineShaderComponent;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.utils.BSP;
import com.ziamor.incadium.utils.BSPLeafIterator;
import com.ziamor.incadium.utils.BSPPostorderIterator;

import java.beans.Transient;
import java.util.Random;

import static com.badlogic.gdx.Application.ApplicationType.Android;

//TODO helpful https://stackoverflow.com/questions/23222053/data-structure-for-tile-map-for-use-with-artemis
public class MapSystem extends BaseEntitySystem {
    private long seed;
    @Wire
    AssetManager assetManager;
    private int[] bitmaskLookup = {24, 24, 16, 16, 24, 24, 16, 16, 27, 27, 29, 19, 27, 27, 29, 19, 25, 25, 28, 28, 25, 25, 17, 17, 26, 26, 6, 36, 26, 26, 35, 18, 24, 24, 16, 16, 24, 24, 16, 16, 27, 27, 29, 19, 27, 27, 29, 19, 25, 25, 28, 28, 25, 25, 17, 17, 26, 26, 6, 36, 26, 26, 35, 18, 0, 0, 8, 8, 0, 0, 8, 8, 21, 21, 14, 41, 21, 21, 14, 41, 20, 20, 7, 7, 20, 20, 42, 42, 15, 15, 32, 31, 15, 15, 47, 37, 0, 0, 8, 8, 0, 0, 8, 8, 3, 3, 43, 11, 3, 3, 43, 11, 20, 20, 7, 7, 20, 20, 42, 42, 33, 33, 30, 46, 33, 33, 23, 13, 24, 24, 16, 16, 24, 24, 16, 16, 27, 27, 29, 19, 27, 27, 29, 19, 25, 25, 28, 28, 25, 25, 17, 17, 26, 26, 6, 36, 26, 26, 35, 18, 24, 24, 16, 16, 24, 24, 16, 16, 27, 27, 29, 19, 27, 27, 29, 19, 25, 25, 28, 28, 25, 25, 17, 17, 26, 26, 6, 36, 26, 26, 35, 18, 0, 0, 8, 8, 0, 0, 8, 8, 21, 21, 14, 41, 21, 21, 14, 41, 1, 1, 44, 44, 1, 1, 9, 9, 34, 34, 39, 22, 34, 34, 45, 12, 0, 0, 8, 8, 0, 0, 8, 8, 3, 3, 43, 11, 3, 3, 43, 11, 1, 1, 44, 44, 1, 1, 9, 9, 2, 2, 38, 5, 2, 2, 4, 10};
    private int[][] mapDataSource;

    private int[][] entityMap;
    public BSP bsp;
    private int map_width = 50, map_height = 50, tileset_width = 8;

    Texture tilesetTex = null;
    Texture groundTex = null;

    private ComponentMapper<TransformComponent> transformComponentComponentMapper;
    private ComponentMapper<TerrainTileComponent> terrainTileComponentComponentMapper;
    private ComponentMapper<BlockingComponent> blockingComponentComponentMapper;
    private ComponentMapper<GroundTileComponent> groundTileComponentComponentMapper;
    private ComponentMapper<MapComponent> mapComponentMapper;

    public MapSystem() {
        super(Aspect.all(MapComponent.class));
    }

    Random random;

    @Override
    protected void inserted(int entityId) {
        //TODO clear old map if it exists
        final MapComponent mapComponent = mapComponentMapper.get(entityId);
        seed = mapComponent.seed;
        random = new Random(seed);
        loadMap();
    }

    @Override
    protected void processSystem() {
    }

    protected void loadMap() {
        tilesetTex = assetManager.get("WallsBM.png", Texture.class);
        groundTex = assetManager.get("ground.png", Texture.class);
        genRandomDungeon();

        entityMap = new int[map_width][map_height];

        for (int i = 0; i < entityMap.length; i++)
            for (int j = 0; j < entityMap[i].length; j++) {
                int ent = createTile(mapDataSource[i][j], i, j);
                entityMap[i][j] = ent;
            }


        Vector2 pos = getFreeSpace();
        int ePlayer = world.createEntity().getId();
        E.E(ePlayer).tag("player")
                .textureRegionResolverComponent("player.png", 0, 0, 32, 32)
                .transformComponent(pos.x, pos.y, 4)
                .movementComponent()
                .attackDamageComponent(25)
                .healthComponentHealthStat(500, 500)
                .playerControllerComponent()
                .turnTakerComponent()
                .healthBarUIComponent(ePlayer)
                .turnComponent()
                .factionComponent(0)
                .targetCameraFocusComponent();

        int num_slimes = 50;
        if (Gdx.app.getType() == Android)
            num_slimes = 10;
        if (ePlayer != -1) {
            for (int i = 0; i < num_slimes; i++) {
                pos = getFreeSpace();
                AnimationMetaData[] animationMetaData = new AnimationMetaData[]{new AnimationMetaData(), new AnimationMetaData()};
                animationMetaData[0].set("walk", new int[]{0, 1, 2, 3}, 0.1f);
                animationMetaData[1].set("attack", new int[]{4, 5, 6, 7}, 0.1f);

                E.E().transformComponent(pos.x, pos.y, 4)
                        .animationResolverComponent("Slime.png", 4, 2, animationMetaData)
                        .healthComponentHealthStat(50, 50f)
                        .movementComponent()
                        .turnTakerComponent()
                        .monsterComponent()
                        .followTargetComponent(ePlayer)
                        .lootableComponent()
                        .attackDamageComponent(15f)
                        .factionComponent(1)
                        .selectableComponent()
                        .slimeAnimation();
            }
        }

    }

    protected int createTile(int id, int x, int y) {
        int ent = world.create();

        if (id != 1) {
            // Get bitmask
            int bitmask = 0;
            if (x > 0 && mapDataSource[x - 1][y] != 1)
                bitmask += 8;
            if (x < map_width - 1 && mapDataSource[x + 1][y] != 1)
                bitmask += 16;
            if (y > 0 && mapDataSource[x][y - 1] != 1)
                bitmask += 64;
            if (y < map_height - 1 && mapDataSource[x][y + 1] != 1)
                bitmask += 2;

            if (x > 0 && y > 0 && mapDataSource[x - 1][y - 1] != 1)
                bitmask += 32;
            if (x < map_width - 1 && y > 0 && mapDataSource[x + 1][y - 1] != 1)
                bitmask += 128;
            if (x > 0 && y < map_height - 1 && mapDataSource[x - 1][y + 1] != 1)
                bitmask += 1;
            if (x < map_width - 1 && y < map_height - 1 && mapDataSource[x + 1][y + 1] != 1)
                bitmask += 4;
            int index = bitmaskLookup[bitmask];

            terrainTileComponentComponentMapper.create(ent).setRegion(tilesetTex, index % tileset_width, index / tileset_width);
            blockingComponentComponentMapper.create(ent);

            //Randomly generate torches
            if (bitmask == 31) {
                if (random.nextFloat() <= 0.2f)
                    DecorFactory.Torch(x, y);
                else if (random.nextFloat() <= 0.1f)
                    DecorFactory.Chains(x, y);
                else if (random.nextFloat() <= 0.05f)
                    DecorFactory.Pipe(x, y);
                else if (random.nextFloat() <= 0.1f)
                    DecorFactory.Crack(x, y);
                else if (random.nextFloat() <= 0.1f)
                    DecorFactory.Blood(x, y);
            }
        }
        groundTileComponentComponentMapper.create(ent).groundTexture = groundTex;
        transformComponentComponentMapper.create(ent).set(x, y, 1);
        return ent;
    }

    public boolean isWallBlocking(int x, int y) {
        if (x >= entityMap.length || y >= entityMap[x].length)
            return true;
        BlockingComponent blockingComponent = blockingComponentComponentMapper.get(entityMap[x][y]);
        return blockingComponent != null;
    }

    public IntBag getBlockingEntities(int x, int y, Aspect.Builder builder) {
        IntBag blockers = new IntBag();
        if (builder == null)
            return blockers;
        IntBag possibleBlockers = world.getAspectSubscriptionManager().get(builder).getEntities();

        for (int i = 0; i < possibleBlockers.size(); i++) {
            TransformComponent entTransform = transformComponentComponentMapper.get(possibleBlockers.get(i));
            if (entTransform != null && entTransform.x == x && entTransform.y == y)
                blockers.add(possibleBlockers.get(i));
        }
        return blockers;
    }

    public Vector2 getFreeSpace() {
        int x = 0, y = 0;

        while (isWallBlocking(x, y)) {
            x = random.nextInt(map_width - 1);
            y = random.nextInt(map_height - 1);
        }

        return new Vector2(x, y);
    }

    public int getMapWidth() {
        if (entityMap == null)
            return -1;
        return entityMap.length;
    }

    public int getMapHeight() {
        if (entityMap == null)
            return -1;
        return entityMap[0].length;
    }

    protected void genRandomDungeon() {
        mapDataSource = new int[map_width][map_height];

        bsp = new BSP(map_width, map_height, 6, seed);

        BSPLeafIterator iterator = bsp.getLeafIterator();

        while (iterator.hasNext()) {
            Rectangle room = iterator.next().room;
            for (int i = 0; i < room.width; i++)
                for (int j = 0; j < room.height; j++) {
                    mapDataSource[i + (int) (room.x)][j + (int) (room.y)] = 1;
                }
        }

        BSPPostorderIterator postorderIterator = bsp.getPostOrderIterator();

        while (postorderIterator.hasNext()) {
            BSP.Node node = postorderIterator.next();

            if (node instanceof BSP.LeafNode)
                continue;

            Vector2 leftAreaMiddle = new Vector2();
            Vector2 rightAreaMiddle = new Vector2();
            Vector2 nodeAreamiddle = new Vector2();

            node.left.area.getCenter(leftAreaMiddle);
            node.right.area.getCenter(rightAreaMiddle);
            node.area.getCenter(nodeAreamiddle);

            if (node.verticalSplit) {
                for (int x = (int) leftAreaMiddle.x; x < rightAreaMiddle.x; x++) {
                    mapDataSource[x][(int) nodeAreamiddle.y] = 1;
                }
            } else {
                for (int y = (int) leftAreaMiddle.y; y < rightAreaMiddle.y; y++) {
                    mapDataSource[(int) nodeAreamiddle.x][y] = 1;
                }
            }
        }
    }

    protected void genBitmaskTest() {
        int col = 4;
        map_width = 4 * col;
        map_height = 4 * 64;
        mapDataSource = new int[map_width][map_height];

        for (int x = 0; x < map_width; x++)
            for (int y = 0; y < map_height; y++)
                mapDataSource[x][y] = 1;

        for (int i = 0; i < 256; i++) {
            int x = i % col * 4 + 1;
            int y = i / col * 4 + 1;
            mapDataSource[x][y + 2] = (i & 1) > 0 ? 0 : 1;
            mapDataSource[x + 1][y + 2] = (i & 2) > 0 ? 0 : 1;
            mapDataSource[x + 2][y + 2] = (i & 4) > 0 ? 0 : 1;
            mapDataSource[x][y + 1] = (i & 8) > 0 ? 0 : 1;
            mapDataSource[x + 2][y + 1] = (i & 16) > 0 ? 0 : 1;
            mapDataSource[x][y] = (i & 32) > 0 ? 0 : 1;
            mapDataSource[x + 1][y] = (i & 64) > 0 ? 0 : 1;
            mapDataSource[x + 2][y] = (i & 128) > 0 ? 0 : 1;
            mapDataSource[x + 1][y + 1] = 0;
        }
    }
}
