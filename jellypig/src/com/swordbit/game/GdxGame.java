package com.swordbit.game;

import com.badlogic.gdx.Game;
import com.swordbit.game.screens.LogoScreen;

public class GdxGame extends Game {
	@Override
	public void create() {
		setScreen(new LogoScreen(this));
	}
}
