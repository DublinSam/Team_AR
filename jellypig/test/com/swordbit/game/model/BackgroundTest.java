package com.swordbit.game.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class BackgroundTest {

	@Test
	public void testBackgroundConstructor() {
		TextureRegion sampleRegion = new TextureRegion();
		Vector2 sampleStartPosition = new Vector2(0,0);
		Vector2 sampleParallaxRatio = new Vector2(1, 1.02f);
		
		Background sampleBackground = 
				new Background(sampleRegion, sampleParallaxRatio, sampleStartPosition);
		
		assertEquals("Matching texture regions", sampleRegion, sampleBackground.region);
		assertEquals("Matching start position", sampleStartPosition, sampleBackground.startPosition);
		assertEquals("Matching parallax ration", sampleParallaxRatio, sampleBackground.parallaxRatio);
	}

}
