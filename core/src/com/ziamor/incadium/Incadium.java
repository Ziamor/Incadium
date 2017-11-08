package com.ziamor.incadium;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ziamor.incadium.Screens.GamePlayScreen;
import com.ziamor.incadium.Screens.MainMenuScreen;


public class Incadium extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public Skin skin;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        skin = new Skin(Gdx.files.internal("skin.json"));

        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render(); //important!
    }

    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        skin.dispose();
    }
}

