package colin.test.newapp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class WorldRenderer {

	private World world;
	private OrthographicCamera cam;

	/** for debug rendering **/
	ShapeRenderer debugRenderer = new ShapeRenderer();

	public WorldRenderer(World world) {
		this.world = world;
		this.cam = new OrthographicCamera(7, 10);
		this.cam.position.set(3.5f, 5, 0);
		this.cam.update();
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
		Food[] food= world.getFood();
		if(!(food==null)){
		for(int i=0;i<world.nofood;i++){
		float x2 = food[i].getPosition().x + rect.x;
		float y2 = food[i].getPosition().y + rect.y;
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