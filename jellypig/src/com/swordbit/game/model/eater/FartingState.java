package com.swordbit.game.model.eater;

import com.swordbit.game.controller.WorldController.Input;
import com.swordbit.game.model.eater.Eater.ActionState;

public class FartingState implements EaterActionState {

	@Override
	public void handleInput(Eater eater, Input input) {
		// No user input affects the FARTING state	
	}

	@Override
	public void update(Eater eater) {
		// transition straight back to IDLE after fart
		eater.actionState = ActionState.IDLE;
		
	}

}
