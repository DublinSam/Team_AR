package com.swordbit.game.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.swordbit.game.init.GdxGame;
import com.swordbit.game.utils.Assets;

public class LevelTest {
	
	LwjglApplication sampleApp;
	GdxGame sampleGame;
	
	@Before
	public void setUp() {
	    sampleGame = new GdxGame();      
	    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	    config.height = 5;
	    config.width = 5;
	    config.disableAudio = true;
	    sampleApp = new LwjglApplication(sampleGame, config);
	}

	@Test
	public void testLevelConstructor() {
		
		int sampleLevelNumber = 5;
		//Level sampleLevel = new Level(sampleLevelNumber);
		AssetManager manager = Assets.instance.getAssetManager();
		/*
		 * 	public Level(int i) {
		Assets.instance.getAssetManager().load("maps/level" + i + ".tmx",
				TiledMap.class);
		Assets.instance.getAssetManager().finishLoading();
		int levelNumber = i + 1;
		levelName = "Level " + levelNumber;
		LevelLocation = "maps/level" + i + ".tmx";
		this.eater = new Eater(new Vector2(3.5f, 2.2f));
	}
		 */
	}

}
