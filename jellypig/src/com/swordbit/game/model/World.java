package com.swordbit.game.model;

import java.util.List;
import java.util.ArrayList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.swordbit.game.model.food.Food;
import com.swordbit.game.model.food.Strawberry;
import com.swordbit.game.utils.Assets;
import com.swordbit.game.view.screens.GameScreen.GameStatus;
import com.swordbit.game.model.GameMusic;

//The world where we place the eater and food.
public class World { 
	private Eater eater;
	/*Jesus - adding sound effects*/
	private SoundEffects jump;
	private Level level; // Current level in world 
	private int difficulty; 
	private int foodMissed; 
	private int currentLevel; // current level index
	private int foodCollected; 
	private int noOfLevels = 1; // total number of levels 
	private List<Food> foodInWorld;
	private String[] levelLocations; // array of all map file locations
	private List<PropertyChangeListener> listener =
				new ArrayList<PropertyChangeListener>();
	//private String maproot = "maps/level4.tmx";
	private String maproot = "maps/MapFoodItems.tmx";
	
	
	private Pool<Food> foodPool = new Pool<Food>() {
		@Override
		protected Food newObject() {
			return new Strawberry(); // Testing the mechanism
		}
	};
	
	public World() {
		System.out.println(":)");
		currentLevel = 0;
		level = new Level(0);
		levelLocations = new String[noOfLevels];
		levelLocations[0] = maproot;
//		for (int j = 0; j < noOfLevels; j++) {
//			levelLocations[j] = "maps/level" + j + ".tmx";
//		}
		foodInWorld = new ArrayList<Food>();
		setLevel(levelLocations[0]);
		initSoundEffects();
		//initGameMusic();
		GameMusic.instance.play(Assets.instance.music.soundtrack);
		System.out.println("World created!");
		createEater();
	}

	public World(int i) {
		currentLevel = 0;
		level = new Level(0);
		levelLocations = new String[noOfLevels];
		levelLocations[0] = maproot;
//		for (int j = 0; j < noOfLevels; j++) {
//			levelLocations[j] = "maps/level" + j + ".tmx";
//		}
		foodInWorld = new ArrayList<Food>();
		setLevel(maproot);
		initSoundEffects();
		GameMusic.instance.play(Assets.instance.music.soundtrack);
		createEater();
	}

	public void createEater() {
		this.eater = new Eater(new Vector2(0f, 15f));//hack hack eater position problem
	}
	
	/*Jesus adding sound effects test*/
	public void initSoundEffects(){
		this.jump = new SoundEffects();
	}

	/**
	 * In current implementation the food array is never cleared, so once
	 * maxfood is reached no more items will be drawn in the game
	 **/
	public void spawnFood(float posX, float posY) {
		Food food = foodPool.obtain();
		food.setPosition(posX, posY);
		// food.generateFoodType();

		foodInWorld.add(food);

	}

//	public boolean nextLevelExists(int i) {
//		boolean result = false;
//		if (i + 1 < noOfLevels) {
//			result = true;
//		}
//		return result;
//	}

	private void notifyListeners(Object object, String property,
			GameStatus oldValue, GameStatus newValue) {
		for (PropertyChangeListener name : listener) {
			name.propertyChange(new PropertyChangeEvent(this, "world",
					oldValue, newValue));
		}
	}

	public void addChangeListener(PropertyChangeListener newListener) {
		listener.add(newListener);
	}
	
	/** Returns eater from world **/
	public Eater getEater() {
		return eater;
	}
	/*Jesus adding sound fx test*/
	public SoundEffects getSoundEffects(){
		return jump;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	/** Returns the array of food Items in the world **/
	public List<Food> getFood() {
		return foodInWorld;
	}

	public int getFoodMissed() {
		return foodMissed;
	}

	public void increaseFoodMissed() {
		this.foodMissed++;
	}
	
	public int getFoodCollected() {
		return foodCollected;
	}

	public void increaseFoodCollected() {
		this.foodCollected++;
	}

	public void setFoodMissed(int foodMissed) {
		this.foodMissed = foodMissed;
	}
	
	public void setFoodCollected(int foodCollected) {
		this.foodCollected = foodCollected;
	}

	public TiledMap getMap() {
		return level.getMap();
	}
	
	public int getLevelCount() {
		return noOfLevels;
	}
	
	public Level getCurrentLevel() {
		return this.level;
	}

	public int getCurrentLevelIndex() {
		return currentLevel;
	}
	
	public boolean isLevelCompleted() {
		return level.isLevelCompleted();
	}

	public void setLevel(String levelLoc) {
		level.loadMap(levelLoc);
	}

	public void levelCompleted() {
		this.level.levelCompleted();
		notifyListeners(this.level, "levelCompleted", GameStatus.INPROGRESS,
				GameStatus.LEVELCOMPLETED);
	}

	public void levelFailed() {
		this.level.levelFailed();
		notifyListeners(this.level, "levelCompleted", GameStatus.INPROGRESS,
				GameStatus.GAMEOVER);
	}

}