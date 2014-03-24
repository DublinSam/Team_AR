package com.swordbit.game.view;

import java.util.List;
import java.util.Iterator;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.swordbit.game.util.Assets;
import com.swordbit.game.util.Benchmarker;
import com.swordbit.game.model.Eater;
import com.swordbit.game.model.World;
import com.swordbit.game.model.food.Food;
import com.swordbit.game.util.ProgressBar;
import com.swordbit.game.view.animations.ScoreAnimation;

public class WorldRenderer implements PropertyChangeListener {

	private World world;
	private Eater eater;
	private OrthographicCamera cam;
	private float CAMERA_WIDTH = 10;
	private float CAMERA_HEIGHT = 7;

	private static final int BLINK_FRAME_COLS = 5;
	private static final int BLINK_FRAME_ROWS = 1;
	private static final int EATING_FRAME_COLS = 3;
	private static final int EATING_FRAME_ROWS = 2;

	Texture blinkSheet;
	Texture idleTexture;
	Texture defaultEaterTexture;
	TextureRegion[] idleFrames;
	TextureRegion[] blinkFrames;
	TextureRegion currentFrame;
	TextureRegion cookieTexture;
	
	TextureRegion strawBerryTexture;
	Animation nextAnimation;
	Animation blinkAnimation;
	Animation eatingAnimation;
	Animation currentAnimation;
	

	SpriteBatch spriteBatch;
	
	FPSLogger fpslog;

	ProgressBar progressBar;
	/** for debug rendering **/
	ShapeRenderer debugRenderer = new ShapeRenderer();
	private Texture hungerTexture;
	TextureRegion tr;
	private TextureRegion hungerTextureRegion;
	private OrthogonalTiledMapRenderer renderer;
	AssetManager assetManager;
	private boolean itemCollected = false;
	private ScoreAnimation scoreAnimation;
	private Texture eatingSheet;
	private TextureRegion[] eatingFrames;
	private int TRANSFORMATION_FRAME_COLS = 3;
	private TextureRegion[] transformationFrames;
	private Texture transformationSheet;
	private int TRANSFORMATION_FRAME_ROWS = 1;
	private Animation transformationAnimation;
	private FoodRenderer foodRenderer;
	private BackgroundRenderer backgroundRenderer;
	
	// Purely for performance benchmarking
	private Benchmarker benchmarker;

	public WorldRenderer(World world) {
		assetManager = Assets.instance.getAssetManager();
		scoreAnimation = new ScoreAnimation();
		fpslog = new FPSLogger();
		init(world);
		renderer = new OrthogonalTiledMapRenderer(world.getMap(), 1 / 48f);
		loadResources();
		progressBar = new ProgressBar(hungerTextureRegion, 5, 3.5f);
		progressBar.SetTargetDimension(1, 4);
	}
	
