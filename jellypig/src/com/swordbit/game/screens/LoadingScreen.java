package com.swordbit.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.swordbit.game.util.Assets;
import com.swordbit.game.util.Constants;

public class LoadingScreen extends AbstractGameScreen {
	private Stage stage;
	private float progress;	
	private Skin loadingSkin;
	private Image headingText;
	private Image firstLoadingBall;
	private Image secondLoadingBall;
	private Image thirdLoadingBall;

	public LoadingScreen(Game game) {
		super(game);
		loadResources();
	}
	
	@Override
	public void show() {
		initializeStage();
		rebuildStage();
	}
	
	private void initializeStage() {
		stage = new Stage();
	}
	
	@Override
	public void render(float delta) {
		clearScreen();
		rebuildStage();
		moveAndDrawStageActors(delta);
		switchToMainMenu();
	}
	
	private void moveAndDrawStageActors(float delta) {
		stage.act(delta);
		stage.draw();
	}
	
	void rebuildStage() {
		stage.clear();
		assembleStage();
	}
	
	private void assembleStage() {	
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, 
					  Constants.VIEWPORT_GUI_HEIGHT);
		addStageLayers(stack);
	}
	
	private void addStageLayers(Stack stack) {
		Table layerHeadingText = buildHeadingTextLayer();
		Table layerLoadingBalls = buildLoadingBallsLayer();
		stack.add(layerHeadingText);
		stack.add(layerLoadingBalls);
	}
	
	private Table buildHeadingTextLayer() {
		Table headingTextLayer = new Table();
		headingTextLayer.add(headingText);
		return headingTextLayer;
	}
	
	private Table buildLoadingBallsLayer() {
		Table loadingBallsLayer = new Table();
		loadingBallsLayer.row().padTop(130);
		addBallsToLayer(loadingBallsLayer);
		return loadingBallsLayer;
	}
	
	private Table addBallsToLayer(Table layer) {
		progress = Assets.instance.getAssetManager().getProgress();
		if (progress < 0.2) {
			// Don't draw any loading balls 
		} else if (progress < 0.4) {
			drawOneBall(layer);
		} else if (progress < 0.6) {
			drawTwoBalls(layer);
		} else {
				drawThreeBalls(layer);
		}
		return layer;
	}
	
	private Table drawOneBall(Table layer) {
		layer.add(firstLoadingBall);
		return layer;
	}
	
	private Table drawTwoBalls(Table layer) {
		layer.add(firstLoadingBall).padRight(50);
		layer.add(secondLoadingBall);
		return layer;
	}
	
	private Table drawThreeBalls(Table layer) {
		layer.add(firstLoadingBall).padRight(50);
		layer.add(secondLoadingBall).padRight(50);
		layer.add(thirdLoadingBall);
		return layer;
	}
	
	private void switchToMainMenu() {
		Assets.instance.getAssetManager().getLoadedAssets();
		if (Assets.instance.getAssetManager().update()) {
			game.getScreen().dispose();
			game.setScreen(new MainMenuScreen(this.game, world));
		}
	}
	
	private void loadResources() {
		Texture.setEnforcePotImages(false);
		loadingSkin = new Skin(Gdx.files.internal(Constants.SKIN_LOADING_SCREEN),
				new TextureAtlas(Constants.TEXTURE_ATLAS_LOADING_SCREEN));
		firstLoadingBall = new Image(loadingSkin, "ball");
		secondLoadingBall = new Image(loadingSkin, "ball");
		thirdLoadingBall = new Image(loadingSkin, "ball");
		headingText = new Image(loadingSkin, "loading");
		Assets.instance.init(new AssetManager());
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(Constants.VIEWPORT_GUI_WIDTH, 
				Constants.VIEWPORT_GUI_HEIGHT, true);
	}
	
	private void clearScreen() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	@Override public void pause() {}
	@Override public void resume() {}
	@Override public void dispose() {}
}