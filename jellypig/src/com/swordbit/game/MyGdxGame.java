package com.swordbit.game;

import com.badlogic.gdx.Game;

/*this is where the game is launched, beginning with the MenuScreen*/
public class MyGdxGame extends Game {
	@Override
	public void create() {
		setScreen(new LoadingScreen(this));
	}
}
