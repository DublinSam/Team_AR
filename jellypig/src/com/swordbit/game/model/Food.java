package com.swordbit.game.model;

import java.util.Random;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class Food {
	public static final float SIZE = 0.5f; // half a unit
	public static final float SPEED = -2f; // unit per second
	float timeAlive;
	boolean released;
	FoodType foodType;
	float stateTime = 0;
	boolean exists = true;
	Vector2 position = new Vector2();
	Vector2 velocity = new Vector2();
	Rectangle bounds = new Rectangle();
	Vector2 acceleration = new Vector2();
	
	/** Food types with their respective height and widths **/
	public enum FoodType {
		STRAWBERRY(0.5f, 0.5f), COOKIE(0.5f, 0.5f), CHILLI(0.25f, 0.70f), APPLE(
				0.5f, 0.5f), BURGER(0.75f, 0.75f), HOTDOG(0.90f, 0.5f), PIZZA(
				0.75f, 0.75f), LEMON(0.5f, 0.5f);
		
		public final float width;
		public final float height;
		
		private FoodType(float width, float height) {
			this.width = width;
			this.height = height;
		}
	}

	public enum State {
		IDLE, MOVING
	}

	public Food() {
	}

	public Food(Vector2 position) {
		generateFoodType();
		this.timeAlive = 0;
		this.position = position;
		this.bounds.setPosition(this.position);
		this.bounds.height = this.foodType.height;
		this.bounds.width = this.foodType.width;
		this.velocity = new Vector2(0, SPEED);
	}

	public void generateFoodType() {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(8);
		this.foodType = FoodType.values()[randomInt];
		this.bounds.height = foodType.height;
		this.bounds.width = foodType.width;
		this.velocity.y = -2;

	}

	public Vector2 getAcceleration() {
		return this.acceleration;
	}

	public Rectangle getBounds() {
		return this.bounds;
	}

	public boolean getExists() {
		return exists;
	}

	public FoodType getFoodType() {
		return foodType;
	}

	public Vector2 getPosition() {
		return this.position;
	}

	public Vector2 getVelocity() {
		return this.velocity;
	}

	public boolean isReleased() {
		return released;
	}

	public void release() {
		this.released = true;
	}

	public void setExists(boolean isAlive) {
		exists = isAlive;
	}

	public void setFoodType(FoodType foodType) {
		this.foodType = foodType;
	}

	public void setPosition(float posX, float posY) {
		this.position.x = posX;
		this.position.y = posY;
	}

	public void update(float delta) {
		timeAlive += delta;
		if (timeAlive > 0) {
			this.release();
		}
		if (this.isReleased()) {
			position.add(velocity.cpy().scl(delta));
			this.bounds.setPosition(position);
		}
	}
}
