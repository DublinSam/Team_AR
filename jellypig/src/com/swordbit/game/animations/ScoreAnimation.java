package com.swordbit.game.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.swordbit.game.util.Assets;

public class ScoreAnimation {
	SpriteBatch spriteBatch;
	float transparency;
	Vector3 position;
	String score;
	Texture texture;

	private boolean isFinished;

	public ScoreAnimation() {
		texture = new Texture(Gdx.files.internal("images/score.png"));
		this.position = new Vector3();
	}

	public void init(SpriteBatch spriteBatch, Vector3 position, String score) {
		isFinished = false;
		this.score = score;
		this.transparency = 1;
		this.spriteBatch = spriteBatch;
		this.position.set(position);
	}

	public boolean draw() {

		float alpha = spriteBatch.getColor().a;
		spriteBatch.setColor(1, 0, 0, transparency);
		// spriteBatch.draw(texture, position.x,position.y,1,1);
		Assets.instance.fonts.defaultNormal.setColor(0, 1, 0, transparency);
		Assets.instance.fonts.defaultNormal.draw(spriteBatch, score,
				position.x, position.y);
		Assets.instance.fonts.defaultNormal.setColor(1, 1, 1, alpha);
		position.y += 1;
		transparency -= 0.02;
		if (transparency <= 0) {
			isFinished = true;
		}

		return isFinished;

	}
}
