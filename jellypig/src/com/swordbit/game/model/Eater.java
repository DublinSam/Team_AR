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
	Vector2 acceleration = new Vector2(); 
	
	public boolean grounded;
	public float timeInState;
	public static float SPEED = 6f;//8f
	public static final float SIZE = 1f;
	public static final float DAMPING = 0.8f;
	private String state = "IDLE";
	private String finalState;
	private int gas;
	private int maximumGas = 10;
	private int minimumGas = 0;
	private String typeFood = "";
	private boolean farting = false;
	private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();

	public Eater(Vector2 position) {
		this.timeInState = 0;
		this.position = position;
		this.bounds.width = SIZE;
		this.bounds.height = SIZE;
		this.acceleration.y = -30;
		this.gas = 0;
		this.bounds.setPosition(position);
	}
	
	public void consumeFood(Food food) {
		updateScore(food);
		setState("EATING");
		setFinalState(food);
		increaseFullness();
		incrementGas();
	}
	
	public void incrementGas(){
		if(typeFood == "JUNK"){
			gas = gas + 2;
		}
		else{
			gas = gas + 1;
		}
		if(gas >= maximumGas){
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
	
	public void updateScore(Food food) {
		score += food.scoreValue;
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
		if ((grounded) && state.equals("JUMPING")) {
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
		notifyListeners(this, "State", state, newState);
		this.state = newState;
		timeInState = 0;
	}
	
	public void increaseScore() {
		score += 100;
		notifyScoreListeners(this, "Score", score + 100, score);
	}
	public void decreaseScore() {
		score -= 100;
		notifyScoreListeners(this, "Score", score - 100, score);
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
//			if (state == "JUMPING") {
//				bounds.height -= 0.25;
//			}
			notifyListeners(this, "State", state, newState);
			timeInState = 0;
			this.state = newState;
		} else if (state == "IDLE" || state == "JUMPING"
				|| state == "BLINK") {
//			if (newState == "JUMPING") {
//				bounds.height += 0.25;
//			} else if (state == "JUMPING") {
//				bounds.height -= 0.25;
//			}
			notifyListeners(this, "State", state, newState);
			timeInState = 0;
			this.state = newState;
		}
	}
	
	public String getState() {
		return this.state;
	}
	
	public String getFinalState() {
		return finalState;
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
