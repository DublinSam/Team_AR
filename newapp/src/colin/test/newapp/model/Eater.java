package colin.test.newapp.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import colin.test.newapp.util.Assets;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Eater {
	/**State is only recorded in case we are going to be using some Movement or Idle animations, currently its not used for anything
	 * it is only set**/
	public enum State {
		IDLE, MOVING, HUNGRY, HOT,BORED,BLINK
	}

	private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();
	public float timeInState;
	int foodMissed;
	int foodCollected;
	public static final float SIZE = 1f; // half a unit
	public static final float DAMPING = 0.8f;
	public static float SPEED = 2.5f;	// unit per second
	Vector2 position = new Vector2();
	/**Acceleration not used**/
	Vector2 acceleration = new Vector2();
	Vector2 velocity = new Vector2();
	Rectangle bounds = new Rectangle();
	State state = State.IDLE;
	int score = 0;
	float cholesterol=0;
	float fullness=0;
	public boolean grounded;
	public Eater(Vector2 position) {
		this.acceleration.y=-7;
		this.position = position;
		this.bounds.height = SIZE;
		this.bounds.width = SIZE;
		this.velocity.x=2;
		this.timeInState=0;
	}
	
	public Vector2 getPosition(){
		return this.position;
	}
	
	public Rectangle getBounds(){
		return this.bounds;
	}

	
	public Vector2 getVelocity() {
	
		return this.velocity;
	}
	
	public void setState(State newState) {
		notifyListeners(this, "state", state, newState);
		timeInState=0;
		this.state = newState;
		
	}
	
	public void update(float delta) {
		timeInState+=delta;
		position.add(velocity.cpy().scl(delta));
		this.bounds.setPosition(position);
	}
	
	public Vector2 getAcceleration() {
		return this.acceleration;
	}
	
	public boolean isColliding(Food food){
		
		return this.bounds.overlaps(food.getBounds());
	}
	
	public void increaseScore(){
		score += 100;
		
	}
	public void decreaseScore(){
		score-=100;
		
	}
	public int getScore(){
		return score;
	}

	public State getState() {
		
		return this.state;
	}

	public float getTimeInState(){
		return this.timeInState;
	}
	
	private void notifyListeners(Object object, String property, State oldValue, State newValue) {
		    for (PropertyChangeListener name : listener) {
		      name.propertyChange(new PropertyChangeEvent(this, "eater", oldValue, newValue));
		    }
	}

	public void addChangeListener(PropertyChangeListener newListener) {
		    listener.add(newListener);
	}
	
	public void consumeFood(Food food){
		switch(food.getFoodType()){
		case STRAWBERRY:
			increaseScore();
			increaseFullness();
			break;
		case COOKIE:
			decreaseScore();
			increaseFullness();
			increaseCholesterol();
			break;
		case CHILLI:
			increaseScore();
			increaseFullness();
			break;
		case APPLE:
			increaseScore();
			increaseFullness();
			break;
		case LEMON:
			increaseScore();
			increaseFullness();
			break;
		
		case BURGER:
			increaseScore();
			increaseCholesterol();
			increaseFullness();
			break;
		case HOTDOG:
			increaseScore();
			increaseCholesterol();
			increaseScore();
			break;
		case PIZZA:
			increaseScore();
			increaseCholesterol();
			increaseFullness();
			
		}
	}

	private void increaseFullness() {
		this.fullness+=1;
		
	}

	private void increaseCholesterol() {
		this.cholesterol+=1;
		
	}

}
