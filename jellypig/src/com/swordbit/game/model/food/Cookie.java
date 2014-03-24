package com.swordbit.game.model.food;

import com.swordbit.game.utils.Assets;

public class Cookie extends Food {

	public Cookie() {
		super();
		init();
	}
		
	private void init() {
		this.width = 0.5f;
		this.height = 0.5f;
		this.scoreValue = 100;
		this.consequence = "ACNE";
		this.bounds.width = width;
		this.bounds.height = height;
		super.foodTexture = Assets.instance.foods.cookie;
	}
}
