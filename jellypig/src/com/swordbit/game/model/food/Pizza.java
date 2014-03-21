package com.swordbit.game.model.food;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.swordbit.game.util.Assets;

public class Pizza extends Food {

	public float width = 0.75f;
	public float height = 0.75f;
	public int scoreValue = 100;
	public String consequence = "FAT";
	TextureRegion foodTexture = Assets.instance.food.pizza;
}
