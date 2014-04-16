package com.swordbit.game.model;

import com.badlogic.gdx.math.Vector2;
import com.swordbit.game.utils.Assets;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class Level {
	Eater eater;
	String LevelLocation;
	Vector2 eaterPosition;
	private TiledMap map;
	private String levelName;
	private LevelStatus levelStatus;
	private String maproot = "maps/MapFoodItems.tmx";
	//private String maproot = "maps/level4.tmx";

	enum LevelStatus {
		COMPLETED, FAILED, INPROGRESS
	}

	public Level(int i) {
		Assets.instance.getAssetManager().load(maproot,	TiledMap.class);
		Assets.instance.getAssetManager().finishLoading();
		int levelNumber = i;
		levelName = "Level " + levelNumber;
		LevelLocation = maproot;
		this.eater = new Eater(new Vector2(3.5f, 2.2f));
	}

	protected TiledMap getMap() {
		return this.map;
	}
	
	public void levelFailed() {
		this.levelStatus = LevelStatus.FAILED;
	}
	
	public String getLevelName() {
		return this.levelName;
	}
	
	protected void levelCompleted() {
		this.levelStatus = LevelStatus.COMPLETED;
	}

	protected boolean isLevelCompleted() {
		return levelStatus == LevelStatus.COMPLETED;
	}

	public void loadMap(String level) {
		map = Assets.instance.getAssetManager().get(level, TiledMap.class);
	}

}
