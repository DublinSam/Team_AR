package colin.test.newapp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import colin.test.newapp.controller.WorldController;
import colin.test.newapp.model.Level;
import colin.test.newapp.model.World;
import colin.test.newapp.ui.Score;
import colin.test.newapp.util.Assets;
import colin.test.newapp.util.ProgressBar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**Game Screen, repsonsible for displaying all game events, and also catching relevant input commands**/
public class GameScreen implements Screen, InputProcessor, PropertyChangeListener {
	public enum GameStatus{
		INPROGRESS,GAMEOVER,LEVELCOMPLETED
	}
	
	boolean touchDown;
	private World world;
	private WorldRenderer renderer;
	private WorldController controller;
	private int width, height;
	private Game myGame;
	GameStatus gameStatus;

	PauseTable pauseTable;
	private final float CAMERA_WIDTH = Gdx.graphics.getWidth();
	private final float CAMERA_HEIGHT= Gdx.graphics.getHeight();
	boolean gamePaused=false;
	private TextureRegion hungerTextureRegion;
	Stage ui;
	private ProgressBar progressBar;
	private Texture hungerTexture;
	private OrthographicCamera cam;
	private TextButton beginButton;

	public GameScreen(Game game,World world){
		renderer=new WorldRenderer(world);
		controller = new WorldController(world);
		this.world = world;
		this.myGame=game;
		this.cam = new OrthographicCamera();
		this.cam.setToOrtho(false, CAMERA_WIDTH,CAMERA_HEIGHT);
		//this.cam.position.set(CAMERA_WIDTH,CAMERA_HEIGHT, 0);
		this.cam.update();
	}
	
	/**Draw Screen**/
	@Override
	public void render(float delta) {
		//Background color blue
		Gdx.gl.glClearColor(0.16f, 0.67f, 0.95f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(!(gamePaused)){
		controller.update(delta);
		}
		renderer.render();
		renderGui(ui.getSpriteBatch());
		if(gameStatus==GameStatus.GAMEOVER){
			myGame.setScreen(new GameOverScreen(this.myGame,world.getEater()));
		}
		else if(gameStatus==GameStatus.LEVELCOMPLETED){
			myGame.setScreen(new LevelCompletedScreen(this.myGame,world));
		}
		ui.act();
	}

	@Override
	public void resize(int width, int height) {
        this.height=height;
        this.width=width;
	}

	@Override
	public void show() {
		
		System.out.println("show screen");
		ui = new Stage();
		ui.setCamera(cam);
		hungerTexture = Assets.instance.getAssetManager().get("images/hunger.png",Texture.class);
		hungerTextureRegion = new TextureRegion(hungerTexture);
		progressBar=new ProgressBar(hungerTextureRegion, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()-10);
		
		world.getEater().addChangeListener(renderer);
		world.addChangeListener(this);
		pauseTable = new PauseTable();
        pauseTable.setPosition(Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2);
        createBeginButton();
        createPauseButton();
        createScore();
        createPauseTable();
        
	    //stage.addActor(table);
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(ui);
		multiplexer.addProcessor(this);
		
		
		Gdx.input.setInputProcessor(multiplexer);
		renderer.setSize(width, height);
	}

private void createBeginButton() {
	Skin skin = new Skin(Gdx.files.internal("data/textbuttons.json"));
	beginButton = new TextButton("Begin", skin);
	beginButton.addListener(new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			super.clicked(event, x, y);
			controller.beginTouched();
			beginButton.remove();
			
				}
		
});
	beginButton.setPosition(CAMERA_WIDTH/2, CAMERA_HEIGHT/2);
	ui.addActor(beginButton);
	}

