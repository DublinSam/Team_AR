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
	Vector2 acceleration = new Vector2(); 
	
	public Rectangle bounds = new Rectangle();
	public boolean grounded;
	public float timeInState;
	public static float SPEED = 8f;
	public static final float DAMPING = 0.8f;
	private String finalState;
	private String state = "IDLE";
	public static final Vector2 SIZE = new Vector2(1, 1);
	public static final Vector2 ACCELERATION = new Vector2(0, -30);
	private List<PropertyChangeListener> scoreListeners =  new ArrayList<PropertyChangeListener>();
	private List<PropertyChangeListener> stateListeners =  new ArrayList<PropertyChangeListener>();
	
	
	public Eater(Vector2 position) {
		this.timeInState = 0;
		this.position = position;
		this.acceleration = ACCELERATION;
		this.bounds.setSize(SIZE.x, SIZE.y);
		this.bounds.setPosition(position);
	}
	
	public void consumeFood(Food food) {
		updateScore(food);
		setState("EATING");
		setFinalState(food);
	}
	
	public void updateScore(Food food) {
		score += food.scoreValue;
		System.out.println("updating score");
		notifyScoreListeners(this, "Score", score + food.scoreValue, score);
	}
	
	public void setFinalState(Food food) {
		finalState = food.consequence;
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

	public void setState(String newState) {
		if (timeInState > 3) {
			if (state == "JUMPING") {
				bounds.height -= 0.25;
			}
			notifyStateListeners(this, "State", state, newState);
			timeInState = 0;
			this.state = newState;
		} else if (state == "IDLE" || state == "JUMPING"
				|| state == "BLINK") {
			if (newState == "JUMPING") {
				bounds.height += 0.25;
			} else if (state == "JUMPING") {
				bounds.height -= 0.25;
			}
			notifyStateListeners(this, "State", state, newState);
			timeInState = 0;
			this.state = newState;
		}
	}
	
//This method is going to cause eater to change size over time
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
		notifyStateListeners(this, "State", state, newState);
		this.state = newState;
		timeInState = 0;
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
	
	public String getState() {
		return this.state;
	}
	
	public String getFinalState() {
		return finalState;
	}
	
	public void addStateListener(PropertyChangeListener newListener) {
		stateListeners.add(newListener);
	}
	
	public void addScoreListener(PropertyChangeListener newListener) {
		scoreListeners.add(newListener);
	}

	private void notifyStateListeners(Object object, String property, String oldValue, String newValue) {
		for (PropertyChangeListener listener : stateListeners) {
			listener.propertyChange(new PropertyChangeEvent(this, "state", oldValue, newValue));
		}
	}
	
	private void notifyScoreListeners(Object object, String property,int oldScore, int newScore) {
		for (PropertyChangeListener listener : scoreListeners) {
			listener.propertyChange(new PropertyChangeEvent(this, "score", oldScore, newScore));
		}
	}
}
