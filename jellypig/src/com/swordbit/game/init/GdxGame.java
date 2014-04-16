package com.swordbit.game.init;

import com.badlogic.gdx.Game;
import com.swordbit.game.view.screens.LoadingScreen;

public class GdxGame extends Game {
	@Override
	public void create() {
		setScreen(new LoadingScreen(this));
	}
}