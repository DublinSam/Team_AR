package com.swordbit.game.model.eater;

import com.swordbit.game.model.food.Food;

public class HappyState implements EaterHealthState {

	@Override
	public void consumeFood(Eater eater, Food food) {
		eater.healthTimer = 0;
	}
}
