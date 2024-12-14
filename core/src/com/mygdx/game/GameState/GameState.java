package com.mygdx.game.GameState;


import com.mygdx.game.Managers.GameStateManager;

public abstract class GameState {
    final GameStateManager gameStateManager;

    GameState(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        init();
    }

    protected abstract void init();

    public abstract void update(float dt);

    public abstract void draw();

    public abstract void handleInput();

    public abstract void dispose();

}
