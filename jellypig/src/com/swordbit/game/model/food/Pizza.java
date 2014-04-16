package com.swordbit.game.model.food;

import com.swordbit.game.utils.Assets;

public class Pizza extends Food {

	public Pizza() {
		super();
		init();
	}
		
	private void init() {
		this.width = 0.75f;
		this.height = 0.75f;
		this.scoreValue = 100;
		this.consequence = "FAT";
		this.bounds.width = width;
		this.bounds.height = height;
		super.foodTexture = Assets.instance.foods.pizza;
	}
}
