package com.swordbit.game.model.food;

import com.swordbit.game.util.Assets;

public class Chili extends Food {

	public Chili() {
		super();
		init();
	}
	
	private void init() {
		this.width = 0.25f;
		this.height = 0.7f;
		this.scoreValue = 100;
		this.consequence = "HOT";
		this.bounds.width = width;
		this.bounds.height = height;
		super.foodTexture = Assets.instance.foods.chili;
	}
}