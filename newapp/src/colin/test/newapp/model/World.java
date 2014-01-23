package colin.test.newapp.model;

import java.util.ArrayList;
import java.util.List;

import colin.test.newapp.LevelManager;
import colin.test.newapp.controller.WorldController.Tile;
import colin.test.newapp.util.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
/** The world where we place out eater and also the bits of food.**/

public class World {
	/** Our player controlled hero **/
	
	Eater eater;
	List<Food> foodInWorld;
	int difficulty;
	int foodMissed;
	int foodCollected;
	private TiledMap map;

	public LevelManager levelManager;
	private Pool<Food> foodPool = new Pool<Food>(){
		@Override
		protected Food newObject(){
			return new Food();
		}
	};
	public World(){
		levelManager = new LevelManager();
		Texture.setEnforcePotImages(false);
		foodInWorld = new ArrayList<Food>();
		createWorld();
		
	}
	

	public void createWorld(){
		String levelLocation=levelManager.getCurrentLevel().getFileString();
		map=Assets.instance.getAssetManager().get(levelLocation);
		this.eater=new Eater(new Vector2(3.5f,2.2f));
	
	}

	/**In current implementation the food array is never cleared, so once maxfood is reached no more items will be drawn in the game**/
	public void spawnFood(float posX, float posY){
		Food food=foodPool.obtain();
		food.setPosition(posX,posY);
		food.generateFoodType();
		
		foodInWorld.add(food);	
		
	}

	/**Returns eater from world**/
	public Eater getEater() {
		return eater;
	}
	
	/**Returns the array of food Items in the world**/
	public List<Food> getFood(){
		return foodInWorld;
	}
	public int getDifficulty(){
		return difficulty;
	}

	public int getFoodCollected(){
		return foodCollected;
	}
	public void increaseFoodCollected(){
		this.foodCollected++;
	}
	public void setFoodCollected(int foodCollected){
		this.foodCollected=foodCollected;
	}
	public int getFoodMissed() {
		return foodMissed;
	}
	public void increaseFoodMissed(){
		this.foodMissed++;
	}
	public void setFoodMissed(int foodMissed) {
		this.foodMissed=foodMissed;
	}
	public TiledMap getMap(){
		return this.map;
	}
	public void getCurrentLevel(){
		levelManager.getCurrentLevel();
	}
	public void nextLevel(){
		levelManager.nextLevel();
	}


	public void levelCompleted() {
		levelManager.levelSuccesfull();
	}
	public boolean isLevelCompleted(){
		return levelManager.getLevelSuccessfull();
	}


}