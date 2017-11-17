package com.ziamor.incadium;

import com.artemis.E;
import com.badlogic.gdx.graphics.Texture;

public class ItemFactory {
    private static final int tileSize = 32;

    protected ItemFactory() {
    }

    public static int Rune(int x, int y) {
        return E.E().transformComponent(x, y, 2).textureRegionResolverComponent("Item.png", 0, 0, tileSize, tileSize).itemComponent().entity().getId();
    }

    public static int Coin(int x, int y) {
        return E.E().transformComponent(x, y, 2).textureRegionResolverComponent("Item.png", tileSize, 0, tileSize, tileSize).itemComponent().entity().getId();
    }

    public static int Sword(int x, int y) {
        return E.E().transformComponent(x, y, 2).textureRegionResolverComponent("Item.png", tileSize * 2, 0, tileSize, tileSize).itemComponent().entity().getId();
    }

    public static int Potion(int x, int y) {
        return E.E().transformComponent(x, y, 2).textureRegionResolverComponent("Item.png", tileSize * 3, 0, tileSize, tileSize).itemComponent().entity().getId();
    }
}
