package com.swordbit.game.model;

import java.util.List;
import java.util.ArrayList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.swordbit.game.model.food.Food;

public class Eater {	
	int score = 0;
	int foodMissed;
	int foodCollected;
	float fullness = 0;
	Vector2 velocity = new Vector2();
	Vector2 position = new Vector2();
	Rectangle bounds = new Rectangle();
	Vector2 acceleration = new Vector2(); /** Acceleration not used **/
	public boolean grounded;
	public float timeInState;
	public static float SPEED = 8f;
	public static final float SIZE = 1f;
	public static final float DAMPING = 0.8f;
	private String state = "IDLE";
	private String finalState;
	private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();
	
	/**
	 * State is only recorded in case we are going to be using some Movement or
	 * Idle animations, currently its not used for anything it is only set
	 **/
	/*public enum State {
		IDLE, MOVING, HUNGRY, HOT, BORED, BLINK, JUMPING, ACNE, SOUR, FAT, HAPPY, EATING, TRANSFORMING
	}
	*/

	public Eater(Vector2 position) {
		this.timeInState = 0;
		this.position = position;
		this.bounds.width = SIZE;
		this.bounds.height = SIZE;
		this.acceleration.y = -30;
		this.bounds.setPosition(position);
	}
	
	public void consumeFood(Food food) {
		updateScore(food);
		setState("EATING");
		setFinalState(food.consequence);
		increaseFullness();
	}
	
	public void updateScore(Food food) {
		score += food.scoreValue;
		notifyScoreListeners(this, "Score", score - 100, score);
	}

	/** updates eaters position based on velocity **/
	public void update(float delta) {
		timeInState += delta;
		// add velocity times times time to do the distance traveled
		position.add(velocity.cpy().scl(delta));
		this.bounds.setPosition(position);
		if (!(state == "IDLE")) {
			if (timeInState > 3) {
				setState("IDLE");
			}
		}
		if ((grounded) 
				&& state.equals("JUMPING")) {
			setState("IDLE");
		}
	}

	private void notifyListeners(Object object, String property,
			String oldValue, String newValue) {
		for (PropertyChangeListener name : listener) {
			name.propertyChange(new PropertyChangeEvent(this, "state",
					oldValue, newValue));
		}
	}
	
	private void notifyScoreListeners(Object object, String property,
			int oldScore, int newScore) {
		for (PropertyChangeListener name : listener) {
			name.propertyChange(new PropertyChangeEvent(this, "score",
					oldScore, newScore));
		}
	}

	public void addChangeListener(PropertyChangeListener newListener) {
		listener.add(newListener);
	}

	public void setGrounded(boolean b) {
		grounded = b;
		if (b && (state == "JUMPING")) {
			 state = "IDLE";
		}
	}

	public void forceState(String newState) {
		if (newState == "JUMPING") {
			bounds.height += 0.25;
		}
		if (state == "JUMPING") {
			bounds.height -= 0.25;
		}
		notifyListeners(this, "State", state, newState);
		this.state = newState;
		timeInState = 0;
	}
	
	public void increaseScore() {
		score += 100;
		notifyScoreListeners(this, "Score", score - 100, score);
	}
	public void decreaseScore() {
		score -= 100;
		notifyScoreListeners(this, "Score", score + 100, score);
	}
	
	public int getScore() {
		return score;
	}

	

	public float getTimeInState() {
		return this.timeInState;
	}
	
	public float getFullness() {
		return this.fullness;
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

	private void increaseFullness() {
		this.fullness += 1;
	}
	
	public void setState(String newState) {
		if (timeInState > 3) {
			if (state == "JUMPING") {
				bounds.height -= 0.25;
			}
			notifyListeners(this, "State", state, newState);
			timeInState = 0;
			this.state = newState;
		} else if (state == "IDLE" || state == "JUMPING"
				|| state == "BLINK") {
			if (newState == "JUMPING") {
				bounds.height += 0.25;
			} else if (state == "JUMPING") {
				bounds.height -= 0.25;
			}
			notifyListeners(this, "State", state, newState);
			timeInState = 0;
			this.state = newState;
		}
	}
	
	public String getState() {
		return this.state;
	}
	
	/*
	public void setState(State newState) {
		if (timeInState > 3) {
			if (state == State.JUMPING) {
				bounds.height -= 0.25;
			}
			notifyListeners(this, "State", state, newState);
			timeInState = 0;
			this.state = newState;
		} else if (state == State.IDLE || state == State.JUMPING
				|| state == State.BLINK) {
			if (newState == State.JUMPING) {
				bounds.height += 0.25;
			} else if (state == State.JUMPING) {
				bounds.height -= 0.25;
			}
			notifyListeners(this, "State", state, newState);
			timeInState = 0;
			this.state = newState;
		}
	}

	public State getState() {
		return this.state;
	}
	*/
	
	public String getFinalState() {
		return finalState;
	}

	public void setFinalState(String state) {
		finalState = state;
	}
	
	/*
	public State getFinalState() {
		return finalState;
	}

	public void setFinalState(State state) {
		finalState = state;
	}
	*/
}
