package com.swordbit.game.model.eater;

import com.swordbit.game.controller.WorldController.Input;
import com.swordbit.game.model.eater.Eater.ActionState;

public class JumpingState implements EaterActionState {

	@Override
	public void handleInput(Eater eater, Input input) {
		// Once the jump has started, the eater cannot jump again
	}

	@Override
	public void update(Eater eater) {
		// Transition to idle state when the eater lands on the ground
		if (eater.grounded)
			eater.actionState = ActionState.IDLE;
	}
}
