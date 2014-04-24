package com.swordbit.game.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.swordbit.game.model.Eater;
import com.swordbit.game.model.SoundEffects;
import com.swordbit.game.model.food.Food;
import com.swordbit.game.model.World;
import com.swordbit.game.utils.Assets;
import com.swordbit.game.utils.PreferencesHelper;
import com.swordbit.game.view.screens.GameScreen.GameStatus;

/**
 * class responsible for updating velocity and positions based on input given at
 * that point in time (the final position is calculated in food/eater update
 * method)
 **/
public class WorldController {

	private Pool<Tile> tilePool = new Pool<Tile>() {
		@Override
		protected Tile newObject() {
			return new Tile();
		}
	};

	int tileWidth = 48;
	private Array<Tile> tiles = new Array<Tile>();
	private Array<MapObject> mapObjects = new Array<MapObject>();
	OrthographicCamera cam;
	int height = 10;
	int CAMERA_WIDTH = 10;
	int CAMERA_HEIGHT = 7;
	private World world;
	private Eater eater;
	
	private List<Food> foodList;
	PreferencesHelper prefs = new PreferencesHelper();
	public float timeSinceFoodSpawn = 0;
	public float spawnInterval = 3;
	float timeNotBlinked = 0;
	private boolean gameStarted = false;

	public WorldController(World world) {
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
		this.cam.update();
		this.world = world;
		this.eater = world.getEater();
		this.foodList = world.getFood();
	}

	public void jumpPressed() {
		if (eater.grounded) {
			eater.setState("JUMPING");
			eater.getVelocity().y = 12;
			SoundEffects.instance.play(Assets.instance.sounds.jump);
		}
	}
	
	public void jumpReleased() {
		if (eater.getVelocity().y > 6) {
			eater.getVelocity().y = 6;
		}
	}
	
	public void fartPressed(){
		if(eater.grounded){
			//eater.setState("FARTING");
			//eater.getVelocity().x = 12;
			eater.decrementGas();
			SoundEffects.instance.play(Assets.instance.sounds.fart);
		}
	}

	public void update(float delta) {
		if (gameStarted) {
			eater.getVelocity().add(
					new Vector2(0, eater.getAcceleration().y * delta));
			setEaterState();
			foodList = world.getFood();

			checkTileCollisions(delta);
			checkObjectCollisions(delta);
			eater.update(delta);
			runFoodGenerator();
			updateFoodCollision(delta);
		}

	}

	private void checkObjectCollisions(float delta) {
		getObjects();
		boolean crystalCollision = false;
		for (int i = 0; i < mapObjects.size; i++) {

			Rectangle objRectangle = ((RectangleMapObject) mapObjects.get(i))
					.getRectangle();
			float rectangleWidth = objRectangle.width / tileWidth;
			float rectangleHeight = objRectangle.height / tileWidth;
			float rectangleX = objRectangle.x / tileWidth;
			float rectangleY = objRectangle.y / tileWidth;
			Rectangle rect = new Rectangle(rectangleX, rectangleY,
					rectangleWidth, rectangleHeight);

			if (rect.overlaps(eater.getBounds())) {
				String finish = (String) mapObjects.get(i).getProperties()
						.get("finish");
				if (!(finish == null)) {
					if (finish.equals("true")) {
						world.levelCompleted();
					}
				} else {
					world.levelFailed();
				}
			}
		}

		mapObjects.clear();
	}

	/** returns any objects that are passed the center of the screen **/
	private void getObjects() {
		MapObjects objects = world.getMap().getLayers().get("Collision")
				.getObjects();

		for (int i = 0; i < objects.getCount(); i++) {
			if ((((RectangleMapObject) objects.get(i)).getRectangle().x / tileWidth) < eater
					.getPosition().x + CAMERA_WIDTH / 2) {
				mapObjects.add((objects.get(i)));
			}
		}
	}

	private void checkTileCollisions(float delta) {
		checkYAxisCollision(delta);
		// checkXAxisCollision(delta);
	}

