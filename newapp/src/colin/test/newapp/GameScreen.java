package colin.test.newapp;

import colin.test.newapp.controller.WorldController;
import colin.test.newapp.model.World;
import colin.test.newapp.ui.PauseButton;
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
public class GameScreen implements Screen, InputProcessor {
	public enum GameStatus{
		INPROGRESS,GAMEOVER,LEVELCOMPLETED
	}
	private World world;
	private WorldRenderer renderer;
	private WorldController controller;
	private int width, height;
	private Game myGame;
	GameStatus gameStatus;
	Table pauseTable;
	private final float CAMERA_WIDTH = 10f;
	private final float CAMERA_HEIGHT= 7f;
	boolean gamePaused=false;
	private TextureRegion hungerTextureRegion;
	Stage ui;
	private ProgressBar progressBar;
	private Texture hungerTexture;

	
	
	public GameScreen(Game game){
		world = new World();
		this.myGame=game;
	}
	
	/**Draw Screen**/
	@Override
	public void render(float delta) {
		//Background color blue
		Gdx.gl.glClearColor(0f, 0f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//Controller updates objects positions, render draws them to screen, positions should not change if paused
		if(!(gamePaused)){
		gameStatus=controller.update(delta);
		}
		renderer.render();
		renderGui(ui.getSpriteBatch());
		if(gameStatus==GameStatus.GAMEOVER){
			myGame.setScreen(new GameOverScreen(this.myGame,world.getEater()));
		}
		else if(gameStatus==GameStatus.LEVELCOMPLETED){
			myGame.setScreen(new LevelCompletedScreen(this.myGame,world.getEater()));
		}
		
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
		world = new World();
		hungerTexture = Assets.instance.getAssetManager().get("images/hunger.png",Texture.class);
		hungerTextureRegion = new TextureRegion(hungerTexture);
		progressBar=new ProgressBar(hungerTextureRegion, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()-10);
		renderer=new WorldRenderer(world);
		controller = new WorldController(world);
		world.getEater().addChangeListener(renderer);
		pauseTable = new Table();
		//sets pause table to fill screen, will want to change this in future
        pauseTable.setFillParent(true);
        //stage.addActor(table);
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

public void createPauseTable(){
	BitmapFont buttonFont = new BitmapFont();
	Texture grey = new Texture(Gdx.files.internal("images/newgreybutton.png"));
	Texture black = new Texture(Gdx.files.internal("images/newblackbutton.png"));
	TextureRegion greyRegion = new TextureRegion(grey);
	TextureRegion blackRegion = new TextureRegion(black);
	TextureRegion upRegion =greyRegion;
	TextureRegion downRegion =blackRegion;
	
	TextButtonStyle style = new TextButtonStyle();
	style.up = new TextureRegionDrawable(upRegion);
	style.down = new TextureRegionDrawable(downRegion);
	style.font = buttonFont;
	TextButton button1 = new TextButton("Resume", style);
	button1.addListener(new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			super.clicked(event, x, y);
			resume();
				}
		
});
	pauseTable.add(button1).padBottom(10);
	pauseTable.row();
	TextButton button2 = new TextButton("Restart", style);
	button2.addListener(new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			super.clicked(event, x, y);
			myGame.getScreen().dispose();
			myGame.setScreen(new GameScreen(myGame));
				}
		
});
	pauseTable.add(button2);
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
	PauseButton pb = new PauseButton(50,20,Gdx.graphics.getWidth()-50,Gdx.graphics.getHeight()-20);

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
		gamePaused=false;
		pauseTable.remove();
		
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
		}

		return true;
	}
 
    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (x < width / 2 && y > height / 2) {
            //controller.leftReleased();
            controller.jumpReleased();
        }
        if (x > width / 2 && y > height / 2) {
            //controller.rightReleased();
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

}
