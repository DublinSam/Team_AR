package colin.test.newapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import colin.test.newapp.model.Eater;
import colin.test.newapp.model.Food;
import colin.test.newapp.model.World;
import colin.test.newapp.model.Eater.State;
import colin.test.newapp.util.PreferencesHelper;
/**class responsible for updating velocity and positions based on input given at that point in time
 * (the final position is calculated in food/eater update method)**/
public class WorldController {
	enum Keys {
		LEFT, RIGHT
	}
	OrthographicCamera cam;
	int height=10;
	int CAMERA_WIDTH=7;
	int CAMERA_HEIGHT=10;
	private World world;
	private Eater eater;
	private ArrayList<Food> foodList;
	PreferencesHelper prefs=new PreferencesHelper();
	float timeSinceFoodSpawn=0;
	float spawnInterval=3;
	public WorldController(World world) {
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
		this.cam.update();
		this.world = world;
		this.eater = world.getEater();
		//set initial velocity of eater
		this.eater.getVelocity().x=2.5f;
		this.foodList = world.getFood();
		
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
	
	public boolean update(float delta) {
		//processInput();
		foodList = world.getFood();
		setEaterVelocity(delta);
		eater.update(delta);
		runFoodGenerator();
		updateFoodPosition();
		updateCollision(delta);
		return checkGameState();
	}

/**handles food generation, checking if food spawns will overlap and checking if it is time to spawn food**/
	private void runFoodGenerator() {
		if(timeSinceFoodSpawn>spawnInterval){
			float xSpawnPos=generateX();
			boolean collisionDetected=checkFoodSpawnCollision(xSpawnPos);
			if(!collisionDetected){
				spawnFoodInWorld(xSpawnPos);
			}
			
		}
		else{
			timeSinceFoodSpawn+=Gdx.graphics.getDeltaTime();
		}
		
		}	



	private void spawnFoodInWorld(float xSpawnPos) {
		world.spawnFood(xSpawnPos, 9.75f);
		timeSinceFoodSpawn=0;
	}

	private boolean checkGameState() {
		boolean gameOver=false;
		if(world.getFoodMissed()>0){
			checkIfNewHighScore();
			gameOver=true;
		}
		return gameOver;
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
	public void checkIfNewHighScore(){
		int currentHighScore=prefs.getHighScore();
		if(eater.getScore()>currentHighScore){
		prefs.saveHighScore(eater.getScore());
		prefs.save();
		}
	}
	/**generate random X value for food spawning**/
	public float generateX(){
		   Random randomGenerator = new Random();
		       float randomInt = randomGenerator.nextInt(7);
		       return (float) (randomInt+0.5);
	}

	public void setEaterVelocity(float delta){
		if(eater.getVelocity().cpy().scl(delta).x+eater.getPosition().x-(eater.getBounds().width/2)<0){
			eater.getPosition().x=eater.getBounds().width/2;
			eater.getVelocity().x=eater.SPEED;
		}
		if(eater.getVelocity().cpy().scl(delta).x+eater.getPosition().x+(eater.getBounds().width/2)>CAMERA_WIDTH){
			eater.getPosition().x=CAMERA_WIDTH-eater.getBounds().width/2;
			eater.getVelocity().x=-eater.SPEED;
			
		}
	}
/**checks if a food is about to spawn over another item of food**/
	public boolean checkFoodSpawnCollision(float xSpawnPos){
		boolean result=false;
		if(!(foodList == null)){
			Iterator<Food> it=foodList.iterator();
			while(it.hasNext()){
				Food currentFoodItem=it.next();
				boolean currentFoodItemOverlap=currentFoodItem.getBounds().contains(xSpawnPos, 9.75f);
				if(currentFoodItemOverlap){
					result=currentFoodItemOverlap;
				}
		
			}
		}
		return result;
	}
	public void updateFoodPosition(){
		if(!(foodList == null)){
			Iterator<Food> it=foodList.iterator();
			while(it.hasNext()){
				Food currentFoodItem=it.next();
				(currentFoodItem).update(Gdx.graphics.getDeltaTime());
			}
		}
	}
	

	
	public void updateCollision(float delta){
		Iterator<Food> foodItemIterator=world.getFood().iterator();
		while(foodItemIterator.hasNext()){
			Food currentFoodItem = foodItemIterator.next();
			releaseFoodOnClick(currentFoodItem);
			checkCollision(currentFoodItem);
			if(currentFoodItem.getExists()==false){
				foodItemIterator.remove();
			}
		}
	}
	private void releaseFoodOnClick(Food currentFoodItem) {
		if(Gdx.input.justTouched()){
			releaseFood(currentFoodItem);
			}
		
	}

	/**checks for collisions between food items + eater and for food that goes past bottom of screen*/
	public void checkCollision(Food item){
		processEaterCollision(item);
		//Food missed only increases for good food missed
		processGroundCollision(item);
			
	}
	private void processGroundCollision(Food item) {
		double topOfFoodPos=item.getPosition().y+(item.getBounds().getHeight()/2);
		 if(topOfFoodPos<0&&item.getFoodType()==Food.strawberry){
			world.increaseFoodMissed();
			item.setExists(false);
		
		 }
	}

	private void processEaterCollision(Food food) {
		if(eater.isColliding(food)){
			updateScore(food);
			food.setExists(false);
		}
		
	}

	/**checks if food item has been clicked**/
	public void releaseFood(Food food){
		int x=Gdx.input.getX();
		int y=Gdx.input.getY();
		Vector3 touchVec=new Vector3(x,y,0);
		cam.unproject(touchVec);
		Vector2 touchVec2d=new Vector2(touchVec.x,touchVec.y);
		if(food.getBounds().contains(touchVec2d)){
			System.out.println("touched");
			food.release();
		}
	}
	public void updateScore(Food food){
		if(food.getFoodType()==Food.strawberry){
			eater.increaseScore();
			world.increaseFoodCollected();
			world.increaseDifficulty();
		}
		else{
				eater.decreaseScore();
		}
	}
}
