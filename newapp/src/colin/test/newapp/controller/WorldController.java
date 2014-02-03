package colin.test.newapp.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import colin.test.newapp.GameScreen.GameStatus;
import colin.test.newapp.model.Eater;
import colin.test.newapp.model.Eater.State;
import colin.test.newapp.model.Food;
import colin.test.newapp.model.Food.FoodType;
import colin.test.newapp.model.World;
import colin.test.newapp.util.PreferencesHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
/**class responsible for updating velocity and positions based on input given at that point in time
 * (the final position is calculated in food/eater update method)**/
public class WorldController {
	public enum Keys {
		LEFT, RIGHT, JUMP
	}

    private Pool<Tile> tilePool = new Pool<Tile>() {
        @Override
        protected Tile newObject () {
                return new Tile();
        }
};
public class Tile extends Rectangle{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id;
	Tile(){
		super();
	}
	Tile(int id,Rectangle rect){
		super(rect);
		this.id=id;
	}
	public void setId(int id){
		this.id=id;
	}
	public int getId() {
		return this.id;
	}
	@Override
	public boolean overlaps(Rectangle r) {
		return x < r.x + r.width && x + width > r.x && y <= r.y + r.height && y + height >= r.y;
	}
	public boolean overlapsX(Rectangle r){
		return x <= r.x + r.width && x + width >= r.x && y < r.y + r.height && y + height > r.y;
	}
	
}
private Array<Tile> tiles = new Array<Tile>();
	OrthographicCamera cam;
	int height=10;
	int CAMERA_WIDTH=10;
	int CAMERA_HEIGHT=7;
	private World world;
	private Eater eater;
	private List<Food> foodList;
	PreferencesHelper prefs=new PreferencesHelper();
	public float timeSinceFoodSpawn=0;
	public float spawnInterval=3;
	public int maxNoOfFoodMisses=0;
	float timeNotBlinked=0;
	private boolean gameStarted= false;
	public WorldController(World world) {
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
		this.cam.update();
		this.world = world;
		this.eater = world.getEater();
		//set initial velocity of eater
		this.foodList = world.getFood();
		
	}
	
	public static Map<Keys, Boolean> keys = new HashMap<WorldController.Keys, Boolean>();
	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.JUMP, false);
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
	public void jumpPressed(){
		keys.get(keys.put(Keys.JUMP, true));
	
	}
	public void jumpReleased(){
		keys.get(keys.put(Keys.JUMP, false));
	}
	
	public GameStatus update(float delta) {
		if(gameStarted){
			System.out.println("y "+eater.getPosition().y);
		processInput();
	
		eater.getVelocity().add(new Vector2(0,eater.getAcceleration().y*delta));
		
		setEaterState();
		foodList = world.getFood();
		
        checkTileCollisions(delta);
    
		eater.update(delta);
		runFoodGenerator();
		updateFoodPosition();
		updateFoodCollision(delta);
		}
		return checkGameStatus();
	}
	
	private void checkTileCollisions(float delta) {
	
		checkYAxisCollision(delta);
		checkXAxisCollision(delta);
		
	
	}

	private void checkYAxisCollision(float delta) {
		Rectangle eaterRect = new Rectangle(eater.getBounds());
		int startX, startY, endX, endY;
        if(eater.getVelocity().y > 0) {
        startY = endY = (int)(eater.getPosition().y + eater.getBounds().height + eater.getVelocity().y*delta);
        } else {
        startY = endY = (int)(eater.getPosition().y + eater.getVelocity().y*delta);
        }
        startX = (int)(eater.getPosition().x);
        endX = (int)(eater.getPosition().x + eater.getBounds().width);
        getTiles(startX, startY, endX, endY, tiles);
        eaterRect.y += eater.getVelocity().y*delta;

        System.out.println("chonk "+tiles.size);
        for(Tile tile: tiles) {
        	System.out.println(tile.getId());
        	System.out.println(eater.getBounds().y+" "+tile.y);
        	if(tile.overlaps(eaterRect)) {
        		System.out.println("binnk");
                if(eater.getVelocity().y > 0) {
                	System.out.println("bonnk");
                        //eater.getPosition().y = tile.y - eater.getBounds().height;
                        //TiledMapTileLayer layer = (TiledMapTileLayer)world.getMap().getLayers().get(0);
                        //layer.setCell((int)tile.x, (int)tile.y, null);
                } else {
                System.out.println("duck");
                System.out.println("tileid "+tile.getId());
                		if(tile.getId()==47||tile.getId()==43){
                			System.out.println("pon "+eater.getBounds().y+" "+tile.y+" "+tile.height+" "+tile.getId());
                		
                		if(eater.getBounds().y>=tile.y+tile.height){
                        eater.getPosition().y = tile.y + tile.height;
                        eater.grounded = true;
                        eater.getVelocity().y = 0;
                		}
                		}
                		
                }
                
                break;
        	}
        }
		
	}

	private void checkXAxisCollision(float delta) {
		Tile eaterRect = new Tile(0,eater.getBounds());
		int startX, startY, endX, endY;
        if(eater.getVelocity().x > 0) {
            startX = endX = (int)(eater.getPosition().x + eater.getBounds().width + eater.getVelocity().x*delta);
        } else {
            startX = endX = (int)(eater.getPosition().x + eater.getPosition().x);
        }
        startY = (int)(eater.getPosition().y);
        endY = (int)(eater.getPosition().y + eater.getBounds().height);
        getTiles(startX, startY, endX, endY, tiles);
        eaterRect.x += eater.getVelocity().x*delta;
        for(Tile tile: tiles) {
        	if(eaterRect.overlapsX(tile)) {
        			
                    //eater.getVelocity().x = 0;
                    //eater.getPosition().x=tile.x-eaterRect.width;
                    break;
            }
    }
		
	}

	private void processInput() {
		if (keys.get(Keys.LEFT)) {

			eater.getVelocity().x = -Eater.SPEED;
		}
		
		if (keys.get(Keys.RIGHT)) {

			eater.getVelocity().x = Eater.SPEED;
		}
		
		// need to check if both or none direction are pressed, then Bob is idle
		if ((keys.get(Keys.LEFT) && keys.get(Keys.RIGHT)) || (!keys.get(Keys.LEFT) && !(keys.get(Keys.RIGHT)))) {
			//eater.setState(State.IDLE);
			// acceleration is 0 on the x
			eater.getAcceleration().x = 0;
			// horizontal speed is 0
			//eater.getVelocity().x = 0;
		}
		if(keys.get(Keys.JUMP)){
			
			if(eater.grounded){
				
			eater.getVelocity().y=11;
			}
		}
	}
	
	public boolean sideCollisionCheck(float delta){
		boolean result=false;
		if(eater.getVelocity().cpy().scl(delta).x+eater.getPosition().x-(eater.getBounds().width/2)<0){
			eater.getPosition().x=eater.getBounds().width/2;
		
			result=true;
			
		}
		if(eater.getVelocity().cpy().scl(delta).x+eater.getPosition().x+(eater.getBounds().width/2)>CAMERA_WIDTH){
			eater.getPosition().x=CAMERA_WIDTH-eater.getBounds().width/2;
			result=true;
			
		}
		return result;
	}
	
