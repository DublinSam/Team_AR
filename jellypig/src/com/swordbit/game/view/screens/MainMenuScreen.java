package com.swordbit.game.view.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.swordbit.game.model.World;
import com.swordbit.game.utils.Constants;

public class MainMenuScreen extends AbstractGameScreen {
	int CAMERA_WIDTH;
	int CAMERA_HEIGHT;
	SpriteBatch spriteBatch;
	private Stage stage;
	private Skin menuSkin;
	private Button playButton;
	private Image imgBackground;
	
	// debug members
	private float debugRebuildStageTimer;
	private boolean debugEnabled = false;
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	
	public MainMenuScreen(Game game, World world) {
		super(game, world);	
	}
	
	@Override
	public void show() {
		initializeStage();
		rebuildStage();
	}
	
	private void initializeStage() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void render(float delta) {
		clearScreen();	
		if (debugEnabled) {
			debugRebuild(delta);
		}
		stage.act(delta);
		stage.draw();
		Table.drawDebug(stage);
	}
	
	private void debugRebuild(float deltaTime) {
		debugRebuildStageTimer -= deltaTime;
		if (debugRebuildStageTimer <= 0 ) { 
			debugRebuildStageTimer = DEBUG_REBUILD_INTERVAL;
			rebuildStage();
		}
	}
	
	private Table buildBackgroundLayer() {
		Table backgroundLayer = new Table();
		imgBackground = new Image(menuSkin, "main-menu-background");
		backgroundLayer.add(imgBackground);
		return backgroundLayer;
	}
	
	private Table buildControlsLayer() {
		Table controlsLayer = new Table();
		addPlayButtonToLayer(controlsLayer);
		if (debugEnabled) controlsLayer.debug();
		return controlsLayer;
	}
	
	private Table addPlayButtonToLayer(Table layer) {
		playButton = new Button(menuSkin, "play");
		layer.add(playButton).width(290.0f).height(150.3f);
		playButton.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				onPlayButtonClicked();
			}
		});
		return layer;
	}
	
	private void onPlayButtonClicked () {
		game.setScreen(new GameScreen(game, new World(4)));
	}
	
	void rebuildStage() {
		menuSkin = new Skin(Gdx.files.internal(Constants.SKIN_JELLYPIG_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_JELLYPIG_UI));
		
		Table layerBackground = buildBackgroundLayer();
		Table layerControls = buildControlsLayer();
		
		// assemble stage for menu screen
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, 
					  Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerBackground);
		stack.add(layerControls);
	}
	
	private void clearScreen() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void resize(int width, int height) {
		stage.setViewport(Constants.VIEWPORT_GUI_WIDTH, 
				Constants.VIEWPORT_GUI_HEIGHT, true);
	}
	
	@Override
	public void hide () {
		stage.dispose();
		menuSkin.dispose();
	}
	
	@Override public void pause() {}
	@Override public void resume() {}
	@Override public void dispose() {}
}
