package com.ziamor.incadium;

import com.artemis.E;
import com.badlogic.gdx.graphics.Texture;

public class DecorFactory {
    private static final int tileSize = 32;

    protected DecorFactory() {
    }

    public static int Torch(int x, int y) {
        return E.E().transformComponent(x, y, 1).textureRegionResolverComponent("Decor.png", 0, 0, tileSize, tileSize).entity().getId();
    }

    public static int Chains(int x, int y) {
        return E.E().transformComponent(x, y, 0).textureRegionResolverComponent("Decor.png", tileSize, 0, tileSize, tileSize).entity().getId();
    }

    public static int Pipe(int x, int y) {
        return E.E().transformComponent(x, y, 0).textureRegionResolverComponent("Decor.png", tileSize * 2, 0, tileSize, tileSize).entity().getId();
    }

    public static int Crack(int x, int y) {
        return E.E().transformComponent(x, y, 0).textureRegionResolverComponent("Decor.png", tileSize * 3, 0, tileSize, tileSize).entity().getId();
    }

    public static int Blood(int x, int y) {
        return E.E().transformComponent(x, y, 0).textureRegionResolverComponent("Decor.png", tileSize * 4, 0, tileSize, tileSize).entity().getId();
    }
}
