package com.ziamor.incadium;

import com.artemis.E;
import com.badlogic.gdx.graphics.Texture;

public class DecorFactory {
    protected static Texture decorTexture = null;
    private static final int tileSize = 32;

    protected DecorFactory() {
    }

    protected static Texture getDecorTexture() {
        if (decorTexture == null)
            decorTexture = new Texture("Decor.png");
        return decorTexture;
    }

    public static int Torch(int x, int y) {
        return E.E().transformComponent(x, y, 1).textureRegionComponent(getDecorTexture(), 0, 0, tileSize, tileSize).entity().getId();
    }

    public static int Chains(int x, int y) {
        return E.E().transformComponent(x, y, 0).textureRegionComponent(getDecorTexture(), tileSize, 0, tileSize, tileSize).entity().getId();
    }

    public static int Pipe(int x, int y) {
        return E.E().transformComponent(x, y, 0).textureRegionComponent(getDecorTexture(), tileSize * 2, 0, tileSize, tileSize).entity().getId();
    }

    public static int Crack(int x, int y) {
        return E.E().transformComponent(x, y, 0).textureRegionComponent(getDecorTexture(), tileSize * 3, 0, tileSize, tileSize).entity().getId();
    }

    public static int Blood(int x, int y) {
        return E.E().transformComponent(x, y, 0).textureRegionComponent(getDecorTexture(), tileSize * 4, 0, tileSize, tileSize).entity().getId();
    }
}
