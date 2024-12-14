package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.Scenes.LoadingScreen;

public class NutriForge extends Game {

    private final AssetManager assetManager;

    public NutriForge() {
        assetManager = new AssetManager();
    }

    @Override
    public void create() {
        setScreen(new LoadingScreen(this));
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
