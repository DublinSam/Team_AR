package com.swordbit.game.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class ParallaxBackground {

	private ParallaxLayer[] layers;
	private Camera camera;
	private SpriteBatch batch;
	private Vector2 speed = new Vector2();
	private float width;
	private float height;

	/**
	 * @param layers
	 *            The background layers
	 * @param width
	 *            The screenWith
	 * @param height
	 *            The screenHeight
	 * @param speed
	 *            A Vector2 attribute to point out the x and y speed
	 */
	public ParallaxBackground(ParallaxLayer[] layers, SpriteBatch spriteBatch,
			Camera camera, float width, float height, Vector2 speed) {
		this.layers = layers;
		this.speed.set(speed);
		this.camera = camera;
		batch = spriteBatch;
		this.width = width;
		this.height = height;
	}

	public void render(float delta) {

		for (ParallaxLayer layer : layers) {
			batch.setProjectionMatrix(camera.projection);
			batch.begin();
			float currentX = -camera.position.x * layer.parallaxRatio.x
					% (width + layer.padding.x);

			do {
				float currentY = -camera.position.y * layer.parallaxRatio.y
						% (height + layer.padding.y);

				batch.draw(layer.region, -this.camera.viewportWidth / 2
						+ currentX + layer.startPosition.x,
						-this.camera.viewportHeight / 2 + currentY
								+ layer.startPosition.y, width, height);

				currentX += (width + layer.padding.x);
			} while (currentX < camera.viewportWidth);
			batch.end();
		}
	}
}