package com.swordbit.game.view.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.swordbit.game.model.World;
import com.swordbit.game.scenes.Cutscene;
import com.swordbit.game.scenes.Scene1;
import com.swordbit.game.scenes.Scene2;
import com.swordbit.game.scenes.Scene5;
import com.swordbit.game.utils.Assets;
import com.swordbit.game.utils.Constants;

public class CutsceneScreen extends AbstractGameScreen {
	
	SpriteBatch spriteBatch;
	private Stage stage;
	private Stage uiStage;
	private Cutscene currentScene;
	private Button skipButton;
	private Skin menuSkin;
	public CutsceneScreen(Game game) {
		super(game);
	}

	@Override
	public void render(float delta) {
		clearScreen();	
		stage.draw();
		uiStage.draw();
		if(currentScene.pan()){
			if(!currentScene.equals(currentScene.nextScene())){
			currentScene=currentScene.nextScene();
			}
			else{ game.setScreen(new MainMenuScreen(game,world));}
		}
		

		
	}
	private void clearScreen() {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	private void initializeStage() {
		stage = new Stage(new ExtendViewport(Constants.VIEWPORT_GUI_WIDTH, 
				Constants.VIEWPORT_GUI_HEIGHT,
				800.0f, 600.0f ));
		uiStage = new Stage(new ExtendViewport(Constants.VIEWPORT_GUI_WIDTH, 
				Constants.VIEWPORT_GUI_HEIGHT,
				800.0f, 600.0f ));
		Gdx.input.setInputProcessor(uiStage);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		uiStage.getViewport().update(width, height, true);
		
	}

	@Override
	public void show() {
		initializeStage();
		rebuildStage();
	
	}

	private void rebuildStage() {
		currentScene=new Scene1(stage);
		menuSkin = new Skin(Gdx.files.internal(Constants.SKIN_JELLYPIG_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_JELLYPIG_UI));
		Table layerControls=buildControlsLayer();
		Stack stack = new Stack();
		uiStage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, 
				  Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerControls);
		

		
	}
	private Table buildControlsLayer() {
		Table controlsLayer = new Table();
		addPlayButtonToLayer(controlsLayer);
		return controlsLayer;
	}
	private Table addPlayButtonToLayer(Table layer) {
		skipButton = new Button(menuSkin, "play");

		Skin skin = new Skin(Gdx.files.internal("data/buttons.json"));
		skipButton=new TextButton("Skip",skin);
		layer.add(skipButton).width(290.0f).height(150.3f);
		skipButton.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				onSkipButtonClicked();
			}

			private void onSkipButtonClicked() {
				game.setScreen(new MainMenuScreen(game,world));
				
			}
		});
		layer.bottom().left();
		return layer;
	}
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}


}
