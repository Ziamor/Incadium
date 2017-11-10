package com.ziamor.incadium;

import com.artemis.E;
import com.badlogic.gdx.graphics.Texture;

public class ItemFactory {
    protected static Texture itemTexture = null;
    private static final int tileSize = 32;

    protected ItemFactory() {
    }

    public static void  setTexture(Texture texture){
        itemTexture = texture;
    }

    public static int Rune(int x, int y) {
        return E.E().transformComponent(x, y, 2).textureRegionComponent(itemTexture, 0, 0, tileSize, tileSize).entity().getId();
    }

    public static int Coin(int x, int y) {
        return E.E().transformComponent(x, y, 2).textureRegionComponent(itemTexture, tileSize, 0, tileSize, tileSize).entity().getId();
    }

    public static int Sword(int x, int y) {
        return E.E().transformComponent(x, y, 2).textureRegionComponent(itemTexture, tileSize * 2, 0, tileSize, tileSize).entity().getId();
    }

    public static int Potion(int x, int y) {
        return E.E().transformComponent(x, y, 2).textureRegionComponent(itemTexture, tileSize * 3, 0, tileSize, tileSize).entity().getId();
    }
}
