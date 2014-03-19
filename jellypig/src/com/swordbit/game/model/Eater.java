package com.swordbit.game.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/** Eater AKA JellyPig **/
public class Eater {
	/**
	 * State is only recorded in case we are going to be using some Movement or
	 * Idle animations, currently its not used for anything it is only set
	 **/
	public enum State {
		IDLE, MOVING, HUNGRY, HOT, BORED, BLINK, JUMPING, ACNE, SOUR, FAT, HAPPY, EATING, TRANSFORMING
	}

	private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();
	public float timeInState;
	int foodMissed;
	int foodCollected;
	public static final float SIZE = 1f;
	public static final float DAMPING = 0.8f;
	public static float SPEED = 8f;
	Vector2 position = new Vector2();
	/** Acceleration not used **/
	Vector2 acceleration = new Vector2();
	Vector2 velocity = new Vector2();
	Rectangle bounds = new Rectangle();
	State state = State.IDLE;
	int score = 0;
	float cholesterol = 0;
	float fullness = 0;
	public boolean grounded;
	private State finalState;

	public Eater(Vector2 position) {
		this.acceleration.y = -30;
		this.position = position;
		this.bounds.setPosition(position);
		this.bounds.height = SIZE;
		this.bounds.width = SIZE;
		this.timeInState = 0;

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

	/** updates eaters position based on velocity **/
	public void update(float delta) {

		timeInState += delta;
		// add velocity times times time to do the distance traveled
		position.add(velocity.cpy().scl(delta));
		this.bounds.setPosition(position);
		if (!(state == State.IDLE)) {
			if (timeInState > 3) {
				setState(State.IDLE);
			}
		}
		if ((grounded) && state.equals(State.JUMPING)) {
			setState(State.IDLE);
		}
	}

	public Vector2 getAcceleration() {
		return this.acceleration;
	}

	public boolean isColliding(Food food) {

		return this.bounds.overlaps(food.getBounds());
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

	public State getState() {

		return this.state;
	}

	public float getTimeInState() {
		return this.timeInState;
	}

	private void notifyScoreListeners(Object object, String property,
			int oldScore, int newScore) {
		for (PropertyChangeListener name : listener) {
			name.propertyChange(new PropertyChangeEvent(this, "score",
					oldScore, newScore));
		}
	}

	private void notifyListeners(Object object, String property,
			State oldValue, State newValue) {
		for (PropertyChangeListener name : listener) {
			name.propertyChange(new PropertyChangeEvent(this, "state",
					oldValue, newValue));
		}
	}

	public void addChangeListener(PropertyChangeListener newListener) {
		listener.add(newListener);
	}

	public void consumeFood(Food food) {
		switch (food.getFoodType()) {
		case STRAWBERRY:
			increaseScore();
			setState(State.EATING);
			setFinalState(State.HAPPY);
			increaseFullness();
			break;
		case COOKIE:
			decreaseScore();
			setState(State.EATING);
			setFinalState(State.ACNE);
			increaseFullness();
			increaseCholesterol();
			break;
		case CHILLI:
			increaseScore();
			setState(State.EATING);
			setFinalState(State.HOT);
			increaseFullness();
			break;
		case APPLE:
			increaseScore();
			setState(State.EATING);
			setFinalState(State.HAPPY);
			increaseFullness();
			break;
		case LEMON:
			increaseScore();
			setState(State.EATING);
			setFinalState(State.SOUR);
			increaseFullness();
			break;

		case BURGER:
			increaseScore();
			setState(State.EATING);
			setFinalState(State.FAT);
			increaseCholesterol();
			increaseFullness();
			break;
		case HOTDOG:

			increaseScore();
			setState(State.EATING);
			setFinalState(State.FAT);
			increaseCholesterol();
			increaseScore();
			break;
		case PIZZA:
			increaseScore();
			setState(State.EATING);
			setFinalState(State.FAT);
			increaseCholesterol();
			increaseFullness();

		}
	}

	private void increaseFullness() {
		this.fullness += 1;

	}

	private void increaseCholesterol() {
		this.cholesterol += 1;

	}

	public void setGrounded(boolean b) {
		grounded = b;
		if (b && (state == State.JUMPING)) {
			// state=State.IDLE;
		}

	}

	public State getFinalState() {

		return finalState;
	}

	public void setFinalState(State state) {
		finalState = state;
	}

	public void forceState(State newState) {
		if (newState == State.JUMPING) {
			bounds.height += 0.25;
		}
		if (state == State.JUMPING) {
			bounds.height -= 0.25;
		}
		notifyListeners(this, "State", state, newState);
		this.state = newState;
		timeInState = 0;

	}

	public float getFullness() {

		return this.fullness;
	}

}
