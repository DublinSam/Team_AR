package com.swordbit.game.view.renderers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.swordbit.game.model.Background;
import com.swordbit.game.utils.Assets;

public class BackgroundRenderer {
	
	private Camera camera;
	private float screenWidth;
	private float screenHeight;
	private TextureRegion cloudTexture;
	private TextureRegion mountainTexture;
	private TextureRegion hillTexture;
	private Background cloudLayer;
	private Background mountainLayer;
	private Background[] backgroundLayers;
	private Background hillLayer;
	
	public BackgroundRenderer(Camera camera) {
		configureCamera(camera);
		configureScreenDimensions(camera.viewportWidth, camera.viewportHeight);	
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
		hillLayer = buildHillLayer();
		addLayers(mountainLayer, cloudLayer, hillLayer);
	}
	
	private Background buildMountainLayer() {
		mountainTexture = Assets.instance.backgroundLayers.mountains;
		Vector2 mountainRatio = new Vector2(0.02f, 0);
		Vector2 startCoordinates = configureLayerPosition();
		Vector2 padding = new Vector2(0,0);
		mountainLayer =  new Background(mountainTexture, mountainRatio, startCoordinates, padding);
		return mountainLayer;
	}
	
	private Background buildHillLayer() {
		hillTexture = Assets.instance.backgroundLayers.hills;
		Vector2 hillRatio = new Vector2(0.08f, 0);
		Vector2 startCoordinates = configureLayerPosition();
		Vector2 padding = new Vector2(0,0);
		hillLayer =  new Background(hillTexture, hillRatio, startCoordinates, padding);
		return hillLayer;
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
	
	private void addLayers(Background mountainLayer, Background cloudLayer, Background hillLayer) {
		Background[] backgroundLayers = new Background[3];
		backgroundLayers[0] = mountainLayer;
		backgroundLayers[1] = cloudLayer;
		backgroundLayers[2] = hillLayer;
		this.backgroundLayers = backgroundLayers;	
	}

	public void render(SpriteBatch batch) {
		for (Background backgroundLayer : backgroundLayers) {
			renderLayer(batch, backgroundLayer);
		}
	}
	
	private void renderLayer(SpriteBatch batch, Background backgroundLayer) {
		batch.setProjectionMatrix(camera.projection);
		batch.begin();
		drawBackground(batch, backgroundLayer);
		batch.end();
	}
	
	private void drawBackground(SpriteBatch batch, Background backgroundLayer) {
		float currentX = calculateCurrentX(backgroundLayer);
		do {
			float currentY = calculateCurrentY(backgroundLayer);		
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