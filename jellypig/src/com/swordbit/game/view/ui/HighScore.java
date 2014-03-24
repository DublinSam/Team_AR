package com.swordbit.game.view.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.swordbit.game.util.Assets;

public class HighScore extends Actor {
	int score = 0;
	float height;
	float width;
	float posX;
	float posY;

	/** eater passed to constructor in order to give reference to score **/
	public HighScore(int score, float height, float width, float posX,
			float posY) {
		this.score = score;
		this.height = height;
		this.width = width;
		this.posX = posX;
		this.posY = posY;
		setSize(width, height);
		setPosition(posX, posY);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {

		batch.end();

		batch.begin();

		Assets.instance.fonts.defaultNormal.draw(batch, "HighScore " + score,
				posX, posY);
		batch.end();
		batch.begin();
	}
}
