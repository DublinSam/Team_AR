package com.swordbit.game.model.eater;

import com.swordbit.game.utils.Constants;
import java.util.List;
import java.util.ArrayList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.swordbit.game.controller.WorldController.Input;
import com.swordbit.game.model.SoundEffects;
import com.swordbit.game.model.food.Food;
import com.swordbit.game.utils.Assets;

public class Eater {	
	int score = 0;
	int foodMissed;
	int foodCollected;
	float fullness = 0;
	Vector2 velocity = new Vector2();
	Vector2 position = new Vector2();
	Rectangle bounds = new Rectangle();
	Vector2 acceleration = new Vector2(); 
	ActionState actionState = ActionState.IDLE;
	HealthState healthState = HealthState.NEUTRAL;
	
	public boolean grounded;
	public float healthTimer;
	public static float SPEED = 6f;//8f
	public static final float SIZE = 1f;
	public static final float DAMPING = 0.8f;
	private int gas;
	private int maximumGas = 10;
	private int minimumGas = 0;
	private String typeFood = "";
	private boolean farting = false;
	
	private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();
	
	public enum ActionState {
		IDLE, JUMPING, FARTING 
	}
	
	public enum HealthState {
		NEUTRAL, HAPPY, FAT, SOUR, ACNE
	}

	public Eater(Vector2 position) {
		this.position = position;
		this.bounds.width = SIZE;
		this.bounds.height = SIZE;
		this.acceleration.y = -30;
		this.gas = 0;
		this.bounds.setPosition(position);
	}
	
	/* BEGIN STATE MACHINE SECTION */
	public void update(float delta) {
		updateEaterPosition(delta);
		updateActionState(delta);
		updateHealthState(delta);
	}
	
	private void updateActionState(float delta) {
		switch(actionState) {
			case IDLE:
				// Do nothing
				break;
			case JUMPING:
				if (grounded)
					actionState = ActionState.IDLE;
				break;
			case FARTING:
				actionState = ActionState.IDLE;
		}
	}
	
	private void updateHealthState(float delta) {
		healthTimer += delta;
		if (healthTimer > Constants.FOOD_EFFECTS_TIMER)
			healthState = HealthState.NEUTRAL;
	}
	
	private void updateEaterPosition(float delta) {
		position.add(velocity.cpy().scl(delta));
		this.bounds.setPosition(position);
	}
	
	public void handleInput(Input input) {
		switch(actionState) {
		
			case IDLE:
				if (input == Input.PRESS_JUMP) {
					SoundEffects.instance.play(Assets.instance.sounds.jump);
					velocity.y = 12;
					actionState = ActionState.JUMPING;
					grounded = false;
					notifyListeners(this, "actionState", ActionState.IDLE, ActionState.JUMPING);
				}
				if (input == Input.PRESS_FART) {
					SoundEffects.instance.play(Assets.instance.sounds.fart);	
					decrementGas();	
					actionState = ActionState.FARTING;
					notifyListeners(this, "actiovfnState", ActionState.IDLE, ActionState.FARTING);
				}
				break;
				
			case JUMPING:	
				// No inputs should work while jumping
				break;
				
			case FARTING:
				// No inputs should work while farting
				break;
		}
	}
	
	public void consumeFood(Food food) {
		updateScore(food);
		incrementGas();
		// NEUTRAL is the only state that can change to non-NEUTRAL
		if (healthState == HealthState.NEUTRAL) {
				if (food.consequence == "FAT")
					healthState = HealthState.FAT;
				if (food.consequence == "");
		}
		System.out.println("Current health state:"+ healthState);
	}
	
	public void updateScore(Food food) {
		score += food.scoreValue;
		notifyScoreListeners(this, "Score", score + food.scoreValue, score);
	}
	
	public void incrementGas(){
		if (typeFood == "JUNK") {
			gas = gas + 2;
		} else{
			gas = gas + 1;
		}
		if (gas >= maximumGas) {
			gas = maximumGas;
		}
		System.out.println("Increment Gas Eater: "+ gas);
	}
	
	public void decrementGas(){
		gas = gas - 2;
		if(gas < minimumGas){
			gas = minimumGas;
		}
		System.out.println("Decrement Gas Eater: "+ gas);
	}

	void notifyListeners(Object object, String property, ActionState oldValue, ActionState newValue) {
		for (PropertyChangeListener name : listener) {
			name.propertyChange(new PropertyChangeEvent(this, "state",
					oldValue, newValue));
		}
	}
	
	void notifyScoreListeners(Object object, String property, int oldScore, int newScore) {
		for (PropertyChangeListener name : listener) {
			name.propertyChange(new PropertyChangeEvent(this, "score",
					oldScore, newScore));
		}
	}

	public void addChangeListener(PropertyChangeListener newListener) {
		listener.add(newListener);
	}
	
	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}
	
	public int getScore() {
		return score;
	}
	
	public float getHealthTimer() {
		return healthTimer;
	}

	public Vector2 getPosition() {
		return this.position;
	}

	public Rectangle getBounds() {
		return this.bounds;
	}

	public Vector2 getVelocity() {
		return this.velocity;
	}
	
	public Vector2 getAcceleration() {
		return this.acceleration;
	}
	
	public boolean isColliding(Food food) {
		return this.bounds.overlaps(food.getBounds());
	}

	public ActionState getActionState() {
		return this.actionState;
	}
	
	public int getGas() {
		return gas;
	}

	public void setGas(int gas) {
		this.gas = gas;
	}
	
	public void setTypeOfFood(Food food){
		this.typeFood = food.getType();
	}
	
	public boolean isFarting() {
		return farting;
	}

	public void setFarting(boolean farting) {
		this.farting = farting;
	}
}
