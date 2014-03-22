package com.swordbit.game.model.food;

import com.swordbit.game.util.Assets;

public class Hotdog extends Food {

	public Hotdog() {
		super();
		init();
	}
		
	private void init() {
		this.width = 0.9f;
		this.height = 0.5f;
		this.scoreValue = 100;
		this.consequence = "FAT";
		this.bounds.width = width;
		this.bounds.height = height;
		super.foodTexture = Assets.instance.foods.hotdog;
	}
}

