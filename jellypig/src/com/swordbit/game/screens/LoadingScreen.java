package com.swordbit.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swordbit.game.util.Assets;

public class LoadingScreen extends AbstractGameScreen {
	int width;
	int height;
	private float progress;
	private Texture textTexture;
	private Texture ballTexture;
	private SpriteBatch spriteBatch;

	public LoadingScreen(Game game) {
		super(game);
		loadResources();
	}
	
	@Override
	public void show() {
		spriteBatch = new SpriteBatch();
	}
	
	private void loadResources() {
		Texture.setEnforcePotImages(false);
		textTexture = new Texture(Gdx.files.internal("images/loading.png"));
		ballTexture = new Texture(Gdx.files.internal("images/ball.png"));
		Assets.instance.init(new AssetManager());
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	
		progress = Assets.instance.getAssetManager().getProgress();
		spriteBatch.begin();
		spriteBatch.draw(textTexture, 0, 0);
		drawProgressBalls(progress);
		spriteBatch.end();
		switchToMainMenu();
	}
	
	private void drawProgressBalls(float progress) {
		if (progress < 0.2) {
			// Don't draw balls
		} else if (progress < 0.4) {
			drawOneBall();
		} else if (progress < 0.6) {
			drawTwoBalls();
		} else {
			drawThreeBalls();
		}
	}
	
	private void drawOneBall() {
		spriteBatch.draw(ballTexture, 130, 80);
	}
	
	private void drawTwoBalls() {
		spriteBatch.draw(ballTexture, 130, 80);
		spriteBatch.draw(ballTexture, 200, 80);
	}
	
	private void drawThreeBalls() {
		spriteBatch.draw(ballTexture, 130, 80);
		spriteBatch.draw(ballTexture, 200, 80);
		spriteBatch.draw(ballTexture, 270, 80);
	}
	
	private void switchToMainMenu() {
		Assets.instance.getAssetManager().getLoadedAssets();
		if (Assets.instance.getAssetManager().update()) {
			game.getScreen().dispose();
			game.setScreen(new MainMenuScreen(this.game, world));
		}
	}

	@Override
	public void resize(int width, int height) {
		this.height = height;
		this.width = width;
	}

	@Override public void pause() {}
	@Override public void resume() {}
	@Override public void dispose() {}
}