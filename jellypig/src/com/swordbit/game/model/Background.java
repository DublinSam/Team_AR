package com.swordbit.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background {
	public TextureRegion region;
	public Vector2 parallaxRatio; //relative speed of x,y
	public Vector2 startPosition;
	public Vector2 padding;
	
	public Background(TextureRegion region, Vector2 parallaxRatio,
			Vector2 startPosition, Vector2 padding) {
		this.region = region;
		this.parallaxRatio = parallaxRatio;
		this.startPosition = startPosition;
		this.padding = padding;
	}
}