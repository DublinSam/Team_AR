package com.swordbit.game.model.food;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.swordbit.game.util.Assets;

public class Chili extends Food {

	public float width = 0.25f;
	public float height = 0.7f;
	public int scoreValue = 100;
	public String consequence = "HOT";
	TextureRegion foodTexture = Assets.instance.food.chili;
}
