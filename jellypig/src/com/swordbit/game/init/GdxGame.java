package com.swordbit.game.init;

import com.badlogic.gdx.Game;
import com.swordbit.game.view.screens.LoadingScreen;

public class GdxGame extends Game {
	@Override
	public void create() {
		LoadingScreen loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
	}
}
