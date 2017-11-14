package com.ziamor.incadium.systems.Util;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ziamor.incadium.DecorFactory;
import com.ziamor.incadium.components.BlockingComponent;
import com.ziamor.incadium.components.Render.GroundTileComponent;
import com.ziamor.incadium.components.Render.TerrainTileComponent;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.systems.TargetCameraSystem;
import com.ziamor.incadium.utils.BSP;
import com.ziamor.incadium.utils.BSPLeafIterator;
import com.ziamor.incadium.utils.BSPPostorderIterator;

import java.util.Random;

//TODO helpfull https://stackoverflow.com/questions/23222053/data-structure-for-tile-map-for-use-with-artemis
public class MapSystem extends BaseSystem {
    @Wire
    AssetManager assetManager;
    private boolean loaded = false;
    private int[] bitmaskLookup = {24, 24, 16, 16, 24, 24, 16, 16, 27, 27, 29, 19, 27, 27, 29, 19, 25, 25, 28, 28, 25, 25, 17, 17, 26, 26, 6, 36, 26, 26, 35, 18, 24, 24, 16, 16, 24, 24, 16, 16, 27, 27, 29, 19, 27, 27, 29, 19, 25, 25, 28, 28, 25, 25, 17, 17, 26, 26, 6, 36, 26, 26, 35, 18, 0, 0, 8, 8, 0, 0, 8, 8, 21, 21, 14, 41, 21, 21, 14, 41, 20, 20, 7, 7, 20, 20, 42, 42, 15, 15, 32, 31, 15, 15, 47, 37, 0, 0, 8, 8, 0, 0, 8, 8, 3, 3, 43, 11, 3, 3, 43, 11, 20, 20, 7, 7, 20, 20, 42, 42, 33, 33, 30, 46, 33, 33, 23, 13, 24, 24, 16, 16, 24, 24, 16, 16, 27, 27, 29, 19, 27, 27, 29, 19, 25, 25, 28, 28, 25, 25, 17, 17, 26, 26, 6, 36, 26, 26, 35, 18, 24, 24, 16, 16, 24, 24, 16, 16, 27, 27, 29, 19, 27, 27, 29, 19, 25, 25, 28, 28, 25, 25, 17, 17, 26, 26, 6, 36, 26, 26, 35, 18, 0, 0, 8, 8, 0, 0, 8, 8, 21, 21, 14, 41, 21, 21, 14, 41, 1, 1, 44, 44, 1, 1, 9, 9, 34, 34, 39, 22, 34, 34, 45, 12, 0, 0, 8, 8, 0, 0, 8, 8, 3, 3, 43, 11, 3, 3, 43, 11, 1, 1, 44, 44, 1, 1, 9, 9, 2, 2, 38, 5, 2, 2, 4, 10};
    private int[][] map_datasource;

    private int[][] entitie_map;
    public BSP bsp;
    private int map_width = 50, map_height = 50, tileset_width = 8;

    Texture tilesetTex = null;
    Texture groundTex = null;

    ComponentMapper<TransformComponent> transformComponentComponentMapper;
    ComponentMapper<TerrainTileComponent> terrainTileComponentComponentMapper;
    ComponentMapper<BlockingComponent> blockingComponentComponentMapper;
    ComponentMapper<GroundTileComponent> groundTileComponentComponentMapper;

    @Override
    protected void processSystem() {
        if (!loaded) {
            loaded = true;
            loadMap();
        }
    }

    protected void loadMap() {
        tilesetTex = assetManager.get("WallsBM.png", Texture.class);
        groundTex = assetManager.get("ground.png", Texture.class);
        genRandomDungeon();

        entitie_map = new int[map_width][map_height];

        for (int i = 0; i < entitie_map.length; i++)
            for (int j = 0; j < entitie_map[i].length; j++) {
                int ent = createTile(map_datasource[i][j], i, j);
                entitie_map[i][j] = ent;
            }

        int ePlayer = world.createEntity().getId();
        Vector2 pos = getFreeSpace();
        E.E(ePlayer).tag("player")
                .textureComponent(assetManager.get("player.png", Texture.class))
                .transformComponent(pos.x, pos.y, 4)
                .movementComponent()
                .attackDamageComponent(50f)
                .healthComponentHealthStat(500, 500f)
                .playerControllerComponent()
                .turnTakerComponent()
                .healthBarUIComponent(ePlayer)
                .turnComponent()
                .factionComponent(0)
                .targetCameraFocusComponent();

        /*pos = getFreeSpace();
        E.E().transformComponent(pos.x, pos.y, 4)
                .textureComponent(assetManager.get("bat.png", Texture.class))
                .healthComponentHealthStat(100f, 100f)
                .movementComponent()
                .turnTakerComponent()
                .monsterComponent()
                .followTargetComponent(ePlayer)
                .lootableComponent()
                .attackDamageComponent(20f)
                .factionComponent(1);*/

        Texture slimeTexture = assetManager.get("Slime.png", Texture.class);
        TextureRegion[][] tmp = TextureRegion.split(slimeTexture, slimeTexture.getWidth() / 4, slimeTexture.getHeight());
        Animation<TextureRegion> walkAnimation = new Animation<TextureRegion>(0.1f, tmp[0]);

        for (int i = 0; i < 50; i++) {
            pos = getFreeSpace();
            E.E().transformComponent(pos.x, pos.y, 4)
                    .animationComponent(walkAnimation, 0)
                    .healthComponentHealthStat(50, 50f)
                    .movementComponent()
                    .turnTakerComponent()
                    .monsterComponent()
                    .followTargetComponent(ePlayer)
                    .lootableComponent()
                    .attackDamageComponent(15f)
                    .factionComponent(1);
        }
    }

