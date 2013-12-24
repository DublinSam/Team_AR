package colin.test.newapp;

import java.util.ArrayList;
import java.util.Iterator;

import colin.test.newapp.controller.WorldController;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.assets.AssetManager;

import colin.test.newapp.Assets;

public class WorldRenderer {

	private World world;
	private OrthographicCamera cam;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;

	/** for debug rendering **/
	ShapeRenderer debugRenderer = new ShapeRenderer();

	public WorldRenderer(World world) {
		// Load assets
		Assets.instance.init(new AssetManager());
		this.world = world;
		batch = new SpriteBatch();
		this.cam = new OrthographicCamera(7, 10);
		this.cam.position.set(3.5f, 5, 0);
		
		this.cam.update();
		
		cameraGUI = new OrthographicCamera(7, 10);
		cameraGUI.position.set(3.5f, 5f, 0);
		cameraGUI.setToOrtho(false); // flip y-axis
		cameraGUI.update();
	}
	
	public void loadTextures(){
		//will be used to load textures
	}
	
	public void render() {
		//currently only gives boxes for objects, will call drawFood + drawBob in future
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeType.Line);

		// render Eater
		Eater eater = world.getEater();

		Rectangle rect = eater.getBounds();
		float x1 = eater.getPosition().x + rect.x;
		float y1 = eater.getPosition().y + rect.y;
		debugRenderer.setColor(new Color(0, 1, 0, 1));
		debugRenderer.rect(x1, y1, rect.width, rect.height);
		ArrayList<Food> foodList= world.getFood();
		if(!(foodList==null)){
			Iterator<Food> it=foodList.iterator();
			while(it.hasNext()){
				Food food = it.next();
				float x2 = food.getPosition().x + rect.x;
				float y2 = food.getPosition().y + rect.y;
				debugRenderer.setColor(new Color(0, 1, 0, 1));
				debugRenderer.rect(x2, y2, rect.width, rect.height);
			}
		}
		debugRenderer.end();
		
		
	}
	

	


	
	
	public void drawEater(){
		//will be used to load specific texture associated with eater
	}
	
	public void drawFood(){
		//to draw falling food
	}
}