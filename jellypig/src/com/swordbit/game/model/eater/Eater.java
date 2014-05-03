package com.swordbit.game.model.eater;

import com.swordbit.game.utils.Constants;
import java.util.List;
import java.util.ArrayList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.swordbit.game.controller.WorldController.Input;
import com.swordbit.game.model.food.Food;

public class Eater {	
	public boolean grounded;
	public float healthTimer;
	public static int SPEED = Constants.EATER_SPEED;
	public static final float SIZE = 1f;
	public static final float DAMPING = 0.8f;
	public enum HealthState { NEUTRAL, HAPPY, FAT, SOUR, ACNE, INVINCIBLE }
	
	int score = 0;
	int foodMissed;
	int foodCollected;
	float fullness = 0;
	Vector2 velocity = new Vector2();
	Vector2 position = new Vector2();
	Rectangle bounds = new Rectangle();
	Vector2 acceleration = new Vector2(); 
	ActionState actionState = new IdleState();
	HealthState healthState = HealthState.NEUTRAL;
	
	private int gas;
	private int maximumGas = 10;
	private int minimumGas = 0;
	private String typeFood = "";
	private boolean farting = false;
	private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();
	

	public Eater(Vector2 position) {
		this.position = position;
		this.bounds.width = SIZE;
		this.bounds.height = SIZE;
		this.acceleration.y = -30;
		this.gas = 0;
		this.bounds.setPosition(position);
	}
	
	public void update(float delta) {
		applyGravity(delta);
		updatePosition(delta);
		updateHealthState(delta);
		updateActionState(delta);
	}
	
	private void applyGravity(float delta) {
		if (!grounded) {
			velocity.add(new Vector2(0, acceleration.y * delta));	
		}
	}

	private void updatePosition(float delta) {
		position.add(velocity.cpy().scl(delta));
		this.bounds.setPosition(position);
	}
	
	private void updateHealthState(float delta) {
		healthTimer += delta;
		if (healthState == HealthState.INVINCIBLE) {
			if (healthTimer > Constants.INVICIBILITY_TIMER) {
				healthState = HealthState.NEUTRAL;
			}
		}
		else if (healthTimer > Constants.FOOD_EFFECTS_TIMER) {
			healthState = HealthState.NEUTRAL;
		}		
	}
	
	// Action State is managed by the ActionState interface
	public void handleInput(Input input) {
		actionState.handleInput(this, input);
	}
	
	public void updateActionState(float delta) {
		actionState.update(this);
	}
	
	public void consumeFood(Food food) {
		updateScore(food);
		incrementGas();
		switch(healthState) {
			case NEUTRAL:
				if (food.consequence == "INVINCIBLE")
					healthState = HealthState.INVINCIBLE;
				if (food.consequence == "FAT")
					healthState = HealthState.FAT;
				if (food.consequence == "SOUR")
					healthState = HealthState.SOUR;
				if (food.consequence == "ACNE")
					healthState = HealthState.ACNE;
				// reset timer
				healthTimer = 0;
				break;		
			case INVINCIBLE:
				// Unaffected by additional Food
				break;
			case HAPPY:
				// Unaffected by additional Food
				break;				
			case FAT:
				// Unaffected by additional Food
				break;			
			case SOUR:
				// Unaffected by additional Food
				break;			
			case ACNE:
				// Unaffected by additional Food
				break;
		}
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
	
	public boolean getGrounded() {
		return grounded;
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
	
	public void setActionState(ActionState actionState) {
		this.actionState = actionState;
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

	public HealthState getHealthState() {
		return healthState;
	}
}
