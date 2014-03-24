package com.swordbit.game.view;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.swordbit.game.model.Background;
import com.swordbit.game.util.Assets;

public class BackgroundRenderer {
	
	private Camera camera;
	private float screenWidth;
	private float screenHeight;
	private TextureRegion mountainTexture;
	private TextureRegion cloudTexture;
	private Background mountainLayer;
	private Background cloudLayer;
	private Background[] backgroundLayers;
	private float currentX;
	private float currentY;
	
	public BackgroundRenderer(Camera camera, float screenWidth, float screenHeight) {
		configureCamera(camera);
		configureScreenDimensions(screenWidth, screenHeight);	
		configureBackgroundLayers();
	}

	private void configureCamera(Camera camera) {
		this.camera = camera;
	}

	private void configureScreenDimensions(float screenWidth, float screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
	
	private void configureBackgroundLayers() {	
		mountainLayer = buildMountainLayer();
		cloudLayer = buildCloudLayer();
		addLayers(mountainLayer, cloudLayer);
	}
	
	private Background buildMountainLayer() {
		mountainTexture = Assets.instance.backgroundLayers.mountains;
		Vector2 mountainRatio = new Vector2(0.02f, 0);
		Vector2 startCoordinates = configureLayerPosition();
		Vector2 padding = new Vector2(0,0);
		mountainLayer =  new Background(mountainTexture, mountainRatio, startCoordinates, padding);
		return mountainLayer;
	}
	
	private Background buildCloudLayer() {
		cloudTexture = Assets.instance.backgroundLayers.clouds;
		Vector2 cloudRatio = new Vector2(0.00f, 0);
		Vector2 startCoordinates = configureLayerPosition();
		Vector2 padding = new Vector2(0,0);
		Background cloudLayer = new Background(cloudTexture, cloudRatio, startCoordinates, padding);
		return cloudLayer;
	}
	
	private Vector2 configureLayerPosition() {	 
		return new Vector2(-this.camera.viewportWidth/2, -this.camera.viewportHeight / 2);	 
	}
	
	private void addLayers(Background mountainLayer, Background cloudLayer) {
		Background[] backgroundLayers = new Background[2];
		backgroundLayers[0] = mountainLayer;
		backgroundLayers[1] = cloudLayer;
		this.backgroundLayers = backgroundLayers;	
	}

	public void render(SpriteBatch batch, float delta) {
		for (Background backgroundLayer : backgroundLayers) {
			renderLayer(batch, delta, backgroundLayer);
		}
	}
	
	private void renderLayer(SpriteBatch batch, float delta, Background backgroundLayer) {
		batch.setProjectionMatrix(camera.projection);
		batch.begin();
		drawBackground(batch, backgroundLayer);
		batch.end();
	}
	
	private void drawBackground(SpriteBatch batch, Background backgroundLayer) {
		currentX = calculateCurrentX(backgroundLayer);
		do {
			currentY = calculateCurrentY(backgroundLayer);		
			batch.draw(backgroundLayer.region, 
					   backgroundLayer.startPosition.x + currentX,
					   backgroundLayer.startPosition.y + currentY,
					   screenWidth, screenHeight);
			currentX += (screenWidth + backgroundLayer.padding.x);
			
		} while (currentX < camera.viewportWidth);
	}
	
	private float calculateCurrentX (Background backgroundLayer) {
		return -camera.position.x * 
				backgroundLayer.parallaxRatio.x
				% (screenWidth + backgroundLayer.padding.x);	
	}
	
	private float calculateCurrentY(Background backgroundLayer) {
		return -camera.position.y * 
				backgroundLayer.parallaxRatio.y
				% (screenHeight + backgroundLayer.padding.y);
	}
}