	/** checks if eater rectangle is about to land on a tile **/
	private void checkYAxisCollision(float delta) {
		Rectangle eaterRect = new Rectangle(eater.getBounds());
		int startX, startY, endX, endY;
		if (eater.getVelocity().y > 0) {
			startY = endY = (int) (eater.getPosition().y
					+ eater.getBounds().height + eater.getVelocity().y * delta);
		} else {
			startY = endY = (int) (eater.getPosition().y + eater.getVelocity().y
					* delta);
		}
		startX = (int) (eater.getPosition().x);
		endX = (int) (eater.getPosition().x + eater.getBounds().width);
		getTiles(startX, startY, endX, endY, tiles);
		eaterRect.y += eater.getVelocity().y * delta;

		for (Tile tile : tiles) {

			if (tile.overlaps(eaterRect)) {

				if (eater.getVelocity().y > 0) {

					// eater.getPosition().y = tile.y -
					// eater.getBounds().height;
					// TiledMapTileLayer layer =
					// (TiledMapTileLayer)world.getMap().getLayers().get(0);
					// layer.setCell((int)tile.x, (int)tile.y, null);
				} else {

					if (tile.getCollidable().equals("true")) {

						if (eater.getBounds().y >= tile.y + tile.height) {
							eater.getPosition().y = tile.y + tile.height;
							eater.setGrounded(true);
							eater.getVelocity().y = 0;
							
						}
					}

				}

				break;
			}
		}

	}

	private void checkXAxisCollision(float delta) {
		Tile eaterRect = new Tile("0", eater.getBounds());
		int startX, startY, endX, endY;
		if (eater.getVelocity().x > 0) {
			startX = endX = (int) (eater.getPosition().x
					+ eater.getBounds().width + eater.getVelocity().x * delta);
		} else {
			startX = endX = (int) (eater.getPosition().x + eater.getPosition().x);
		}
		startY = (int) (eater.getPosition().y);
		endY = (int) (eater.getPosition().y + eater.getBounds().height);
		getTiles(startX, startY, endX, endY, tiles);
		eaterRect.x += eater.getVelocity().x * delta;
		for (Tile tile : tiles) {
			if (eaterRect.overlapsBoundary(tile)) {

				// eater.getVelocity().x = 0;
				// eater.getPosition().x=tile.x-eaterRect.width;
				break;
			}
		}

	}

	/**
	 * handles food generation, checking if food spawns will overlap and
	 * checking if it is time to spawn food
	 **/
	public void runFoodGenerator() {
		if (timeSinceFoodSpawn > spawnInterval) {
			float xSpawnPos = generateX() + eater.getPosition().x;
			boolean collisionDetected = checkFoodSpawnCollision(xSpawnPos);
			if (!collisionDetected) {
				spawnFoodInWorld(xSpawnPos);
			}

		} else {
			timeSinceFoodSpawn += Gdx.graphics.getDeltaTime();
		}

	}

	public void spawnFoodInWorld(float xSpawnPos) {
		world.spawnFood(xSpawnPos, 6.75f);
		timeSinceFoodSpawn = 0;
	}

	public GameStatus checkGameStatus() {
		GameStatus gameStatus = GameStatus.INPROGRESS;
		if (world.isLevelCompleted()) {
			checkIfNewHighScore();
			gameStatus = GameStatus.LEVELCOMPLETED;
		}
		return gameStatus;
	}

	public void checkIfNewHighScore() {
		int currentHighScore = prefs.getHighScore();
		if (eater.getScore() > currentHighScore) {
			prefs.saveHighScore(eater.getScore());
			prefs.save();
		}
	}

	/** generate random X value for food spawning **/
	private float generateX() {
		Random randomGenerator = new Random();
		float randomInt = randomGenerator.nextInt(7);
		return (float) (randomInt + 3.0);
	}

