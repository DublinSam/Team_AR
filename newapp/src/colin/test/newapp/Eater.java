package colin.test.newapp;



import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Eater {
	/**State is only recorded in case we are going to be using some Movement or Idle animations, currently its not used for anything
	 * it is only set**/
	public enum State {
		IDLE, MOVING
	}

	 public static final float SIZE = 0.5f; // half a unit
	public static final float SPEED = 5f;	// unit per second
     Vector2         position = new Vector2();
     Vector2         acceleration = new Vector2();
     Vector2         velocity = new Vector2();
     Rectangle         bounds = new Rectangle();
     State                state = State.IDLE;
     boolean                facingLeft = true;
     /**state time not used yet**/
     float                stateTime = 0;
 

	public Eater(Vector2 position) {
		this.position = position;
		this.bounds.height = SIZE;
		this.bounds.width = SIZE;
	}
	public Vector2 getPosition(){
		return this.position;
	}
	public Rectangle getBounds(){
		return this.bounds;
	}
	public void setFacingLeft(boolean b) {
		this.facingLeft=b;
		
	}
	public Vector2 getVelocity() {
		// TODO Auto-generated method stub
		return this.velocity;
	}
	
	public void setState(State newState) {
		    this.state = newState;
		}
	public void update(float delta) {
		    position.add(velocity.cpy().scl(delta));
		}
	public Vector2 getAcceleration() {
		// TODO Auto-generated method stub
		return this.acceleration;
	}
}
