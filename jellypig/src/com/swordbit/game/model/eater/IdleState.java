package com.swordbit.game.model.eater;

import com.swordbit.game.controller.WorldController.Input;
import com.swordbit.game.model.SoundEffects;
import com.swordbit.game.model.eater.Eater.ActionState;
import com.swordbit.game.utils.Assets;

public class IdleState implements EaterActionState {

	@Override
	public void handleInput(Eater eater, Input input) {
		if (input == Input.PRESS_JUMP) {
			SoundEffects.instance.play(Assets.instance.sounds.jump);
			eater.velocity.y = 12;
			eater.actionState = ActionState.JUMPING;
			eater.grounded = false;
			eater.notifyListeners(this, "actionState", ActionState.IDLE, ActionState.JUMPING);
		}
		if (input == Input.PRESS_FART) {
			SoundEffects.instance.play(Assets.instance.sounds.fart);	
			eater.decrementGas();	
			eater.actionState = ActionState.FARTING;
			eater.notifyListeners(this, "actiovfnState", ActionState.IDLE, ActionState.FARTING);
		}
	}

	@Override
	public void update(Eater eater) {
		// IDLE is a stable state
	}
}
