package colin.test.newapp;

import colin.test.newapp.controller.WorldController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**Game Screen, repsonsible for displaying all game events, and also catching relevant input commands**/
public class GameScreen implements Screen, InputProcessor {

    private SpriteBatch spriteBatch;
    private Texture splsh;
	private World world;
	private float gameTime;
	private WorldRenderer renderer;
	private WorldController controller;
	private OrthographicCamera cam;
	private int width, height;
	private final float CAMERA_WIDTH = 7f;
	private final float CAMERA_HEIGHT=10f;
	public GameScreen(){
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
		world = new World();
		
	}
	/**Draw Screen**/
	@Override
	public void render(float delta) {
		//Background color blue
		Gdx.gl.glClearColor(0f, 0f, 1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		//Controller updates objects positions, render draws them to screen
		        controller.update(delta);
		        renderer.render();
		        //when gameTime is greater than 4 seconds food will spawn// 
		        if(gameTime%5>4){
		        	world.spawnFood(3.5f, 10);
		        	world.spawnFood(4f, 12);
		        	world.spawnFood(5f, 11);
		        	world.spawnFood(1.5f, 10);
		        	world.spawnFood(0.5f, 14);
	}
		        gameTime+=delta;
		      
		        
		      
		
	}

	@Override
	public void resize(int width, int height) {
		this.height=height;
		this.width=width;
		
	}

	@Override
	public void show() {
	world = new World();
	
	renderer=new WorldRenderer(world);
	controller = new WorldController(world);
	Gdx.input.setInputProcessor(this);

		
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
