package com.swordbit.game.model.eater;

import com.swordbit.game.controller.WorldController.Input;

public interface ActionState {
	public void handleInput(Eater eater, Input input);
	public void update(Eater eater);
}
