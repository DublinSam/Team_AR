package colin.test.newapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import colin.test.newapp.Eater;
import colin.test.newapp.Eater.State;
import colin.test.newapp.Collision;
import colin.test.newapp.Food;
import colin.test.newapp.World;
/**class responsible for updating velocity and positions based on input given at that point in time
 * (the final position is calculated in food/eater update method)**/
public class WorldController {
	enum Keys {
		LEFT, RIGHT
	}
	
	private float gameTime=0;
	private World world;
	private Eater eater;
	private ArrayList<Food> foodList;
	float timeSince=0;
	Collision collision;
	int spawnInterval=5;
	public WorldController(World world) {
		this.world = world;
		this.eater = world.getEater();
		this.foodList = world.getFood();
		collision = new Collision(world);
	}
	
	static Map<Keys, Boolean> keys = new HashMap<WorldController.Keys, Boolean>();
	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
	};
	
	public void leftPressed() {
		keys.get(keys.put(Keys.LEFT, true));
	}

	public void rightPressed() {
		keys.get(keys.put(Keys.RIGHT, true));
	}
	
	public void leftReleased() {
		keys.get(keys.put(Keys.LEFT, false));
	}
	
	public void rightReleased() {
		keys.get(keys.put(Keys.RIGHT, false));
	}
	
	public void update(float delta) {
		if(timeSince>spawnInterval){
			world.spawnFood(generateX(), 10);
			timeSince=0;
			}
		timeSince=timeSince+delta;
		processInput();
		
		foodList = world.getFood();
		eater.update(delta);
		if(!(foodList == null)){
			Iterator<Food> it=foodList.iterator();
			while(it.hasNext()){
				
				(it.next()).update(delta);
			}
		}
		collision.update(delta);
		gameTime+=delta;
		/*if(gameTime>4){
		 * world.spawnFood(3.5f, 10);
		 * }*/
	}

	private void processInput() {
		if (keys.get(Keys.LEFT)) {
			// left is pressed
			eater.setFacingLeft(true);
			eater.setState(State.MOVING);
			eater.getVelocity().x = -Eater.SPEED;
		}
		
		if (keys.get(Keys.RIGHT)) {
			// left is pressed
			eater.setFacingLeft(false);
			eater.setState(State.MOVING);
			eater.getVelocity().x = Eater.SPEED;
		}
		
		// need to check if both or none direction are pressed, then Bob is idle
		if ((keys.get(Keys.LEFT) && keys.get(Keys.RIGHT)) || (!keys.get(Keys.LEFT) && !(keys.get(Keys.RIGHT)))) {
			eater.setState(State.IDLE);
			// acceleration is 0 on the x
			eater.getAcceleration().x = 0;
			// horizontal speed is 0
			eater.getVelocity().x = 0;
		}
	}
	/**generate random X value for food spawning**/
	public float generateX(){
		   Random randomGenerator = new Random();
		    
		      float randomInt = randomGenerator.nextInt(7);
		      
		      return (float) (randomInt+0.5);
		      
		      
		      
	
	}
}