public void createPauseTable(){
	Skin skin = new Skin(Gdx.files.internal("data/textbuttons.json"));
	TextButton resumeButton = new TextButton("Resume", skin);
	resumeButton.addListener(new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			super.clicked(event, x, y);
			resumeGame();
				}

		private void resumeGame() {
			gamePaused=false;
			pauseTable.remove();
			pauseTable.setVelocity();
			pauseTable.setPosition(Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2);
			
		}
		
});
	pauseTable.add(resumeButton).padBottom(10).width(CAMERA_WIDTH/3);
	pauseTable.row();
	TextButton restartButton = new TextButton("Restart", skin);
	restartButton.addListener(new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			super.clicked(event, x, y);
			myGame.getScreen().dispose();
			int levelIndex=Assets.instance.getLevelManager().getLevelIndex();
			myGame.setScreen(new GameScreen(myGame,new World(levelIndex)));
				}
		
});
	pauseTable.add(restartButton).pad(10).width(CAMERA_WIDTH/3);
	pauseTable.row();
	TextButton mainMenuButton = new TextButton("Main Menu", skin);
	mainMenuButton.addListener(new ClickListener(){
		@Override
		public void clicked(InputEvent event, float x, float y) {
			// TODO Auto-generated method stub
			super.clicked(event, x, y);
			myGame.getScreen().dispose();
			myGame.setScreen(new MainMenuScreen(myGame));
		}
	});
	pauseTable.add(mainMenuButton).width(CAMERA_WIDTH/3);
	
}
/**renders the GUI, pause screen, pause button and score**/
	private void renderGui (SpriteBatch batch) {
		ui.draw();
		ui.getSpriteBatch().begin();
		progressBar.SetEnd(100, world.getFoodCollected());
		progressBar.Draw(ui.getSpriteBatch());
		ui.getSpriteBatch().end();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
public void createPauseButton(){
	Skin skin =Assets.instance.getAssetManager().get("data/textbuttons.json", Skin.class);
	TextButton pb = new TextButton("Pause",skin);
	pb.setPosition(CAMERA_WIDTH-pb.getWidth(), CAMERA_HEIGHT-pb.getHeight());;
	pb.addListener(new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if(!gamePaused){
			pause();
			}
			else{
				resume();
				}
			super.clicked(event, x, y);
		}
		
});
	ui.addActor(pb);
}
public void createScore(){ 
	Score score = new Score(world.getEater(),75,25,5,Gdx.graphics.getHeight()-20);
	ui.addActor(score);
}
public void pauseGame() {
	       gamePaused=true;
	       ui.addActor(pauseTable);
	    }

	@Override
	public void pause() {
		pauseGame();
		
	}

	@Override
	public void resume() {

		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.LEFT)
			controller.leftPressed();
		if (keycode == Keys.RIGHT)
			controller.rightPressed();
		if(keycode==Keys.Z){
			controller.jumpPressed();
		}
		return true;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.LEFT)
			controller.leftReleased();
		if (keycode == Keys.RIGHT)
			controller.rightReleased();
		if(keycode==Keys.Z){
			controller.jumpReleased();
		}
		return true;
	}
	
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
    public boolean touchDown(int x, int y, int pointer, int button) {

		if (x > width / 2 && y > height / 2) {
			//controller.rightPressed();
		}
		if (x < width / 2 && y > height / 2) {
			controller.jumpPressed();
		}else{
			controller.jumpReleased();
			 }

		return true;
	}
 
    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (x < width / 2 && y > height / 2) {
            controller.jumpReleased();
        }
        if (x > width / 2 && y > height / 2) {
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
		System.out.println("Changed property: " + evt.getPropertyName() + " [old -> "
                + evt.getOldValue() + "] | [new -> " + evt.getNewValue() +"]");
				gameStatus=(GameStatus)evt.getNewValue();
		
	}
	public class PauseTable extends Table{
		float tableVelocity=10;
		float tableDamping=0.9f;
		@Override
		public void act(float delta) {
			// TODO Auto-generated method stub
			float xPos = this.getX();
			float yPos = this.getY();
			super.act(delta);
			if(yPos>Gdx.graphics.getHeight()/3&&gamePaused){
				tableVelocity*=tableDamping;
			}
			if(yPos<Gdx.graphics.getHeight()/2&&gamePaused){
				yPos=yPos+tableVelocity;
				this.setPosition(xPos, yPos);
			}
		}
		public void setVelocity(){
			tableVelocity=10;
		}
	}

}
