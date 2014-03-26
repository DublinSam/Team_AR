package com.swordbit.game.view.renderers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.swordbit.game.model.Eater;
import com.swordbit.game.model.World;

// The MasterRenderer coordinates all other renderers. It implements the
// PropertyChangeListener interface to allow it to react to state changes
// in the Eater model.
 
public class MasterRenderer implements PropertyChangeListener{

	private World world;
	private Eater eater;	
	private SpriteBatch spriteBatch;	
	private float CAMERA_WIDTH = 10;
	private float CAMERA_HEIGHT = 7;
	private FoodRenderer foodRenderer;
	private EaterRenderer eaterRenderer;
	private BackgroundRenderer backgroundRenderer;
	private OrthogonalTiledMapRenderer mapRenderer;
	private OrthographicCamera scrollingCamera;
	private FPSLogger framesPerSecondlogger;
	
	public MasterRenderer(World world) {	
		init(world);
		configureScrollingCamera();
		configureSlaveRenderers();
		configureFrameRateLogger();
	}
		
	private void init(World world) {
		this.world = world;
		this.eater = this.world.getEater();
	}
	
	private void configureScrollingCamera() {
		this.scrollingCamera = new OrthographicCamera();
		this.scrollingCamera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.scrollingCamera.position.set(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, 0);
		this.scrollingCamera.update();	
	}

	private void configureSlaveRenderers() {
		constructSpriteBatch();
		constructSlaveRenderers();
	}
	
	private void constructSpriteBatch() {
		spriteBatch = new SpriteBatch();
	}
	
	private void constructSlaveRenderers() {		
		foodRenderer = new FoodRenderer(world);
		eaterRenderer = new EaterRenderer(world);
		backgroundRenderer = new BackgroundRenderer(scrollingCamera);
		mapRenderer = new OrthogonalTiledMapRenderer(world.getMap(), 1 / 48f);			
	}
	
	private void configureFrameRateLogger() {
		framesPerSecondlogger = new FPSLogger();
	}
	
	public void render() {
		renderBackground();
		renderForeground();
		logFrameRate();
	}
	
	private void renderBackground() {
		backgroundRenderer.render(spriteBatch);
	}
	
	private void renderForeground() {
		updateCameraPosition();
		drawLevelObjects();
		drawActors();
	}
	
	private void logFrameRate() {
		framesPerSecondlogger.log();
	}

	private void updateCameraPosition() {
		moveCameraToEaterPosition();
		if (eaterRising()) {
			moveCameraUp();
		} else if (eaterFalling()) {
			moveCameraDown();
		}
		updateNewCameraPosition();
	}
	
	private void moveCameraToEaterPosition() {
		scrollingCamera.position.x = eater.getPosition().x + CAMERA_WIDTH / 2 - eater.SIZE;
	}
	
	private boolean eaterRising() {
		boolean isRising = false;
		if (eater.getPosition().y > 3.5 && scrollingCamera.position.y < 5)
			isRising = true;
		return isRising;
	}
	
	private boolean eaterFalling() {
		boolean isFalling = false;
		if (eater.getPosition().y < 3.5 && scrollingCamera.position.y > 3.5)
			isFalling = true;
		return isFalling;
	}
	
	private void moveCameraUp() {
		scrollingCamera.position.y += 0.1;
	}
	
	private void moveCameraDown() {
		scrollingCamera.position.y -= 0.1;
	}
	
	private void updateNewCameraPosition() {
		scrollingCamera.update();
	}
	
	private void drawLevelObjects() {
		mapRenderer.setView(scrollingCamera);
		mapRenderer.render();
	}
	
	private void drawActors() {
		spriteBatch.setProjectionMatrix(scrollingCamera.combined);		
		spriteBatch.begin();
			foodRenderer.drawFood(spriteBatch);
			eaterRenderer.drawEater(spriteBatch);	
		spriteBatch.end();	
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		eaterRenderer.propertyChange(evt);	
	}
}