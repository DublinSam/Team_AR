package com.swordbit.game.model.eater;

import com.swordbit.game.controller.WorldController.Input;
import com.swordbit.game.model.SoundEffects;
import com.swordbit.game.utils.Assets;

public class IdleState implements ActionState {

	@Override
	public void handleInput(Eater eater, Input input) {
		if (input == Input.PRESS_JUMP) {
			jump(eater);
		}
		if (input == Input.PRESS_FART) {
			fart(eater);	
		}
	}

	@Override
	public void update(Eater eater) {
		// IDLE is a stable state
	}
	
	private void jump(Eater eater) {
		eater.velocity.y = 12;
		eater.grounded = false;
		SoundEffects.instance.play(Assets.instance.sounds.jump);
		JumpingState jumpingState = new JumpingState();
		eater.setActionState(jumpingState);
		eater.notifyListeners(this, "actionState", this, jumpingState);
	}
	
	private void fart(Eater eater) {
		eater.decrementGas();	
		SoundEffects.instance.play(Assets.instance.sounds.fart);
		FartingState fartingState = new FartingState();
		eater.setActionState(fartingState);
		eater.notifyListeners(this, "actiovfnState", this, fartingState);
	}
}
