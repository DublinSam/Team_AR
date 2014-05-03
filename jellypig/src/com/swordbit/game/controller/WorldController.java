	package com.swordbit.game.controller;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.swordbit.game.controller.collisions.CollisionDetector;
import com.swordbit.game.model.eater.Eater;
import com.swordbit.game.model.World;
import com.swordbit.game.utils.PreferencesHelper;
import com.swordbit.game.view.screens.GameScreen.GameStatus;

public class WorldController {
	public enum Input { PRESS_JUMP, PRESS_FART }
	private World world;
	private Eater eater;
	private int CAMERA_WIDTH = 10;
	private int CAMERA_HEIGHT = 7;
	private OrthographicCamera cam;	
	private boolean gameStarted = false;
	private CollisionDetector collisionDetector;
	private PreferencesHelper prefs = new PreferencesHelper();
	
	public WorldController(World world) {
		init(world);
		setUpCamera();
	}
	
	private void init(World world) {
		this.world = world;
		this.eater = world.getEater();	
		collisionDetector = new CollisionDetector(world);
	}

	private void setUpCamera() {
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
		this.cam.update();
	}
	
	public void update(float delta) {
		if (gameStarted) {
			eater.update(delta);
			collisionDetector.update(delta);
			checkEaterFallenToDeath();	
		}
	}
	
	private void checkEaterFallenToDeath() {
		if (eater.getPosition().y < 0) {
			world.levelFailed();
		}
	}
	
	public void beginGame() {
		gameStarted = true;
		eater.getVelocity().x = eater.SPEED;
	}
	
	public void jumpPressed() {
		eater.handleInput(Input.PRESS_JUMP);
	}
	
	public void fartPressed(){
		eater.handleInput(Input.PRESS_FART);
	}

	public GameStatus checkGameStatus() {
		GameStatus gameStatus = GameStatus.INPROGRESS;
		if (world.isLevelCompleted()) {
			checkIfNewHighScore();
			gameStatus = GameStatus.LEVELCOMPLETED;
		}
		return gameStatus;
	}

	public void checkIfNewHighScore() {
		int currentHighScore = prefs.getHighScore();
		if (eater.getScore() > currentHighScore) {
			prefs.saveHighScore(eater.getScore());
			prefs.save();
		}
	}
}