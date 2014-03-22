package com.swordbit.game.model.food;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class Food {
	// Fixed properties for every food item
	protected static final float SIZE = 0.5f; // half a unit
	protected static final float SPEED = -2f; // unit per second
	protected Vector2 velocity = new Vector2();
	protected Vector2 acceleration = new Vector2();
	
	// Properties that are defined in the food subtype
	public String consequence;
	public int scoreValue;
	protected float width;
	protected float height;
	protected TextureRegion foodTexture;
	
	float timeAlive;
	boolean released;
	//float stateTime = 0;
	boolean exists = true;
	Vector2 position = new Vector2();
	Rectangle bounds = new Rectangle();

	public Food() {
		this.timeAlive = 0;
		this.velocity = new Vector2(0, SPEED);
	}

	public Food(Vector2 position) {
		this.timeAlive = 0;
		this.position = position;
		this.bounds.setPosition(this.position);
		this.velocity = new Vector2(0, SPEED);
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
	
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.draw(this.foodTexture, 
						this.getBounds().x,
						this.getBounds().y, 
						this.getBounds().width,
						this.getBounds().height);
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

	public TextureRegion getTexture() {
		return foodTexture;		
	}

	public String getConsequence() {
		return consequence;
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
}
