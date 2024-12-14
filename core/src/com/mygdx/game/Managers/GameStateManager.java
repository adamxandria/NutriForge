package com.mygdx.game.Managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.game;
import com.mygdx.game.GameState.*;


public class GameStateManager {

    private GameState gameState;

    //states of game
    public enum State {
        MENU, PLAY, HIGHSCORES, OPTIONS, GAMEOVER, GAMEWON, PAUSE
    }


    //Play mode options available.
    public enum OptionKeys {
        SNAKE, PLAYER
    }

    public enum PlayMode{
        LEVEL_UP, INFINITE_TAIL
    }

    private static OptionKeys optionKeys;

    private static PlayMode playMode;

    private static boolean newGame;

    public final SpriteBatch batch = new SpriteBatch();

    public final Texture texture = new Texture("menuimageresized.jpeg");
    public final ShapeRenderer renderer = new ShapeRenderer();
    public final BitmapFont titleFont = new BitmapFont();
    public final BitmapFont font = new BitmapFont();


    public GameStateManager() {
        optionKeys = OptionKeys.PLAYER;
        playMode= PlayMode.INFINITE_TAIL;
        setState(State.MENU);
    }

    public void setOptionsKeys(OptionKeys optionKeys) {
        GameStateManager.optionKeys = optionKeys;
    }

    public static OptionKeys getOptionsKeys() {
        return GameStateManager.optionKeys;
    }

    public static PlayMode getPlayMode() {
        return playMode;
    }

    public static void setPlayMode(PlayMode playMode) {
        GameStateManager.playMode = playMode;
    }


     //Sets new game state.
     //MENU, PLAY, HIGHSCORES, OPTIONS, GAMEOVER, PAUSE
    public void setState(State state) {
        if (gameState != null) {
            gameState.dispose();
            gameState = null;
            System.gc();
        }

        switch (state) {
            case MENU:
                game.setCameraPosition();
                gameState = new MenuState(this);
                break;
            case PLAY:
                game.setCameraPosition();
                gameState = new PlayState(this);
                break;
            case OPTIONS:
                game.setCameraPosition();
                gameState = new OptionState(this);
                break;
        }
    }
    public static boolean isNewGame() {
        return newGame;
    }

    public static void startNewGame() {
        newGame=!newGame;
    }

    public void update(float dt) {
        gameState.update(dt);
    }

    public void draw() {
        gameState.draw();
    }
}

