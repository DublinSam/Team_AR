package com.swordbit.game.model.food;

import com.swordbit.game.util.Assets;

public class Apple extends Food {

	public Apple() {
		super();
		init();
	}
		
	private void init() {
		this.width = 0.5f;
		this.height = 0.5f;
		this.scoreValue = 100;
		this.consequence = "HAPPY";
		this.bounds.width = width;
		this.bounds.height = height;
		super.foodTexture = Assets.instance.foods.apple;
	}
}
