package com.swordbit.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.swordbit.game.util.Assets;
import com.swordbit.game.util.ProgressBar;

/**
 * Loading Screen that displays swordbit logo and a loading bar
 **/
public class LoadingScreen implements Screen {
	int width;
	int height;
	private SpriteBatch spriteBatch;
	private Texture splsh;
	private Game myGame;
	private Texture hungerTexture;
	private TextureRegion hungerTextureRegion;
	private ProgressBar progressBar;

	/**
	 * Constructor for the splash screen
	 * 
	 * @param g
	 *            Game which called this splash screen.
	 */
	public LoadingScreen(Game g) {
		Texture.setEnforcePotImages(false);
		myGame = g;
		splsh = new Texture(Gdx.files.internal("images/logo.png"));
		Assets.instance.init(new AssetManager());
		hungerTexture = new Texture("images/hunger.png");
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1, 1, 1, 1);

		spriteBatch.begin();
		spriteBatch.draw(splsh, 0, 0, width, height);
		progressBar.SetEnd(100,
				Assets.instance.getAssetManager().getProgress() * 100);
		progressBar.Draw(spriteBatch);
		spriteBatch.end();
		Assets.instance.getAssetManager().getLoadedAssets();
		if (Assets.instance.getAssetManager().update()) {
			myGame.getScreen().dispose();
			myGame.setScreen(new MainMenuScreen(this.myGame));
		}
	}

	@Override
	public void show() {

		spriteBatch = new SpriteBatch();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		hungerTextureRegion = new TextureRegion(hungerTexture);
		progressBar = new ProgressBar(hungerTextureRegion,
				Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 10);
	}

	@Override
	public void resize(int width, int height) {
		this.height = height;
		this.width = width;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}