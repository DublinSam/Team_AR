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
	

	/** for debug rendering **/
	ShapeRenderer debugRenderer = new ShapeRenderer();

	public WorldRenderer(World world) {
		// Load assets
		Assets.instance.init(new AssetManager());
		this.world = world;
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

		Rectangle eaterBounds = eater.getBounds();
		float eaterBottomLeftX = eater.getPosition().x - eaterBounds.width/2;
		float eaterBottomLeftY = eater.getPosition().y - eaterBounds.height/2;
		debugRenderer.setColor(new Color(0, 1, 0, 1));
		debugRenderer.rect(eaterBottomLeftX, eaterBottomLeftY, eaterBounds.width, eaterBounds.height);
		ArrayList<Food> foodList= world.getFood();
		Rectangle foodBounds;
		if(!(foodList==null)){
			Iterator<Food> it=foodList.iterator();
			while(it.hasNext()){
				Food foodItem = it.next();
				foodBounds=foodItem.getBounds();
				float foodBottomLeftX = foodItem.getPosition().x - foodBounds.width/2;
				float foodBottomLeftY = foodItem.getPosition().y - foodBounds.height/2;
				debugRenderer.setColor(new Color(0, 1, 0, 1));
				debugRenderer.rect(foodBottomLeftX, foodBottomLeftY, foodBounds.width, foodBounds.height);
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