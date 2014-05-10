package com.swordbit.game.view.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.swordbit.game.model.GameMusic;
import com.swordbit.game.model.SoundEffects;
import com.swordbit.game.model.World;
import com.swordbit.game.utils.Assets;
import com.swordbit.game.utils.Constants;

public class MainMenuScreen extends AbstractGameScreen {
	SpriteBatch spriteBatch;
	private Stage stage;
	private Skin menuSkin;
	private Button playButton;
	private Image imgBackground;
	
	// debug members
	private float debugRebuildStageTimer;
	private boolean debugEnabled = false;
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private Button introButton;
	private Skin buttonSkin;
	
	public MainMenuScreen(Game game, World world) {
		super(game, world);	
	}
	
	@Override
	public void show() {
		initializeStage();
		rebuildStage();
	}
	
	private void initializeStage() {
		stage = new Stage(new ExtendViewport(Constants.VIEWPORT_GUI_WIDTH, 
											Constants.VIEWPORT_GUI_HEIGHT,
											800.0f, 600.0f ));
		Gdx.input.setInputProcessor(stage);
	}
	
	private void rebuildStage() {
		menuSkin = new Skin(Gdx.files.internal(Constants.SKIN_JELLYPIG_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_JELLYPIG_UI));
		 buttonSkin = new Skin(Gdx.files.internal("data/buttons.json"));
		
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
		addintroButtonToLayer(controlsLayer);
		if (debugEnabled) controlsLayer.debug();
		return controlsLayer;
	}
	


	private Table addPlayButtonToLayer(Table layer) {
		playButton = new TextButton("Play", buttonSkin);
		layer.add(playButton).width(290.0f).height(150.3f).row();
		playButton.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				onPlayButtonClicked();
			}
		});
		return layer;
	}
	
	private Table addintroButtonToLayer(Table layer) {
		introButton=new TextButton("Intro",buttonSkin);
		layer.add(introButton).width(290.0f).height(150.3f);
		introButton.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				onIntroButtonClicked();
			}
		});
		return layer;
		
	}
	
	private void onPlayButtonClicked () {
		SoundEffects.instance.play(Assets.instance.sounds.play);
		game.setScreen(new GameScreen(game, new World(0)));
	}
	
	private void onIntroButtonClicked () {
		SoundEffects.instance.play(Assets.instance.sounds.play);
		game.setScreen(new CutsceneScreen(game));
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
	
	private void clearScreen() {
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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