	/** checks if a food is about to spawn over another item of food **/
	public boolean checkFoodSpawnCollision(float xSpawnPos) {
		boolean result = false;
		if (!(foodList == null)) {
			Iterator<Food> it = foodList.iterator();
			while (it.hasNext()) {
				Food currentFoodItem = it.next();
				boolean currentFoodItemOverlap = currentFoodItem.getBounds()
						.contains(xSpawnPos, 9.75f);
				if (currentFoodItemOverlap) {
					result = currentFoodItemOverlap;
				}

			}
		}
		return result;
	}

	public void updateFoodCollision(float delta) {
		Iterator<Food> foodItemIterator = world.getFood().iterator();
		while (foodItemIterator.hasNext()) {
			Food currentFoodItem = foodItemIterator.next();
			checkCollision(currentFoodItem);
			if (currentFoodItem.getExists() == false) {
				foodItemIterator.remove();
			}
		}
	}


	/**
	 * checks for collisions between food items + eater and for food that goes
	 * past bottom of screen
	 */
	public void checkCollision(Food item) {
		processEaterCollision(item);
		processGroundCollision(item);
	}

	public void processEaterCollision(Food food) {
		if (eater.isColliding(food)) {
			eater.setTypeOfFood(food);
			processFood(food);
			food.setExists(false);
		}

	}

	public void processGroundCollision(Food item) {
		double topOfFoodPos = item.getPosition().y
				+ (item.getBounds().getHeight() / 2);
		if (topOfFoodPos < 0) {
			item.setExists(false);
			//if (item.getFoodType() == FoodType.STRAWBERRY) {
			//	world.increaseFoodMissed();
			//}

		}
	}
	
	public void processFood(Food food) {

		eater.consumeFood(food);
		world.increaseFoodCollected();

	}

	public void setEaterState() {
		if (eater.getPosition().y < 0) {
			world.levelFailed();
		}
		if (!(eater.getVelocity().y == 0)) {
			eater.setGrounded(false);
		}
		timeNotBlinked += Gdx.graphics.getDeltaTime();
		if (timeNotBlinked > 5) {
			setBlinkAnimation();
			timeNotBlinked = 0;
		}
	}

	/** Gets tiles that are to be considered for collision detection **/
	private void getTiles(int startX, int startY, int endX, int endY,Array<Tile> tiles) {
		TiledMapTileLayer layer = (TiledMapTileLayer) world.getMap().getLayers().get("Terrain");
				tilePool.freeAll(tiles);

		tiles.clear();
		for (int y = startY; y <= endY; y++) {
			for (int x = startX; x <= endX; x++) {
				Cell cell = layer.getCell(x, y);
				if (cell != null) {
					Tile tile = tilePool.obtain();
					if (cell.getTile().getProperties().containsKey("collision")) {

						tile.setCollidable((String) cell.getTile()
								.getProperties().get("collision"));
					} else {
						tile.setCollidable("false");
					}
					tile.set(x, y, 1f, 1f);

					tiles.add(tile);

				}
			}
		}
	}

	private void setBlinkAnimation() {
		Eater eater = world.getEater();
		eater.setState("BLINK");
		timeNotBlinked = 0;
	}

	public void beginGame() {
		gameStarted = true;
		eater.getVelocity().x = eater.SPEED;
	}

	public class Tile extends Rectangle {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String collidable;

		Tile() {
			super();
		}

		Tile(String collidable, Rectangle rect) {
			super(rect);
			this.collidable = collidable;
		}

		public String getCollidable() {
			return this.collidable;
		}

		@Override
		public boolean overlaps(Rectangle r) {
			return x < r.x + r.width && x + width > r.x && y <= r.y + r.height
					&& y + height >= r.y;
		}

		public boolean overlapsBoundary(Rectangle r) {
			return x <= r.x + r.width && x + width >= r.x
					&& y <= r.y + r.height && y + height >= r.y;
		}

		public void setCollidable(String collidable) {
			this.collidable = collidable;
		}

	}
}