package com.ziamor.incadium;

import com.artemis.E;
import com.badlogic.gdx.graphics.Texture;

public class DecorFactory {
    protected static Texture decorTexture = null;
    private static final int tileSize = 32;

    protected DecorFactory() {
    }

    public static void  setTexture(Texture texture){
        decorTexture = texture;
    }

    public static int Torch(int x, int y) {
        return E.E().transformComponent(x, y, 1).textureRegionComponent(decorTexture, 0, 0, tileSize, tileSize).entity().getId();
    }

    public static int Chains(int x, int y) {
        return E.E().transformComponent(x, y, 0).textureRegionComponent(decorTexture, tileSize, 0, tileSize, tileSize).entity().getId();
    }

    public static int Pipe(int x, int y) {
        return E.E().transformComponent(x, y, 0).textureRegionComponent(decorTexture, tileSize * 2, 0, tileSize, tileSize).entity().getId();
    }

    public static int Crack(int x, int y) {
        return E.E().transformComponent(x, y, 0).textureRegionComponent(decorTexture, tileSize * 3, 0, tileSize, tileSize).entity().getId();
    }

    public static int Blood(int x, int y) {
        return E.E().transformComponent(x, y, 0).textureRegionComponent(decorTexture, tileSize * 4, 0, tileSize, tileSize).entity().getId();
    }
}
