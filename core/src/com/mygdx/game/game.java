package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Managers.GameStateManager;
import com.mygdx.game.Audio.Music;

public class game extends ApplicationAdapter {

	public static float WIDTH;
	public static float HEIGHT;

	public static OrthographicCamera camera;
	private static Vector3 cameraPosition; //Vector to save the original position of the camera

	private static GameStateManager gameStateManager;
	private Texture backgroundTexture;
	private SpriteBatch backgroundSprite;


	@Override
	public void create() {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(WIDTH, HEIGHT);
		camera.translate(WIDTH / 2, HEIGHT / 2);
		cameraPosition = camera.position.cpy();
		camera.update();

		loadSounds();
		gameStateManager = new GameStateManager();
		backgroundSprite = new SpriteBatch();
		backgroundTexture = new Texture("menuimageresized.jpeg");
	}

	/**
	 * Sets the original position of the camera.
	 */
	public static void setCameraPosition() {
		camera.position.set(cameraPosition);
		camera.update();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.1f, 0.5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		backgroundSprite.begin();
		backgroundSprite.draw(backgroundTexture,0,0);
		backgroundSprite.end();
		gameStateManager.update(Gdx.graphics.getDeltaTime());
		gameStateManager.draw();
	}

	@Override
	public void dispose() {
		gameStateManager.batch.dispose();
		gameStateManager.renderer.dispose();
		gameStateManager.titleFont.dispose();
		gameStateManager.font.dispose();
	}

	private void loadSounds(){
		Music.MANAGER.load("sounds/itemback.wav", "select");
		Music.MANAGER.load("sounds/itempick.wav", "accept");
		Music.MANAGER.load("sounds/level-up.wav", "levelup");
		Music.MANAGER.load("sounds/tick.wav", "bonus");
		Music.MANAGER.load("sounds/Slide01.wav","slide1");
		Music.MANAGER.load("sounds/Slide02.wav", "slide2");
		Music.MANAGER.load("sounds/hiss2.wav", "hiss");
	}
}
