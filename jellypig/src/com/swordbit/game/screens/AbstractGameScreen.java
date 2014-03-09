package com.swordbit.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.swordbit.game.model.World;
import com.swordbit.game.util.Assets;

public abstract class AbstractGameScreen implements Screen {
	protected Game game;
	protected World world;

	// Constructor for the loading screen
	public AbstractGameScreen(Game game) {
		this.game = game;
	}
	
	// Constructor for all other screens
	public AbstractGameScreen(Game game, World world) {
		this.game = game;
		this.world = world;
	}

	public abstract void render(float deltaTime);
	public abstract void resize(int width, int height);
	public abstract void show();
	public void hide() {};
	public abstract void pause();
	public void resume() {}
	public void dispose() {}

}
