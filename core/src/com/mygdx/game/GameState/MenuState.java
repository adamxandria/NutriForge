package com.mygdx.game.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.game;
import com.mygdx.game.Managers.Font;
import com.mygdx.game.Managers.GameStateManager;
import com.mygdx.game.Audio.Music;

public class MenuState extends GameState {

    private SpriteBatch batch;

    public Texture getBGDtexture() {
        return BGDtexture;
    }

    private Texture BGDtexture;
    private ShapeRenderer renderer;
    private BitmapFont titleFont;
    private BitmapFont font;

    static final String title = "NUTRIFORGE";

    private int currentItem;
    private String[] menuItems;


    public MenuState(GameStateManager gameStateManager) {
        super(gameStateManager);
        BGDtexture = new Texture("menuimageresized.jpeg");
    }

    @Override
    public void init() {
        batch = gameStateManager.batch;
        renderer = gameStateManager.renderer;

        // set font
        titleFont = Font.MANAGER.set(50);
        titleFont.setColor(Color.WHITE);

        font = Font.MANAGER.set(35);

        menuItems = new String[]{"Play"};
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void draw() {
        batch.setProjectionMatrix(game.camera.combined);
        renderer.setProjectionMatrix(game.camera.combined);

        Font.MANAGER.centered(batch, titleFont, title, game.WIDTH / 2, game.HEIGHT - 175);

        float row = game.HEIGHT - 225;

        for (int i = 0; i < menuItems.length; i++) {
            row -= 30;

            font.setColor(currentItem == i ? Color.RED : Color.WHITE);

            Font.MANAGER.centered(batch, font, menuItems[i], game.WIDTH / 2, row);
        }


    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && currentItem > 0) {
            currentItem--;
            Music.MANAGER.play("select");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && currentItem < menuItems.length - 1) {
            currentItem++;
            Music.MANAGER.play("select");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            select();
            Music.MANAGER.play("accept");
        }
    }

    @Override
    public void dispose() {
        //dispose of objects is manipulated by the game class
    }

    private void select() {
        // play
        switch (currentItem) {
            case 0:
                GameStateManager.startNewGame();
                gameStateManager.setState(GameStateManager.State.OPTIONS);
                break;
        }
    }
}