/**handles food generation, checking if food spawns will overlap and checking if it is time to spawn food**/
	public void runFoodGenerator() {
		if(timeSinceFoodSpawn>spawnInterval){
			float xSpawnPos=generateX()+eater.getPosition().x;
			boolean collisionDetected=checkFoodSpawnCollision(xSpawnPos);
			if(!collisionDetected){
				spawnFoodInWorld(xSpawnPos);
			}
			
		}
		else{
			timeSinceFoodSpawn+=Gdx.graphics.getDeltaTime();
		}
		
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
	


	public void spawnFoodInWorld(float xSpawnPos) {
		world.spawnFood(xSpawnPos, 6.75f);
		timeSinceFoodSpawn=0;
	}

	public GameStatus checkGameStatus() {
		GameStatus gameStatus=GameStatus.INPROGRESS;

		if(world.isLevelCompleted()){
			checkIfNewHighScore();
			gameStatus=GameStatus.LEVELCOMPLETED;
		}

		return gameStatus;
	}

	public void checkIfNewHighScore(){
		int currentHighScore=prefs.getHighScore();
		if(eater.getScore()>currentHighScore){
		prefs.saveHighScore(eater.getScore());
		prefs.save();
		}
	}
	
	/**generate random X value for food spawning**/
	private float generateX(){
		   Random randomGenerator = new Random();
		       float randomInt = randomGenerator.nextInt(7);
		       return (float) (randomInt+3.0);
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
	
	public void updateFoodCollision(float delta){
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
		processGroundCollision(item);
			
	}
	
	public void processEaterCollision(Food food) {
		if(eater.isColliding(food)){
			processFood(food);
			food.setExists(false);
		}
		
	}
	public void processGroundCollision(Food item) {
		double topOfFoodPos=item.getPosition().y+(item.getBounds().getHeight()/2);
		 if(topOfFoodPos<0){	
			item.setExists(false);
			if(item.getFoodType()==FoodType.STRAWBERRY){
				world.increaseFoodMissed();
			}
		
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
	public void processFood(Food food){
	
			eater.consumeFood(food);
			world.increaseFoodCollected();

	}
	public void setEaterState(){
	
			if(eater.getPosition().y<0){
				System.out.println("dead");
				world.levelFailed();
			}
			//if(eater.getPosition().x>20){
			//	world.levelCompleted();
			//}
			if(!(eater.getVelocity().y==0)){
				eater.grounded=false;
			}
	        timeNotBlinked += Gdx.graphics.getDeltaTime(); 
			  if(timeNotBlinked>5){
			        //blink animation
			        setBlinkAnimation();
			        timeNotBlinked=0;
			  }
			
		
	}
	private void getTiles(int startX, int startY, int endX, int endY, Array<Tile> tiles) {
        TiledMapTileLayer layer = (TiledMapTileLayer)world.getMap().getLayers().get(0);
        tilePool.freeAll(tiles);
        tiles.clear();
        for(int y = startY; y <= endY; y++) {
                for(int x = startX; x <= endX; x++) {
                        Cell cell = layer.getCell(x, y);
                        if(cell != null) {
                                Tile tile = tilePool.obtain();
                                tile.setId(cell.getTile().getId());
                                if(tile.getId()==1){
                                	tile.set(x, y, 1, 1);
                                }
                                else{
                                	tile.set(x, y, 1, 1);
                                }
                                tiles.add(tile);
                                
                        }
                }
        }
}
	private void setBlinkAnimation() {
		Eater eater=world.getEater();
		eater.setState(State.BLINK);
		timeNotBlinked=0;
	}
	
	public void beginTouched(){
		gameStarted=true;
		eater.getVelocity().x=8;
	}

}
