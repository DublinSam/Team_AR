package com.swordbit.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class GameOverScreen implements Screen {
	private Game myGame;
	Stage stage;
	Eater eater;
	Table table;
	PreferencesHelper phelp = new PreferencesHelper();
	private OrthographicCamera cam;
	private int CAMERA_WIDTH;
	private int CAMERA_HEIGHT;
	int levelIndex;

	public GameOverScreen(Game myGame, Eater eater, int levelIndex) {
		this.levelIndex = levelIndex;
		CAMERA_WIDTH = Gdx.graphics.getWidth();
		CAMERA_HEIGHT = Gdx.graphics.getHeight();
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
		this.cam.update();
		this.myGame = myGame;
		this.eater = eater;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1f, 0f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		int highScore = phelp.getHighScore();
		// Skin skin = new Skin(Gdx.files.internal("data/textbuttons.json"));
		Skin skin = Assets.instance.getAssetManager().get(
				"data/textbuttons.json", Skin.class);

		table = new Table();
		table.setFillParent(true);
		stage = new Stage();
		Label highScoreLabel = new Label("Highscore " + highScore, skin);
		Label scoreLabel = new Label("Score " + eater.getScore(), skin);
		table.add(highScoreLabel).pad(10);
		table.row();
		table.add(scoreLabel).pad(10);
		table.row();

		TextButton restartButton = new TextButton("Restart", skin);
		restartButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				myGame.getScreen().dispose();
				myGame.setScreen(new GameScreen(myGame, new World(levelIndex)));

			}

		});
		TextButton mainMenuButton = new TextButton("Main Menu", skin);
		mainMenuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				super.clicked(event, x, y);
				myGame.getScreen().dispose();
				myGame.setScreen(new MainMenuScreen(myGame));
			}
		});
		table.add(restartButton).pad(10).width(CAMERA_WIDTH / 3);
		table.row();
		table.add(mainMenuButton).width(CAMERA_WIDTH / 3);
		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

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

}
