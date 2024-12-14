package com.mygdx.game.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.game;
import com.mygdx.game.Managers.Font;
import com.mygdx.game.Managers.GameStateManager;
import com.mygdx.game.Audio.Music;

public class OptionState extends GameState {

    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private BitmapFont titleFont;
    private BitmapFont subFont;
    private BitmapFont font;
    private BitmapFont info;

    private int currentItem;
    private String[] menuItems;

    public OptionState(GameStateManager gameStateManager) {
        super(gameStateManager);
    }


    @Override
    public void init() {
        batch = gameStateManager.batch;
        renderer = gameStateManager.renderer;

        // set font
        titleFont = Font.MANAGER.set(50);
        titleFont.setColor(Color.WHITE);

        subFont = Font.MANAGER.set(40);
        subFont.setColor(Color.RED);

        font = Font.MANAGER.set(35);
        info = Font.MANAGER.set(30);

        String[] playMenuItems = new String[]{"Controls","Up arrow - Move Up", "Down arrow - Move Down", "Left arrow - Move Left",
                "Right arrow - Move Right", "Spacebar - Pause"};

        if (GameStateManager.isNewGame())
            menuItems = playMenuItems;
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

        float row = game.HEIGHT - 100;
        String str = "";

        if (!GameStateManager.isNewGame()) {
            Font.MANAGER.centered(batch, info, str, game.WIDTH / 2, row);
            row -= 50;
        }

        for (int i = 0; i < menuItems.length; i++) {
            row -= 30;

            font.setColor(Color.WHITE);
            Font.MANAGER.centered(batch, font, menuItems[i], game.WIDTH / 2, row);
        }

        row -= 50;

        if (!GameStateManager.isNewGame()) {
            Font.MANAGER.centered(batch, info, str, game.WIDTH / 2, row);
        }

        Font.MANAGER.centered(batch, subFont,
            "Hit enter to continue ...",
            game.WIDTH / 2,
            125);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            select();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            //revert newGame
            GameStateManager.startNewGame();

            gameStateManager.setState(GameStateManager.State.MENU);
        }
    }

    @Override
    public void dispose() {
        //dispose of objects is manipulated by the Game class
    }

    /**
     * Selection options of the game menu.
     */
    private void select() {
        // play
        if (GameStateManager.isNewGame()) {
            switch (currentItem) {
                case 0:
                    GameStateManager.setPlayMode(GameStateManager.PlayMode.LEVEL_UP);
                    break;
                case 1:
                    GameStateManager.setPlayMode(GameStateManager.PlayMode.INFINITE_TAIL);
                    break;
            }
            Music.MANAGER.play("accept");
            GameStateManager.startNewGame();
            gameStateManager.setState(GameStateManager.State.PLAY);
        } else {
            switch (currentItem) {
                case 0:
                    gameStateManager.setOptionsKeys(GameStateManager.OptionKeys.PLAYER);
                    break;
                case 1:
                    gameStateManager.setOptionsKeys(GameStateManager.OptionKeys.SNAKE);
                    break;
            }
            gameStateManager.setState(GameStateManager.State.MENU);
        }
    }
}
