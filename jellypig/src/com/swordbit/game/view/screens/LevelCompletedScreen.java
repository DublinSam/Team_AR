package com.swordbit.game.view.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.swordbit.game.model.Eater;
import com.swordbit.game.model.World;
import com.swordbit.game.utils.PreferencesHelper;

public class LevelCompletedScreen extends AbstractGameScreen {
	private static final float CAMERA_WIDTH = 10;
	private static final float CAMERA_HEIGHT = 7;
	private Game myGame;
	private Stage stage;
	private Eater eater;
	private Table table;
	private World world;
	private OrthographicCamera cam;

	public LevelCompletedScreen(Game game, World world) {
		super(game, world);
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
		this.cam.update();
		this.eater = this.world.getEater();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 1f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public void show() {
		PreferencesHelper phelp = new PreferencesHelper();
		int highScore = phelp.getHighScore();
		Skin skin = new Skin(Gdx.files.internal("data/textbuttons.json"));
		BitmapFont buttonFont = new BitmapFont();
		table = new Table();
		table.setFillParent(true);
		stage = new Stage();
		Label highScoreLabel = new Label("Highscore " + highScore, skin);
		Label scoreLabel = new Label("Score " + eater.getScore(), skin);
		table.add(highScoreLabel);
		table.row();
		table.add(scoreLabel).pad(10);
		table.row();
		if (world.nextLevelExists(world.getCurrentLevelIndex())) {

			TextButton nextLevelButton = new TextButton("Next Level", skin);
			nextLevelButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					myGame.getScreen().dispose();

					myGame.setScreen(new GameScreen(myGame, world));
				}

			});

			table.add(nextLevelButton).pad(10);
		}
		TextButton mainMenuButton = new TextButton("Main Menu", skin);
		mainMenuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				myGame.getScreen().dispose();
				myGame.setScreen(new MainMenuScreen(myGame, world));
			}
		});
		table.add(mainMenuButton);
		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}
}
