package com.swordbit.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.swordbit.game.model.World;
import com.swordbit.game.util.Assets;

public class LevelSelectScreen extends AbstractGameScreen {

	private Stage stage;
	private Table table;
	Skin skin = new Skin(Gdx.files.internal("data/textbuttons.json"));
	ScrollPane scroll;
	Table container;
	Texture backgroundImage;
	SpriteBatch spriteBatch;
	int CAMERA_HEIGHT;
	int CAMERA_WIDTH;

	public LevelSelectScreen(Game game, World world) {
		super(game, world);
		CAMERA_WIDTH = 480;
		CAMERA_HEIGHT = 320;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		table = new Table();
		container = new Table();
		container.setFillParent(true);
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		backgroundImage = Assets.instance.getAssetManager().get(
				"images/LandingPage.png", Texture.class);

		container.add(table);

		stage = new Stage();
		spriteBatch = stage.getSpriteBatch();
		Gdx.input.setInputProcessor(stage);
		table = new Table();

		scroll = new ScrollPane(table, skin);
		scroll.getStyle().background = new SpriteDrawable(new Sprite(
				backgroundImage));

		for (int i = 0; i < world.getLevelCount(); i++) {
			TextButton levelButton = new TextButton("Level " + (i + 1), skin);
			LevelListener levelListener = new LevelListener(i, levelButton);
			levelListener.createListener();
			table.add(levelListener.button).width(CAMERA_WIDTH / 3).pad(60);
			table.row();
		}

		container.add(scroll).width(CAMERA_WIDTH).height(CAMERA_HEIGHT);
		container.row();
		stage.addActor(container);
		scroll.layout();
	}


	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/**
	 * listener designed to create correct game screen depending on button
	 * clicked
	 **/
	public class LevelListener {

		int level;
		TextButton button;

		public LevelListener(int level, TextButton button) {
			this.level = level;
			this.button = button;
		}

		public void createListener() {
			button.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					game.setScreen(new GameScreen(game, new World(level)));
				};
			});
		}

	}
}
