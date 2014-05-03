package com.swordbit.game.model;

import com.badlogic.gdx.math.Rectangle;

public class Tile extends Rectangle {
	private static final long serialVersionUID = 1L;
	private String collidable;

	public Tile() {
		super();
	}

	Tile(String collidable, Rectangle rect) {
		super(rect);
		this.collidable = collidable;
	}

	public String getCollidable() {
		return this.collidable;
	}
	
	public void setCollidable(String collidable) {
		this.collidable = collidable;
	}

	@Override
	public boolean overlaps(Rectangle r) {
		return  x < r.x + r.width 
				&& x + width > r.x 
				&& y <= r.y + r.height
				&& y + height >= r.y;
	}

	public boolean overlapsBoundary(Rectangle r) {
		return  x <= r.x + r.width 
				&& x + width >= r.x
				&& y <= r.y + r.height 
				&& y + height >= r.y;
	}
}
