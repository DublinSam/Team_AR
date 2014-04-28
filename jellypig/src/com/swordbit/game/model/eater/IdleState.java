package com.swordbit.game.model.eater;

import com.swordbit.game.controller.WorldController.Input;
import com.swordbit.game.model.SoundEffects;
import com.swordbit.game.utils.Assets;

public class IdleState implements ActionState {

	@Override
	public void handleInput(Eater eater, Input input) {
		if (input == Input.PRESS_JUMP) {
			SoundEffects.instance.play(Assets.instance.sounds.jump);
			eater.velocity.y = 12;
			eater.grounded = false;
			JumpingState jumpingState = new JumpingState();
			eater.setActionState(jumpingState);
			eater.notifyListeners(this, "actionState", this, jumpingState);
		}
		if (input == Input.PRESS_FART) {
			SoundEffects.instance.play(Assets.instance.sounds.fart);	
			eater.decrementGas();	
			FartingState fartingState = new FartingState();
			eater.setActionState(fartingState);
			eater.notifyListeners(this, "actiovfnState", this, fartingState);
		}
	}

	@Override
	public void update(Eater eater) {
		// IDLE is a stable state
	}
}