	private void init(World world) {
		this.world = world;
		this.eater = this.world.getEater();
		this.cam = new OrthographicCamera();
		this.cam.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, 0);
		this.cam.update();	
		foodRenderer = new FoodRenderer(world);
		backgroundRenderer = new BackgroundRenderer(cam, CAMERA_WIDTH, CAMERA_HEIGHT);	
		benchmarker = new Benchmarker();
	}

	public void setSize(int width, int height) {}

	public void loadResources() {
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(cam.combined);
		idleTexture = assetManager.get("images/JellyPig48.png", Texture.class);
		idleTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		hungerTexture = assetManager.get("images/hunger.png", Texture.class);
		hungerTextureRegion = new TextureRegion(hungerTexture);
		loadAnimations();
	}
	
	private void loadAnimations() {
		constructBlinkAnimation();
		constructEatingAnimation();
		constructTransformationAnimation();
	}

	private void constructTransformationAnimation() {
		transformationSheet = assetManager.get("images/Explosion48.png",
				Texture.class);
		TextureRegion[][] tmp2 = TextureRegion.split(transformationSheet,
				transformationSheet.getWidth() / TRANSFORMATION_FRAME_COLS,
				transformationSheet.getHeight() / TRANSFORMATION_FRAME_ROWS);
		transformationFrames = new TextureRegion[TRANSFORMATION_FRAME_COLS
				* TRANSFORMATION_FRAME_ROWS];
		int index2 = 0;
		for (int i = 0; i < TRANSFORMATION_FRAME_ROWS; i++) {
			for (int j = 0; j < TRANSFORMATION_FRAME_COLS; j++) {
				transformationFrames[index2++] = tmp2[i][j];
			}
		}

		transformationAnimation = new Animation(0.1f, transformationFrames);
		transformationAnimation.setPlayMode(Animation.NORMAL);

	}

	private void constructEatingAnimation() {
		eatingSheet = assetManager.get("images/Eating.png", Texture.class); // #9
		TextureRegion[][] tmp2 = TextureRegion.split(eatingSheet,
				eatingSheet.getWidth() / EATING_FRAME_COLS,
				eatingSheet.getHeight() / EATING_FRAME_ROWS); // #10
		eatingFrames = new TextureRegion[EATING_FRAME_COLS * EATING_FRAME_ROWS];
		int index2 = 0;
		for (int i = 0; i < EATING_FRAME_ROWS; i++) {
			for (int j = 0; j < EATING_FRAME_COLS; j++) {
				eatingFrames[index2++] = tmp2[i][j];
			}
		}

		eatingAnimation = new Animation(0.1f, eatingFrames);
		eatingAnimation.setPlayMode(Animation.NORMAL);

	}

	private void constructBlinkAnimation() {
		blinkSheet = assetManager.get("images/JellyPigSprite.png",
				Texture.class); // #9
		TextureRegion[][] tmp = TextureRegion.split(blinkSheet,
				blinkSheet.getWidth() / BLINK_FRAME_COLS,
				blinkSheet.getHeight() / BLINK_FRAME_ROWS); // #10
		blinkFrames = new TextureRegion[BLINK_FRAME_COLS * BLINK_FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < BLINK_FRAME_ROWS; i++) {
			for (int j = 0; j < BLINK_FRAME_COLS; j++) {
				blinkFrames[index++] = tmp[i][j];
			}
		}

		blinkAnimation = new Animation(0.1f, blinkFrames);
		blinkAnimation.setPlayMode(Animation.LOOP_PINGPONG);

	}

	public void render() {
		benchmarker.startTimer(System.currentTimeMillis());
		
		backgroundRenderer.render(spriteBatch, Gdx.graphics.getDeltaTime());
		setCameraPosition();
		spriteBatch.setProjectionMatrix(cam.combined);

		drawDebug();

		fpslog.log();
		renderer.setView(cam);
		renderer.render();

		spriteBatch.begin();
			drawFood();
			drawEater();
			drawScoreAnimation();
		spriteBatch.end();
		
		benchmarker.endTimer(System.currentTimeMillis());
	}
	
	public void drawFood() {
		foodRenderer.drawFood(spriteBatch);
	}

	private void setCameraPosition() {
		cam.position.x = eater.getPosition().x + CAMERA_WIDTH / 2 - eater.SIZE;
		if (eater.getPosition().y > 3.5 && cam.position.y < 5) {
			cam.position.y += 0.1;
			;
		} else if (eater.getPosition().y < 3.5 && cam.position.y > 3.5) {

			cam.position.y -= 0.1;
		}
		cam.update();

	}

	private void drawScoreAnimation() {
		if (itemCollected) {
			Boolean result = scoreAnimation.draw();
			if (result) {
				System.out.println(itemCollected);
				itemCollected = false;
			}
		}
	}

	public void drawEater() {
		if (currentAnimation == null) 
			drawEaterWithoutAnimation();
		else 
			drawEaterWithAnimation();
	}
	
	private void drawEaterWithoutAnimation() {
			spriteBatch.draw(idleTexture, 
							eater.getPosition().x,
							eater.getPosition().y, 
							eater.getBounds().width,
							eater.getBounds().height);
	}
	
	private void drawEaterWithAnimation() {
		Rectangle eaterBounds = eater.getBounds();
		currentFrame = currentAnimation.getKeyFrame(
				eater.getTimeInState(),false);
		spriteBatch.draw(currentFrame, eater.getPosition().x,
				eater.getPosition().y - 0.1f, eaterBounds.width,
				eaterBounds.height);
		if (currentAnimation.isAnimationFinished(eater.getTimeInState())) {
			if (eater.getState() == "EATING") {
				eater.forceState("TRANSFORMING");
			} else if (eater.getState() == "TRANSFORMING") {

				String finalState = eater.getFinalState();
				System.out.println(finalState);
				eater.forceState(finalState);
				currentAnimation = null;
			} else {
				eater.forceState("IDLE");
				currentAnimation = null;
			}

		}
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// code listens for changes to eater, called in setState in eater class
		if (event.getPropertyName().equals("state")) {
			String state = (String) event.getNewValue();
			if (state == "BLINK")
				currentAnimation = blinkAnimation;	
			else if(state == "JUMPING")
				idleTexture = assetManager.get("images/JellyPig_Jump48.png",
						Texture.class);
			else if(state == "IDLE")
				idleTexture = assetManager.get("images/JellyPig48.png",
						Texture.class);
			else if(state == "TRANSFORMING")
				currentAnimation = transformationAnimation;
			else if(state == "FAT")
				idleTexture = assetManager.get("images/FatJellyPig-01.png",
						Texture.class);
			else if(state == "ACNE")
				idleTexture = assetManager.get("images/AcneJellyPig-01.png",
						Texture.class);
			else if(state == "HOT")
				idleTexture = assetManager.get(
						"images/EnchiladoJellyPig-01.png", Texture.class);
			else if(state == "SOUR")
				idleTexture = assetManager.get("images/LemonJellyPig-01.png",
						Texture.class);
			else if(state == "HAPPY")
				idleTexture = assetManager.get("images/HappyJellyPig-01.png",
						Texture.class);
			else if(state == "EATING")
				currentAnimation = eatingAnimation;
			else
				currentAnimation = eatingAnimation;
			}
		}
	
	public void drawDebug() {
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeType.Line);

		// render Eater
		Eater eater = world.getEater();

		Rectangle eaterBounds = eater.getBounds();
		debugRenderer.setColor(new Color(0, 1, 0, 1));
		debugRenderer.rect(eater.getPosition().x, eater.getPosition().y,
				eaterBounds.width, eaterBounds.height);
		List<Food> foodList = world.getFood();
		Rectangle foodBounds;
		if (!(foodList == null)) {
			Iterator<Food> it = foodList.iterator();
			while (it.hasNext()) {
				Food foodItem = it.next();
				foodBounds = foodItem.getBounds();
				debugRenderer.setColor(new Color(0, 1, 0, 1));
				debugRenderer.rect(foodItem.getBounds().x,
						foodItem.getBounds().y, foodBounds.width,
						foodBounds.height);
			}
		}
		debugRenderer.end();
	}
}