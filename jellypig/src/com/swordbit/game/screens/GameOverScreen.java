package com.swordbit.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.swordbit.game.model.Eater;
import com.swordbit.game.model.World;
import com.swordbit.game.util.Assets;
import com.swordbit.game.util.PreferencesHelper;

public class GameOverScreen extends AbstractGameScreen {
	private Stage stage;
	private Eater eater;
	private Table table;
	private int levelIndex;
	private int CAMERA_WIDTH;
	private int CAMERA_HEIGHT;
	private OrthographicCamera cam;

	public GameOverScreen(Game game, World world) { 
		super(game, world);
		setupCamera();
		this.levelIndex = world.getCurrentLevelIndex();
		System.out.println(levelIndex);
		this.eater = world.getEater();
	}

	private void setupCamera() {
		CAMERA_WIDTH = Gdx.graphics.getWidth();
		CAMERA_HEIGHT = Gdx.graphics.getHeight();
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
		this.cam.update();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1f, 0f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public void show() {
		Stage stage = buildStage();
		Gdx.input.setInputProcessor(stage);
	}
	
	private Stage buildStage() {
		stage = new Stage();
		Table table = buildTable();
		stage.addActor(table);
		return stage;
	}
	
	private Table buildTable() {
		table = new Table();
		table.setFillParent(true);
		Skin skin = Assets.instance.getAssetManager().get(
				"data/textbuttons.json", Skin.class);	
		addScoreLabels(table, skin);
		TextButton restartButton = buildRestartButton(table, skin);
		TextButton mainMenuButton = buildMainMenuButton(table, skin);
		table.add(restartButton).pad(10).width(CAMERA_WIDTH / 3);
		table.row();
		table.add(mainMenuButton).width(CAMERA_WIDTH / 3);
		return table;
	}
	
	private Table addScoreLabels(Table table, Skin skin) {
		PreferencesHelper prefhelper = new PreferencesHelper();
		int highScore = prefhelper.getHighScore();
		Label highScoreLabel = new Label("Highscore " + highScore, skin);
		Label scoreLabel = new Label("Score " + eater.getScore(), skin);
		table.add(highScoreLabel).pad(10);
		table.row();
		table.add(scoreLabel).pad(10);
		table.row();
		return table;
	}
	
	private TextButton buildRestartButton(Table table, Skin skin) {
		TextButton restartButton = new TextButton("Restart", skin);
		restartButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.getScreen().dispose();
				game.setScreen(new GameScreen(game, new World(levelIndex)));
			}
		});
		return restartButton;
	}
	
	private TextButton buildMainMenuButton(Table table, Skin skin) {
		TextButton mainMenuButton = new TextButton("Main Menu", skin);
		mainMenuButton.addListener(new ClickListener() {	
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.getScreen().dispose();
				game.setScreen(new MainMenuScreen(game, world));
			}
		});
		return mainMenuButton;
	}
	
	@Override public void resize(int width, int height) {}
	@Override public void pause() {}


}
