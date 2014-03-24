package com.swordbit.game.view.screens;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.swordbit.game.controller.WorldController;
import com.swordbit.game.model.World;
import com.swordbit.game.util.Assets;
import com.swordbit.game.util.ProgressBar;
import com.swordbit.game.view.WorldRenderer;
import com.swordbit.game.view.animations.ScoreAnimation;
import com.swordbit.game.view.ui.Score;

/*
 * Game Screen is responsible for displaying all game events, and also catching
 * relevant input commands
 */

public class GameScreen extends AbstractGameScreen implements InputProcessor,
		PropertyChangeListener {
	Stage ui;
	Skin skin;
	PauseTable pauseTable;
	boolean touchDown;
	boolean gamePaused = false;
	private int width, height;
	private TextureRegion hungerTextureRegion;
	private int currentLevel;
	private ProgressBar progressBar;
	private Texture hungerTexture;
	private OrthographicCamera cam;
	private TextButton beginButton;
	private boolean itemCollected;
	private WorldRenderer renderer;
	private WorldController controller;
	private ScoreAnimation scoreAnimation;
	private final float CAMERA_WIDTH = 480;
	private final float CAMERA_HEIGHT = 320;
	public enum GameStatus {
		INPROGRESS, GAMEOVER, LEVELCOMPLETED
	}

	public GameScreen(Game game, World world) {
		super(game, world);
		renderer = new WorldRenderer(world);
		controller = new WorldController(world);	
		currentLevel = world.getCurrentLevelIndex();
		buildUI();
		createScore();
	}
	
	private void buildUI() {
		ui = new Stage();
		setUpCamera();
		ui.setCamera(cam);
		skin = Assets.instance.getAssetManager().get("data/textbuttons.json",
				Skin.class);	
		createBeginButton();
		createPauseButton();
		createPauseTable();
	}
	
	private void setUpCamera() {
		this.cam = new OrthographicCamera(); 
		this.cam.setToOrtho(false);//, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.update();	
	}

	@Override
	public void render(float delta) {
		// Background color blue
		Gdx.gl.glClearColor(0.16f, 0.67f, 0.95f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		if (!(gamePaused)) {
			controller.update(delta);
		}
		renderer.render();
		renderGui(ui.getSpriteBatch());
		ui.act();
	}

	@Override
	public void resize(int width, int height) {
		this.height = height;
		this.width = width;
	}

	@Override
	public void show() {
		hungerTexture = Assets.instance.getAssetManager().get(
				"images/hunger.png", Texture.class);
		hungerTextureRegion = new TextureRegion(hungerTexture);
		progressBar = new ProgressBar(hungerTextureRegion, CAMERA_WIDTH / 2,
				CAMERA_HEIGHT - 10);

		world.getEater().addChangeListener(renderer);
		world.getEater().addChangeListener(this);
		world.addChangeListener(this);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(ui);
		multiplexer.addProcessor(this);

		Gdx.input.setInputProcessor(multiplexer);
		renderer.setSize(width, height);
	}

	private void createBeginButton() {

		beginButton = new TextButton("Begin", skin);
		beginButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				controller.beginTouched();
				beginButton.remove();

			}

		});
		beginButton.setPosition(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2);
		ui.addActor(beginButton);
	}

	public void createPauseTable() {
		
		pauseTable = new PauseTable();
		pauseTable.setPosition(CAMERA_WIDTH / 2, -CAMERA_HEIGHT / 2);
		TextButton resumeButton = new TextButton("Resume", skin);
		resumeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				resumeGame();
			}

			private void resumeGame() {
				gamePaused = false;
				pauseTable.remove();
				pauseTable.setVelocity();
				pauseTable.setPosition(CAMERA_WIDTH / 2, -CAMERA_HEIGHT / 2);

			}

		});
		pauseTable.add(resumeButton).padBottom(10).width(CAMERA_WIDTH / 3);
		pauseTable.row();
		TextButton restartButton = new TextButton("Restart", skin);
		restartButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.getScreen().dispose();
				game.setScreen(new GameScreen(game, new World(currentLevel)));
			}

		});
		pauseTable.add(restartButton).pad(10).width(CAMERA_WIDTH / 3);
		pauseTable.row();
		TextButton mainMenuButton = new TextButton("Main Menu", skin);
		mainMenuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				super.clicked(event, x, y);
				game.getScreen().dispose();
				game.setScreen(new MainMenuScreen(game, world));
			}
		});
		pauseTable.add(mainMenuButton).width(CAMERA_WIDTH / 3);

	}

	/** renders the GUI, pause screen, pause button and score **/
	private void renderGui(SpriteBatch batch) {
		ui.draw();
		ui.getSpriteBatch().begin();
		if (itemCollected) {
			boolean result = scoreAnimation.draw();
			if (result) {
				itemCollected = false;
			}
		}
		progressBar.SetEnd(100, world.getEater().getFullness());
		progressBar.Draw(ui.getSpriteBatch());
		ui.getSpriteBatch().end();
	}

	public void createPauseButton() {

		TextButton pb = new TextButton("Pause", skin);
		pb.setPosition(CAMERA_WIDTH - pb.getWidth(),
				CAMERA_HEIGHT - pb.getHeight());
		;
		pb.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!gamePaused) {
					pause();
				} else {
					resume();
				}
				super.clicked(event, x, y);
			}

		});
		ui.addActor(pb);
	}

	public void createScore() {
		scoreAnimation = new ScoreAnimation();
		Score score = new Score(world.getEater(), 75, 25, 5, CAMERA_HEIGHT - 20);
		ui.addActor(score);
	}

	public void pauseGame() {
		gamePaused = true;
		ui.addActor(pauseTable);
	}

	@Override
	public void pause() {
		pauseGame();
	}

	@Override
	public boolean keyDown(int keycode) {

		if (keycode == Keys.Z) {
			controller.jumpPressed();
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {

		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {

		if (x < width / 2 && y > height / 2) {
			controller.jumpPressed();

		}

		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (x < width / 2 && y > height / 2) {
			controller.jumpReleased();

		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// listens to world controller, waiting for a game over to change screen
		if (evt.getPropertyName().equals("score")) {
			itemCollected = true;
			int newScore = (Integer) evt.getNewValue();
			int oldScore = (Integer) evt.getOldValue();
			int difference = newScore - oldScore;
			scoreAnimation.init(ui.getSpriteBatch(), ui.getCamera().position,
					String.valueOf(difference));
		} else if (evt.getPropertyName().equals("world")) {
			GameStatus gameStatus = (GameStatus) evt.getNewValue();

			if (gameStatus == GameStatus.GAMEOVER) {
				game.getScreen().dispose();
				game.setScreen(new GameOverScreen(this.game, world));
			} else if (gameStatus == GameStatus.LEVELCOMPLETED) {
				game.getScreen().dispose();
				game.setScreen(new LevelCompletedScreen(this.game, world));
			}
		}
	}

	public class PauseTable extends Table {
		float tableVelocity = 10;
		float tableDamping = 0.9f;

		@Override
		public void act(float delta) {
			// TODO Auto-generated method stub
			float xPos = this.getX();
			float yPos = this.getY();
			super.act(delta);
			if (yPos > CAMERA_HEIGHT / 3 && gamePaused) {
				tableVelocity *= tableDamping;
			}
			if (yPos < CAMERA_HEIGHT / 2 && gamePaused) {
				yPos = yPos + tableVelocity;
				this.setPosition(xPos, yPos);
			}
		}

		public void setVelocity() {
			tableVelocity = 10;
		}
	}

	@Override
	public void dispose() {
		Assets.instance.getAssetManager().unload(
				"maps/level" + currentLevel + ".tmx");
	}

}
