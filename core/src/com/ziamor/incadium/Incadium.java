package com.ziamor.incadium;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ziamor.incadium.Screens.GamePlayScreen;
import com.ziamor.incadium.Screens.MainMenuScreen;


public class Incadium extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public Skin skin;
    public AssetManager assetManager;

    private boolean doneLoading, started;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        assetManager = new AssetManager();
        assetManager.load("skin.json", Skin.class);
    }

    public void render() {
        super.render();
        doneLoading = assetManager.update();

        if (doneLoading && !started) {
            started = true;
            skin = assetManager.get("skin.json", Skin.class);
            this.setScreen(new MainMenuScreen(this));
        }
    }

    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        assetManager.dispose();
    }
}

