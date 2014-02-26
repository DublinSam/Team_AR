package colin.test.newapp.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import colin.test.newapp.controller.WorldController.Tile;
import colin.test.newapp.model.Eater.State;
import colin.test.newapp.util.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.swordbit.game.GameScreen.GameStatus;
/** The world where we place out eater and also the bits of food.**/

public class World {
	/** Our player controlled hero **/
	private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();
	Eater eater;
	List<Food> foodInWorld;
	int difficulty;
	/**food that has not been collected**/
	int foodMissed;
	/**food that has been collected**/
	int foodCollected;
	/**Current level in world**/
	Level level;
	/**array of all map file locations**/
	String[] levelLocations;
	/**total number of levels**/
	int noOfLevels=6;
	/**current level index**/
	int currentLevel;
	private Pool<Food> foodPool = new Pool<Food>(){
		@Override
		protected Food newObject(){
			return new Food();
		}
	};
	public World(int i){
		currentLevel=i;
		level= new Level(i);
		levelLocations = new String[noOfLevels];
		for(int j=0;j<noOfLevels;j++){
			levelLocations[j]="maps/level"+j+".tmx";
		}
		Texture.setEnforcePotImages(false);
		foodInWorld = new ArrayList<Food>();
		
		setLevel(levelLocations[i]);
		createEater();
		
	}
	public World(){
		currentLevel=0;
		level= new Level(0);
		levelLocations = new String[noOfLevels];
		for(int j=0;j<noOfLevels;j++){
			levelLocations[j]="maps/level"+j+".tmx";
		}
		foodInWorld = new ArrayList<Food>();
		createEater();
		setLevel(levelLocations[0]);
	
		
		
	}
	
	
	public void createEater(){
		this.eater=new Eater(new Vector2(1f,1f));
	
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

	
	public boolean nextLevelExists(int i){
		boolean result = false;
		if(i+1<noOfLevels){
			result=true;
		}

		return result;
	}


	public Level getCurrentLevel() {
		
		return this.level;
	}
	private void notifyListeners(Object object, String property, GameStatus oldValue, GameStatus newValue) {
	    for (PropertyChangeListener name : listener) {
	      name.propertyChange(new PropertyChangeEvent(this, "world", oldValue, newValue));
	    }
}

public void addChangeListener(PropertyChangeListener newListener) {
	    listener.add(newListener);
}
public int getCurrentLevelIndex(){
	return currentLevel;
}

public void levelCompleted() {
	this.level.levelCompleted();
	notifyListeners(this.level,"levelCompleted",GameStatus.INPROGRESS,GameStatus.LEVELCOMPLETED);
	
}

public void levelFailed() {
	this.level.levelFailed();
	notifyListeners(this.level,"levelCompleted",GameStatus.INPROGRESS,GameStatus.GAMEOVER);
}

public TiledMap getMap() {
	return level.getMap();
}

public boolean isLevelCompleted() {
	return level.isLevelCompleted();
}

public void setLevel(String levelLoc){

	level.loadMap(levelLoc);
}
public int getLevelCount() {

	return noOfLevels;
}


}