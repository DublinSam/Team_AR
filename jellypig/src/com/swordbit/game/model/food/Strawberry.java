package com.swordbit.game.model.food;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.swordbit.game.util.Assets;

public class Strawberry extends Food {
	
	public float width = 0.5f;
	public float height = 0.5f;
	public int scoreValue = 100;
	public String consequence = "HAPPY";
	TextureRegion foodTexture = Assets.instance.food.strawberry;
}
