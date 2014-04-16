package com.swordbit.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background {
	public TextureRegion region;
	public Vector2 parallaxRatio; //relative speed of x,y
	public Vector2 startPosition;
	
	public Background(TextureRegion region, Vector2 parallaxRatio, Vector2 startPosition) {
		this.region = region;
		this.parallaxRatio = parallaxRatio;
		this.startPosition = startPosition;
	}
}