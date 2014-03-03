package com.swordbit.game;

import com.badlogic.gdx.Game;
import com.swordbit.game.screens.LoadingScreen;

public class GdxGame extends Game {
	@Override
	public void create() {
		setScreen(new LoadingScreen(this));
	}
}
