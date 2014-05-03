package com.swordbit.game.controller.collisions;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.swordbit.game.model.Tile;
import com.swordbit.game.model.World;
import com.swordbit.game.model.eater.Eater;

public class GroundCollisions {
	private World world;
	private Eater eater;
	private Pool<Tile> tilePool;
	private Array<Tile> tiles = new Array<Tile>();
		
	public GroundCollisions(World world) {
		this.world = world;
		this.eater = world.getEater();
		setUpTiles();
	}
	
	void update(float delta) {
		getTilesNearEater(delta);
		Rectangle eaterBounds = getEaterBounds(delta);
		eater.setGrounded(false);
		for (Tile tile : tiles) {
			checkContactWithTile(tile, eaterBounds);
		}
	}
	
	private void getTilesNearEater(float delta) {
		int startX, startY, endX, endY;
		startY = (int) (eater.getPosition().y + eater.getVelocity().y * delta);
		endY = startY;
		startX = (int) eater.getPosition().x;
		endX = (int) (eater.getPosition().x + eater.getBounds().width);
		getTiles(startX, startY, endX, endY, tiles);
	}
	
	private Rectangle getEaterBounds(float delta) {
		Rectangle eaterRect = new Rectangle(eater.getBounds());
		eaterRect.y += eater.getVelocity().y * delta;
		return eaterRect;
	}
	
	private void checkContactWithTile(Tile tile, Rectangle eaterBounds) {
		if (hasCollided(eaterBounds, tile)){
							eater.getPosition().y = tile.y + tile.height;
							eater.setGrounded(true);
							eater.getVelocity().y = 0;
			}
	}
	
	private boolean hasCollided(Rectangle eaterBounds, Tile tile) {
		boolean hasCollided = tile.overlaps(eaterBounds)   
							  && eater.getVelocity().y <= 0 
							  && tile.getCollidable().equals("true");
		return hasCollided;
	}
	
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
	
	// A pool of tiles is used to reduce memory usage
	private void setUpTiles() {
		tilePool = new Pool<Tile>() {
			protected Tile newObject() {
					return new Tile();
			}
		};
	}

}