    protected int createTile(int id, int x, int y) {
        int ent = world.create();

        if (id != 1) {
            // Get bitmask
            int bitmask = 0;
            if (x > 0 && map_datasource[x - 1][y] != 1)
                bitmask += 8;
            if (x < map_width - 1 && map_datasource[x + 1][y] != 1)
                bitmask += 16;
            if (y > 0 && map_datasource[x][y - 1] != 1)
                bitmask += 64;
            if (y < map_height - 1 && map_datasource[x][y + 1] != 1)
                bitmask += 2;

            if (x > 0 && y > 0 && map_datasource[x - 1][y - 1] != 1)
                bitmask += 32;
            if (x < map_width - 1 && y > 0 && map_datasource[x + 1][y - 1] != 1)
                bitmask += 128;
            if (x > 0 && y < map_height - 1 && map_datasource[x - 1][y + 1] != 1)
                bitmask += 1;
            if (x < map_width - 1 && y < map_height - 1 && map_datasource[x + 1][y + 1] != 1)
                bitmask += 4;
            int index = bitmaskLookup[bitmask];

            terrainTileComponentComponentMapper.create(ent).setRegion(tilesetTex, index % tileset_width, index / tileset_width);
            blockingComponentComponentMapper.create(ent);

            //Randomly generate torches
            /*if (bitmask == 31) {
                if (MathUtils.random() <= 0.2f)
                    DecorFactory.Torch(x, y);
                else if (MathUtils.random() <= 0.1f)
                    DecorFactory.Chains(x, y);
                else if (MathUtils.random() <= 0.05f)
                    DecorFactory.Pipe(x, y);
                else if (MathUtils.random() <= 0.1f)
                    DecorFactory.Crack(x, y);
                else if (MathUtils.random() <= 0.1f)
                    DecorFactory.Blood(x, y);
            }*/
        }
        groundTileComponentComponentMapper.create(ent).groundTexture = groundTex; //TODO load one texture for all ground tiles rather
        transformComponentComponentMapper.create(ent).set(x, y, 1);
        return ent;
    }

    public boolean isBlocking(int x, int y) {
        if (x >= entitie_map.length || y >= entitie_map[x].length)
            return true;
        BlockingComponent blockingComponent = blockingComponentComponentMapper.get(entitie_map[x][y]);
        return blockingComponent != null;
    }

    public Vector2 getFreeSpace() {
        int x = 0, y = 0;

        while (isBlocking(x, y)) {
            x = MathUtils.random(map_width - 1);
            y = MathUtils.random(map_height - 1);
        }

        return new Vector2(x, y);
    }

    public int getMapWidth() {
        if (entitie_map == null)
            return -1;
        return entitie_map.length;
    }

    public int getMapHeight() {
        if (entitie_map == null)
            return -1;
        return entitie_map[0].length;
    }

    protected void genRandomDungeon() {
        map_datasource = new int[map_width][map_height];

        bsp = new BSP(map_width, map_height, 6);

        BSPLeafIterator iterator = bsp.getLeafIterator();
        Random rand = new Random();

        while (iterator.hasNext()) {
            Rectangle room = iterator.next().room;
            for (int i = 0; i < room.width; i++)
                for (int j = 0; j < room.height; j++) {
                    map_datasource[i + (int) (room.x)][j + (int) (room.y)] = 1;
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
                    map_datasource[x][(int) nodeAreamiddle.y] = 1;
                }
            } else {
                for (int y = (int) leftAreaMiddle.y; y < rightAreaMiddle.y; y++) {
                    map_datasource[(int) nodeAreamiddle.x][y] = 1;
                }
            }
        }
    }

    protected void genBitmaskTest() {
        int col = 4;
        map_width = 4 * col;
        map_height = 4 * 64;
        map_datasource = new int[map_width][map_height];

        for (int x = 0; x < map_width; x++)
            for (int y = 0; y < map_height; y++)
                map_datasource[x][y] = 1;

        for (int i = 0; i < 256; i++) {
            int x = i % col * 4 + 1;
            int y = i / col * 4 + 1;
            map_datasource[x][y + 2] = (i & 1) > 0 ? 0 : 1;
            map_datasource[x + 1][y + 2] = (i & 2) > 0 ? 0 : 1;
            map_datasource[x + 2][y + 2] = (i & 4) > 0 ? 0 : 1;
            map_datasource[x][y + 1] = (i & 8) > 0 ? 0 : 1;
            map_datasource[x + 2][y + 1] = (i & 16) > 0 ? 0 : 1;
            map_datasource[x][y] = (i & 32) > 0 ? 0 : 1;
            map_datasource[x + 1][y] = (i & 64) > 0 ? 0 : 1;
            map_datasource[x + 2][y] = (i & 128) > 0 ? 0 : 1;
            map_datasource[x + 1][y + 1] = 0;
        }
    }
}
