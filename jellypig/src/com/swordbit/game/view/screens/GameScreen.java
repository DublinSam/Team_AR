package com.swordbit.game.view.screens;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.swordbit.game.controller.WorldController;
import com.swordbit.game.model.World;
import com.swordbit.game.utils.Assets;
import com.swordbit.game.utils.Constants;
import com.swordbit.game.view.renderers.MasterRenderer;
import com.swordbit.game.view.renderers.FloatingScoreRenderer;
import com.swordbit.game.view.ui.Score;

/*
 * Game Screen is responsible for displaying all game events, and also catching
 * relevant input commands
 */

public class GameScreen extends AbstractGameScreen implements InputProcessor,
		PropertyChangeListener {
	private int width;
	private int height;
	private Skin skin;
	private Stage stage;
	private int currentLevel;
	private boolean gamePaused;
	private boolean itemCollected;
	private PauseTable pauseTable;
	private TextButton beginButton;
	private SpriteBatch spriteBatch;
	private WorldController controller;
	private MasterRenderer masterRenderer;
	private final float CAMERA_WIDTH = 800;
	private final float CAMERA_HEIGHT = 480;
	private FloatingScoreRenderer scoreAnimation;
	
	public enum GameStatus {
		INPROGRESS, GAMEOVER, LEVELCOMPLETED
	}

	public GameScreen(Game game, World world) {
		super(game, world);
		init(world);
	}
	
	private void init(World world) {
		masterRenderer = new MasterRenderer(world);
		controller = new WorldController(world);	
		spriteBatch = new SpriteBatch();
		gamePaused = false;
		currentLevel = world.getCurrentLevelIndex();
		buildOverlayUI();
		createScore();
	}
	
	private void buildOverlayUI() {
		stage = new Stage(new ExtendViewport(Constants.VIEWPORT_GUI_WIDTH, 
											 Constants.VIEWPORT_GUI_HEIGHT,800,600));
		
		skin = Assets.instance.getAssetManager().get("data/textbuttons.json",Skin.class);	
		createBeginButton();
		createPauseButton();
		createPauseTable();
	}

	@Override
	public void render(float delta) {
		glClearScreenToBlue();
		if (!(gamePaused)) {
			controller.update(delta);
		}
		masterRenderer.render();
		stage.act();
		renderGui();
	}
	
	private void glClearScreenToBlue() {
		Gdx.gl.glClearColor(0.16f, 0.67f, 0.95f, 1); // Background colour blue
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void resize(int width, int height) {
		this.height = height;
		this.width = width;
	
		stage.getViewport().update(width, height, true);
		
	}

	@Override
	public void show() {
		world.getEater().addChangeListener(masterRenderer);
		world.getEater().addChangeListener(this);
		world.addChangeListener(this);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);
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
		
		stage.addActor(beginButton);
		//beginButton.setSize(200, 200);
		beginButton.setPosition(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2);
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
	private void renderGui() {
		stage.draw();
		spriteBatch.begin();
		if (itemCollected) {
			boolean result = scoreAnimation.draw();
			if (result) {
				itemCollected = false;
			}
		}
		spriteBatch.end();
	}

	public void createPauseButton() {
		TextButton pb = new TextButton("Pause", skin);
		pb.setPosition(CAMERA_WIDTH - pb.getWidth(),
				CAMERA_HEIGHT - pb.getHeight());
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
		stage.addActor(pb);
	}

	public void createScore() {
		scoreAnimation = new FloatingScoreRenderer();
		Score score = new Score(world.getEater(), 75, 25, 5, CAMERA_HEIGHT - 20);
		stage.addActor(score);
	}

	@Override
	public void pause() {
		pauseGame();
	}
	
	public void pauseGame() {
		gamePaused = true;
		stage.addActor(pauseTable);
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
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
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
			scoreAnimation.init(spriteBatch, stage.getViewport().getCamera().position,
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