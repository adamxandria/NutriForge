package com.mygdx.game.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Managers.Font;
import com.mygdx.game.Managers.GameStateManager;
import com.mygdx.game.Audio.Music;
import com.mygdx.game.game;

public class PauseState extends GameState {

    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private BitmapFont titleFont;
    private BitmapFont font;
    private BitmapFont info;

    private int currentItem;
    private String[] pauseItems;

    public PauseState(GameStateManager gameStateManager) {
        super(gameStateManager);
    }


    @Override
    public void init() {
        batch = gameStateManager.batch;
        renderer = gameStateManager.renderer;

        // set font
        titleFont = Font.MANAGER.set(36);
        titleFont.setColor(Color.WHITE);

        font = Font.MANAGER.set(20);
        info = Font.MANAGER.set(15);

        String[] gamePauseItems = new String[]{"Restart", "Main Menu"};

        pauseItems = gamePauseItems;
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void draw() {
        batch.setProjectionMatrix(game.camera.combined);
        renderer.setProjectionMatrix(game.camera.combined);

        Font.MANAGER.centered(batch, titleFont, MenuState.title, game.WIDTH / 2, game.HEIGHT - 50);

        float row = game.HEIGHT - 150;
        String str = "PAUSE MENU";

        if (!GameStateManager.isNewGame()) {
            Font.MANAGER.centered(batch, info, str, game.WIDTH / 2, row);
            row -= 50;
        }

        for (int i = 0; i < pauseItems.length; i++) {
            row -= 30;

            if (currentItem == i)
                font.setColor(Color.RED);
            else
                font.setColor(Color.WHITE);
            Font.MANAGER.centered(batch, font, pauseItems[i], game.WIDTH / 2, row);
        }

        str = currentItem == 0 ? "Restart the game" : "Go back to main menu";

        row -= 50;

        if (!GameStateManager.isNewGame()) {
            Font.MANAGER.centered(batch, info, str, game.WIDTH / 2, row);
        }
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && currentItem > 0) {
            currentItem--;
            System.out.println(currentItem);
            Music.MANAGER.play("select");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && currentItem < pauseItems.length - 1) {
            currentItem++;
            System.out.println(currentItem);
            Music.MANAGER.play("select");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            select();
            Music.MANAGER.play("accept");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            //revert newGame
            GameStateManager.startNewGame();

            Music.MANAGER.play("accept");
            gameStateManager.setState(GameStateManager.State.MENU);
        }
    }

    @Override
    public void dispose() {
    }

    /**
     * Selection options of the game menu.
     */
    private void select() {
        // if item selected is restart
        if (currentItem == 0){
            Music.MANAGER.play("accept");
            gameStateManager.setState(GameStateManager.State.PLAY);
        } else if (currentItem == 1) {
            Music.MANAGER.play("accept");
            gameStateManager.setState(GameStateManager.State.MENU);
        }
    }
}
