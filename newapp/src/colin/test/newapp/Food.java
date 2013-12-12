package colin.test.newapp;



import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Food {
	public enum State {
		IDLE, MOVING
	}

	 public static final float SIZE = 0.5f; // half a unit
	public static final float SPEED = -2f;	// unit per second
     Vector2         position = new Vector2();
     Vector2         acceleration = new Vector2();
     Vector2         velocity = new Vector2();
     Rectangle         bounds = new Rectangle();

   
     float                stateTime = 0;
 

	public Food(Vector2 position) {
		this.position = position;
		this.bounds.height = SIZE;
		this.bounds.width = SIZE;
		this.velocity=new Vector2(0,SPEED);
	}
	public Vector2 getPosition(){
		return this.position;
	}
	public Rectangle getBounds(){
		return this.bounds;
	}

	public Vector2 getVelocity() {
		// TODO Auto-generated method stub
		return this.velocity;
	}

	public void update(float delta) {
		    position.add(velocity.cpy().scl(delta));
		}
	public Vector2 getAcceleration() {
		// TODO Auto-generated method stub
		return this.acceleration;
	}
}

