package colin.test.newapp.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import colin.test.newapp.GameScreen.GameStatus;
import colin.test.newapp.controller.WorldController.Tile;
import colin.test.newapp.model.Eater.State;
import colin.test.newapp.util.Assets;
import colin.test.newapp.util.Assets.LevelManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
/** The world where we place out eater and also the bits of food.**/

public class World {
	/** Our player controlled hero **/
	private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();
	Eater eater;
	List<Food> foodInWorld;
	int difficulty;
	int foodMissed;
	int foodCollected;
	Level level;

	private Pool<Food> foodPool = new Pool<Food>(){
		@Override
		protected Food newObject(){
			return new Food();
		}
	};
	public World(int i){
		
		Texture.setEnforcePotImages(false);
		foodInWorld = new ArrayList<Food>();
		
		level = new Level(i);
		setLevel(i);
		createEater();
		
	}
	public World(){
		
		Texture.setEnforcePotImages(false);
		foodInWorld = new ArrayList<Food>();
		createEater();
		int x=Assets.instance.getLevelManager().getLevelIndex();
		setLevel(x);
	
		
		
	}
	
	
	public void createEater(){
		this.eater=new Eater(new Vector2(3.5f,3.00f));
	
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

	
	public boolean loadNextLevel(){
		boolean result=false;
		eater.position.x=3.5f;
		eater.position.y=5f;
		if(Assets.instance.getLevelManager().nextLevel()){
			result=true;
			level=Assets.instance.getLevelManager().getCurrentLevel();
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


public void levelCompleted() {
	this.level.levelCompleted();
	notifyListeners(this.level,"levelCompleted",GameStatus.INPROGRESS,GameStatus.LEVELCOMPLETED);
	
}

public void levelFailed() {
	this.level.levelFailed();
	notifyListeners(this.level,"levelCompleted",GameStatus.INPROGRESS,GameStatus.GAMEOVER);
}

public TiledMap getMap() {
	// TODO Auto-generated method stub
	return level.getMap();
}


public boolean isLevelCompleted() {
	return level.isLevelCompleted();
}

public void setLevel(int i){

	Assets.instance.getLevelManager().loadLevel(i);
	level=Assets.instance.getLevelManager().setLevel(i);
	level.loadMap();
	
}


}