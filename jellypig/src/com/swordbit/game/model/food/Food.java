package com.swordbit.game.model.food;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class Food {
	protected static final float SIZE = 0.5f; // half a unit
	protected float width;
	protected float height;
	public int scoreValue;
	public String consequence;
	public TextureRegion foodTexture;
	float timeAlive;
	boolean exists = true;
	Vector2 position = new Vector2();
	Rectangle bounds = new Rectangle();

	
	public Food() {
		this.timeAlive = 0;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
		this.bounds.setPosition(position);
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	public TextureRegion getTexture() {
		return foodTexture;		
	}

	public String getConsequence() {
		return consequence;
	}

	public Vector2 getPosition() {
		return position;
	}

	public int getScoreValue() {
		return scoreValue;
	}
	
	public boolean getExists() {
		return exists;
	}

	public void setExists(boolean isAlive) {
		exists = isAlive;
	}
}
