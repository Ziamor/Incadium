package com.ziamor.incadium;

import com.artemis.E;
import com.badlogic.gdx.graphics.Texture;

public class ItemFactory {
    protected static Texture decorTexture = null;
    private static final int tileSize = 32;

    protected ItemFactory() {
    }

    protected static Texture getItemTexture() {
        if (decorTexture == null)
            decorTexture = new Texture("Item.png");
        return decorTexture;
    }

    public static int Rune(int x, int y) {
        return E.E().transformComponent(x, y, 2).textureRegionComponent(getItemTexture(), 0, 0, tileSize, tileSize).entity().getId();
    }

    public static int Coin(int x, int y) {
        return E.E().transformComponent(x, y, 2).textureRegionComponent(getItemTexture(), tileSize, 0, tileSize, tileSize).entity().getId();
    }

    public static int Sword(int x, int y) {
        return E.E().transformComponent(x, y, 2).textureRegionComponent(getItemTexture(), tileSize * 2, 0, tileSize, tileSize).entity().getId();
    }

    public static int Potion(int x, int y) {
        return E.E().transformComponent(x, y, 2).textureRegionComponent(getItemTexture(), tileSize * 3, 0, tileSize, tileSize).entity().getId();
    }
}
