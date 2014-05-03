package com.swordbit.game.controller.collisions;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.swordbit.game.model.World;
import com.swordbit.game.model.eater.Eater;
import com.swordbit.game.model.food.Food;
import com.swordbit.game.utils.Constants;

public class FoodCollisions {
	private World world;
	private Eater eater;
	private List<Food> foodList;
	public float timeSinceFoodSpawn = 0;
	public float spawnInterval = Constants.FOOD_SPAWN_INTERVAL_TIME;

	public FoodCollisions(World world) {
		this.world = world;
		this.eater = world.getEater();
		this.foodList = world.getFood();
	}

	void update(float delta) {
		runFoodGenerator(delta);
		Iterator<Food> foodItemIterator = world.getFood().iterator();
		while (foodItemIterator.hasNext()) {
			Food currentFoodItem = foodItemIterator.next();
			checkEaterCollision(currentFoodItem);
			if (currentFoodItem.getExists() == false) {
				foodItemIterator.remove();
			}
		}	
	}
	
	private void runFoodGenerator(float delta) {
		if (timeSinceFoodSpawn > spawnInterval) {
			float xSpawnPos = generateRandomX() + eater.getPosition().x;
			boolean collisionDetected = checkFoodSpawnCollision(xSpawnPos);
			if (!collisionDetected) {
				spawnFoodInWorld(xSpawnPos);
			}
		} else {
			timeSinceFoodSpawn += delta;
		}
	}

	private void checkEaterCollision(Food food) {
		if (eater.isColliding(food)) {
			eater.setTypeOfFood(food);
			processFood(food);
			food.setExists(false);
		}
	}
	
	private void processFood(Food food) {
		eater.consumeFood(food);
		world.increaseFoodCollected();
	}
	
	private void spawnFoodInWorld(float xSpawnPos) {
		world.spawnFood(xSpawnPos, 3f);
		timeSinceFoodSpawn = 0;
	}
	
	private float generateRandomX() {
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
}
