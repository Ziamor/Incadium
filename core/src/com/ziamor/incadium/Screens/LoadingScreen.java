package com.ziamor.incadium.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ziamor.incadium.Incadium;


public class LoadingScreen implements Screen {
    private Incadium incadium;

    AssetManager assetManager;
    Stage stage;
    Table table;
    Skin skin;

    ProgressBar loadingbar;

    public LoadingScreen(Incadium incadium) {
        this.incadium = incadium;

        assetManager = incadium.assetManager;
        skin = incadium.skin;

        stage = new Stage();
        table = new Table();

        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);
        stage.addActor(table);

        Label lbLoading = new Label("Loading", skin);
        loadingbar = new ProgressBar(0f, 1f, 0.01f, false, skin);
        table.add(lbLoading);
        table.row();
        table.add(loadingbar);

        assetManager.load("player.png", Texture.class);
        assetManager.load("bat.png", Texture.class);
        assetManager.load("Slime.png", Texture.class);
        assetManager.load("Decor.png", Texture.class);
        assetManager.load("Item.png", Texture.class);
        assetManager.load("WallsBM.png", Texture.class);
        assetManager.load("ground.png", Texture.class);
        assetManager.load("light_stencil.png", Texture.class);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (assetManager.update()) {
            incadium.setScreen(new MainMenuScreen(incadium));
            this.dispose();
        }

        loadingbar.setValue(assetManager.getProgress());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
