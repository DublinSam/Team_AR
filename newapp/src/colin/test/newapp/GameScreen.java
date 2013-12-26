package colin.test.newapp;

import colin.test.newapp.controller.WorldController;

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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**Game Screen, repsonsible for displaying all game events, and also catching relevant input commands**/
public class GameScreen implements Screen, InputProcessor {
	private World world;
	private WorldRenderer renderer;
	private WorldController controller;
	private OrthographicCamera cam;
	private int width, height;
	private Game myGame;
	Table pauseTable;
	private final float CAMERA_WIDTH = 7f;
	private final float CAMERA_HEIGHT=10f;
	boolean gamePaused=false;
	Stage stage;
	
	
	public GameScreen(Game game){
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
		world = new World();
		this.myGame=game;
	}
	
	/**Draw Screen**/
	@Override
	public void render(float delta) {
		//Background color blue
		Gdx.gl.glClearColor(0f, 0f, 1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//Controller updates objects positions, render draws them to screen, positions should not change if paused
		if(!(gamePaused)){
		controller.update(delta);
		}
		
		renderer.render();
		renderGui(stage.getSpriteBatch());
		
		
	}

	@Override
	public void resize(int width, int height) {
		this.height=height;
		this.width=width;
	}

	@Override
	public void show() {
		stage = new Stage();
	//stage.setCamera(cam);
		
		world = new World();
		renderer=new WorldRenderer(world);
		controller = new WorldController(world);
		pauseTable = new Table();
		//sets pause table to fill screen, will want to change this in future
        pauseTable.setFillParent(true);
        //stage.addActor(table);
        createPauseButton();
        createScore();
        createPauseTable();
        
	    //stage.addActor(table);
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(this);
		
		
		Gdx.input.setInputProcessor(multiplexer);
	}
public void createPauseTable(){
	BitmapFont buttonFont = new BitmapFont();
	Texture grey = new Texture(Gdx.files.internal("data/greyskin.png"));
	Texture black = new Texture(Gdx.files.internal("data/blackSkin.png"));
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
	pauseTable.add(button1);
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
		batch.setProjectionMatrix(cam.combined);
		
		stage.draw();
		
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
public void createPauseButton(){
	PauseButton pb = new PauseButton(25,75,5,0);

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
	stage.addActor(pb);
}
public void createScore(){ 
	Score score = new Score(world.getEater(),25,75,5f,475);
	stage.addActor(score);
}
public void pauseGame() {
	       gamePaused=true;
	       stage.addActor(pauseTable);
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
		return true;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.LEFT)
			controller.leftReleased();
		if (keycode == Keys.RIGHT)
			controller.rightReleased();
		return true;
	}
	
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
    public boolean touchDown(int x, int y, int pointer, int button) {
		if (!Gdx.app.getType().equals(ApplicationType.Android))
			return false;
		if (x > width / 2 && y > height / 2) {
			controller.rightPressed();
		}
		if (x < width / 2 && y > height / 2) {
			controller.leftPressed();
		}
		System.out.println(x+" "+y+" "+width/2);
		return true;
	}
 
    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
    	if (!Gdx.app.getType().equals(ApplicationType.Android))
    		    return false;

        if (x < width / 2 && y > height / 2) {
            controller.leftReleased();
        }
        if (x > width / 2 && y > height / 2) {
            controller.rightReleased();
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
