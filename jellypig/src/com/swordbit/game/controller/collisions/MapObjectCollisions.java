package com.swordbit.game.controller.collisions;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.swordbit.game.model.World;
import com.swordbit.game.model.eater.Eater;
import com.swordbit.game.model.eater.Eater.HealthState;
import com.swordbit.game.utils.Constants;

public class MapObjectCollisions {
	private World world;
	private Eater eater;
	private int CAMERA_WIDTH = 10;
	private int tileWidth = Constants.TILE_WIDTH;	
	private Array<MapObject> mapObjects = new Array<MapObject>();

	public MapObjectCollisions(World world) {
		this.world = world;
		this.eater = world.getEater();
	}

	public void update(float delta) {
		getMapObjectsNearEater();	
		for (int i = 0; i < mapObjects.size; i++) {
			Rectangle mapObjectBounds = getMapObjectBounds(i);
			if (mapObjectBounds.overlaps(eater.getBounds())) {
				checkCrystalCollision();
				checkLevelFinish(i);
			}
		}
		mapObjects.clear();
		
	}
	
	/** returns any objects that are passed the center of the screen **/
	private void getMapObjectsNearEater() {
		MapObjects objects =  world.getMap().getLayers().get("Collision").getObjects();

		for (int i = 0; i < objects.getCount(); i++) {
			if ((((RectangleMapObject) objects.get(i)).getRectangle().x / tileWidth) 
					< eater.getPosition().x + CAMERA_WIDTH / 2) {
				mapObjects.add((objects.get(i)));
			}
		}
	}
	
	private Rectangle getMapObjectBounds(int i) {
		Rectangle objRectangle = 
				((RectangleMapObject) mapObjects.get(i)).getRectangle();
		
		float rectangleWidth = objRectangle.width / tileWidth;
		float rectangleHeight = objRectangle.height / tileWidth;
		float rectangleX = objRectangle.x / tileWidth;
		float rectangleY = objRectangle.y / tileWidth;
		
		Rectangle mapObjectBounds = 
				new Rectangle(rectangleX, rectangleY, rectangleWidth, rectangleHeight);
		return mapObjectBounds;
		
	}
	
	private void checkCrystalCollision() {
		if (eater.getHealthState() != HealthState.INVINCIBLE) {
			world.levelFailed();
	}
	}
	
	private void checkLevelFinish(int i) {
		String finish = (String) mapObjects.get(i).getProperties().get("finish");
		if (!(finish == null)) {
			if (finish.equals("true")) {
				world.levelCompleted();
			}
		}
		
	}

}
