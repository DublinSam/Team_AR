package com.swordbit.game.model.food;

import com.swordbit.game.utils.Assets;

public class Strawberry extends Food {

	public Strawberry() {
		super();
		init();
	}
		
	private void init() {
		this.width = 0.5f;
		this.height = 0.5f;
		this.scoreValue = 100;
		this.consequence = "INVINCIBLE";
		this.bounds.width = width;
		this.bounds.height = height;
		this.type = "JUNK";
		super.foodTexture = Assets.instance.foods.strawberry;
	}
}
