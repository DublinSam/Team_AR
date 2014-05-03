package com.swordbit.game.controller.collisions;

import com.swordbit.game.model.World;

public class CollisionDetector {
	private GroundCollisions groundCollisions;
	private MapObjectCollisions mapObjectCollisions;
	private FoodCollisions foodCollisions;
	
	public CollisionDetector(World world) {
		groundCollisions = new GroundCollisions(world);
		mapObjectCollisions = new MapObjectCollisions(world);
		foodCollisions = new FoodCollisions(world);
	}
	
	public void update(float delta) {
		groundCollisions.update(delta);
		mapObjectCollisions.update(delta);
		foodCollisions.update(delta);
	}
}
