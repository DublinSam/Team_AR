package com.swordbit.game.model.eater;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.badlogic.gdx.math.Vector2;
import com.swordbit.game.controller.WorldController.Input;

public class ActionStateTest {

	@RunWith(Parameterized.class)
	public class InterfaceTesting {
	    public ActionState actionState;
	    public Eater sampleEater;

	    public InterfaceTesting(ActionState actionState) {
	        this.actionState = actionState;
	        this.sampleEater = new Eater(new Vector2(0,0));
	    }

	    @Test
	    public final void testJumpingStateIgnoresNewInputBeforeLanding() {
	    	JumpingState
	    	sampleEater.setActionState(actionState)
	        assertTrue(actionState.handleInput(sampleEater, Input.PRESS_JUMP));
	    }

	    @Test
	    public final void testMyMethod_False() {
	        assertFalse(myInterface.myMethod(false));
	    }

	    @Parameterized.Parameters
	    public Collection<Object[]> statesToTest() {
	        return Arrays.asList(
	                    new Object[]{new IdleState()},
	                    new Object[]{new JumpingState()},
	                    new Object[]{new FartingState()}
	        );
	    }
	}

}
