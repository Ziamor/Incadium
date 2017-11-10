package com.ziamor.incadium;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ziamor.incadium.Screens.GamePlayScreen;
import com.ziamor.incadium.Screens.LoadingScreen;
import com.ziamor.incadium.Screens.MainMenuScreen;


public class Incadium extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public Skin skin;
    public AssetManager assetManager;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        assetManager = new AssetManager();
        assetManager.load("skin.json", Skin.class);
        assetManager.finishLoading();

        skin = assetManager.get("skin.json", Skin.class);
        this.setScreen(new LoadingScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        assetManager.dispose();
    }
}

