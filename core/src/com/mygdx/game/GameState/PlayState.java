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


public class PlayState extends GameState {

    private SpriteBatch sb;
    private ShapeRenderer sr;
    private BitmapFont titleFont;
    private BitmapFont subFont;
    private BitmapFont font;

    private float tempGameWidth;
    private float tempGameHeight;

    private float moveTimer;
    private float moveTime; //move every x second

    private boolean playTime;
    private boolean exitMessage;

    private boolean beat1;

    public PlayState(GameStateManager gameStateManager) {
        super(gameStateManager);
    }

    @Override
    public void init() {
        sr = gameStateManager.renderer;
        sb = gameStateManager.batch;
        titleFont = gameStateManager.titleFont;

        //removed from draw, causes a huge consumption of memory in there
        Color color = new Color(0, 1, 1, 1);
        titleFont = Font.MANAGER.set(50, color);
        font = Font.MANAGER.set(35);
        subFont = Font.MANAGER.set(40);
        subFont.setColor(Color.RED);

        setupLevel();

        //Timers
        moveTimer = 0;
        moveTime = 0.25f;

        updateExtraLives();
        resetBody();
    }

    /**
     * Resets the snake position considering level start position and orientation.
     */
    private void resetBody() {

    }


    private boolean isPlayTime() {
        return playTime;
    }

    @Override
    public void update(float dt) {
        //get user input
        handleInput();

        moveTimer += dt;

        //if is paused doesn't update the rest
        if (!isPlayTime()) {
            return;
        }

        //only moves every time defined by moveTime
        if (moveTimer > moveTime) {
            moveTimer = 0; //reset timer

            updateBodyPosition();


            if (beat1)
                Music.MANAGER.play("slide1", 0.1f);
            else
                Music.MANAGER.play("slide2", 0.10f);
            beat1 = !beat1;

            updatesLives();
        }
    }

    /**
     * Update player's lives.
     */
    private void updatesLives() {
    }

    /**
     * Updates tail parts position.
     */
    private void updateBodyPosition() {

    }

    /**
     * Format game board. Sets main class game WIDTH and HEIGHT,
     * according columns and rows times gridCell dimension.
     * <p>
     * Grants that LevelManager is reset without a stored level in memory.
     */
    private void setupLevel() {
        tempGameWidth = game.WIDTH;
        tempGameHeight = game.HEIGHT;
    }


    /**
     * Updates the extra lives to the display
     */
    private void updateExtraLives() {
    }

    @Override
    public void draw() {
        sr.setProjectionMatrix(game.camera.combined);
        sb.setProjectionMatrix(game.camera.combined);

        drawGrid();
        drawText();
    }

    /**
     * Draws text information
     */
    private void drawText() {
        Font.MANAGER.centered(sb, titleFont,
                MenuState.title,
                game.WIDTH / 2,
                game.HEIGHT + 80);


        if (!playTime && !exitMessage) {
            Font.MANAGER.centered(sb, subFont,
                    "Hit space to continue ...",
                    game.WIDTH / 2,
                    game.HEIGHT - 200);

            Font.MANAGER.centered(sb, font,
                    "Press esc to quit game",
                    game.WIDTH / 2,
                    game.HEIGHT - 250);
        }

        if (exitMessage) {
            Font.MANAGER.centered(sb, font,
                    "Are you sure you want",
                    game.WIDTH / 2,
                    game.HEIGHT / 2 + 20);

            Font.MANAGER.centered(sb, font,
                    "to quit the game?",
                    game.WIDTH / 2,
                    game.HEIGHT / 2);

            Font.MANAGER.centered(sb, font,
                    "(Y to exit, N to cancel)",
                    game.WIDTH / 2,
                    game.HEIGHT / 2 - 20);
        }
    }


    /**
     * Draws a grid of the board.
     */
    private void drawGrid() {
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && exitMessage!=true) {
            Music.MANAGER.play("accept");
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            exitMessage = true;

            Music.MANAGER.play("accept");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Y) && exitMessage==true) {
            exitMessage = !exitMessage;
            //revert startNewGame
            GameStateManager.isNewGame();

            Music.MANAGER.play("accept");
            gameStateManager.setState(GameStateManager.State.MENU);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.N) && exitMessage==true) {
            exitMessage = !exitMessage;
        }
    }

    @Override
    public void dispose() {
        game.WIDTH = tempGameWidth;
        game.HEIGHT = tempGameHeight;
        game.setCameraPosition();
    }
}
