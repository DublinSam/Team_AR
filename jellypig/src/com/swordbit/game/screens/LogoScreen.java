package com.swordbit.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swordbit.game.util.Constants;

public class LogoScreen extends AbstractGameScreen {
	private SpriteBatch spriteBatch;
	private Texture logoTexture;
	private float countDown;
	private float bottomLeftCornerX;
	private float bottomLeftCornerY;
	private float logoWidth;
	private float logoHeight;
	
	public LogoScreen(Game game) {
		super(game);
		calculateLogoCoordinates();
	}

	@Override
	public void show() {
		spriteBatch = new SpriteBatch();
		countDown = Constants.LOGO_TIMER_IN_SECONDS;
		logoTexture = new Texture(Gdx.files.internal("images/logo.png"));
	}

	@Override
	public void render(float deltaTime) {
		clearScreen();
		drawLogoInScreenCenter();
		transitionTimer(deltaTime);
	}
	
	private void clearScreen() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	private void drawLogoInScreenCenter() {
		spriteBatch.begin();
		spriteBatch.draw(logoTexture, 
				bottomLeftCornerX,
				bottomLeftCornerY,
				logoWidth, logoHeight);
		spriteBatch.end();
	}
	
	private void transitionTimer (float deltaTime) { 
		countDown -= deltaTime;
		if (countDown < 0)
			switchToLoadingScreen();
	}
	
	private void switchToLoadingScreen() {	
			game.setScreen(new LoadingScreen(game));
	}
	
	private void calculateLogoCoordinates() {
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		bottomLeftCornerX = 
				screenWidth/2 - screenWidth 
				* Constants.LOGO_SIZE /2;
		bottomLeftCornerY = 
				screenHeight/2 
				- (screenWidth * Constants.LOGO_SIZE /2)
					/ Constants.LOGO_IMAGE_ASPECT_RATIO;
		logoWidth = 
				screenWidth * Constants.LOGO_SIZE;
		logoHeight = 
				screenWidth * Constants.LOGO_SIZE 
				   / Constants.LOGO_IMAGE_ASPECT_RATIO;
	}
	
	@Override public void resize(int width, int height) {}
	@Override public void pause() {}
}
