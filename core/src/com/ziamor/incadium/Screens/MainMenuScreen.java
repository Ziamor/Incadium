package com.ziamor.incadium.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ziamor.incadium.Incadium;


public class MainMenuScreen implements Screen {
    final Incadium incadium;

    OrthographicCamera camera;
    SpriteBatch batch;

    Stage stage;
    Table table;
    Skin skin;

    public MainMenuScreen(final Incadium incadium) {
        this.incadium = incadium;

        //TODO set camera to use a viewport
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        skin = incadium.skin;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        final TextButton button = new TextButton("Start", skin, "default");
        button.setWidth(200f);
        button.setHeight(20f);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                incadium.setScreen(new GamePlayScreen(incadium));
                dispose();
            }
        });

        table.add(button);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //TODO remove later
        //incadium.setScreen(new GamePlayScreen(incadium));
        //dispose();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
