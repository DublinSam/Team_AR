package com.swordbit.game.model.eater;

import com.swordbit.game.model.food.Food;

public interface EaterHealthState {
	public void consumeFood(Eater eater, Food food);
}
