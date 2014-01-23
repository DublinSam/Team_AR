package colin.test.newapp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;

import colin.test.newapp.model.Eater;
import colin.test.newapp.model.Eater.State;
import colin.test.newapp.model.Food;
import colin.test.newapp.model.Food.FoodType;
import colin.test.newapp.model.World;
import colin.test.newapp.util.Assets;
import colin.test.newapp.util.ProgressBar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class WorldRenderer  implements PropertyChangeListener  {

	private World world;
	private Eater eater;
	private OrthographicCamera cam;
	int CAMERA_WIDTH=7;
	int CAMERA_HEIGHT=10;
	int width;
	int height;
    private static final int        BLINK_FRAME_COLS = 5;      
    private static final int        BLINK_FRAME_ROWS = 1; 
    
    Texture idleTexture;
    TextureRegion cookieTexture;
    TextureRegion strawBerryTexture;
    Texture defaultEaterTexture;
    Animation                       blinkAnimation;
    Animation                       currentAnimation;
    Texture                         blinkSheet;              
    TextureRegion[]                 blinkFrames;
    TextureRegion[] 				idleFrames;
    SpriteBatch                     spriteBatch;          
    TextureRegion                   currentFrame;          
    FPSLogger fpslog;                                      

	ProgressBar progressBar;
	/** for debug rendering **/
	ShapeRenderer debugRenderer = new ShapeRenderer();
	private TextureRegion chilliTexture;
	private TextureRegion hotdogTexture;
	private TextureRegion burgerTexture;
	private TextureRegion lemonTexture;
	private TextureRegion appleTexture;
	private TextureRegion pizzaTexture;
	private Texture hungerTexture;
	private TextureRegion idleTextureRegion;
	TextureRegion tr;
	private TextureRegion hungerTextureRegion;
     private OrthogonalTiledMapRenderer renderer;

	public WorldRenderer(World world) {
		System.out.println("create renderer");
		fpslog = new FPSLogger();
		this.world = world;
		this.eater=this.world.getEater();
		this.cam = new OrthographicCamera();
		this.cam.setToOrtho(false, 10, 7);
		this.cam.position.set(5f, 3.5f, 0);
		this.cam.update();
		
        loadTextures();
        
        progressBar=new ProgressBar(hungerTextureRegion, 5, 3.5f);
        progressBar.SetTargetDimension(1, 4);
	}
	public void setSize(int width,int height){
		this.width=width;
		this.height=height;
		
		
	}
	public void loadTextures(){
		System.out.println("load textures");
		Gdx.app.debug("texts", "loading");
		renderer = new OrthogonalTiledMapRenderer(world.getMap(), 1/48f);
		TextureAtlas atlas = Assets.instance.getAssetManager().get("atlas/textures.pack", TextureAtlas.class);
		spriteBatch = new SpriteBatch();
	    spriteBatch.setProjectionMatrix(cam.combined);
	
		idleTexture = Assets.instance.getAssetManager().get("images/JellyPig.png", Texture.class);
		idleTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		hungerTexture = Assets.instance.getAssetManager().get("images/hunger.png", Texture.class);
		hungerTextureRegion = new TextureRegion(hungerTexture);
		cookieTexture = atlas.findRegion("Cookie");
		strawBerryTexture = atlas.findRegion("Strawberry");
		chilliTexture =atlas.findRegion("Chilli");
		hotdogTexture = atlas.findRegion("Hotdog");
		burgerTexture = atlas.findRegion("Hamburger");
		lemonTexture = atlas.findRegion("Lemon");
		appleTexture = atlas.findRegion("Apple");
		pizzaTexture = atlas.findRegion("Pizza");
		blinkSheet = Assets.instance.getAssetManager().get("images/JellyPigSprite.png", Texture.class);     // #9
        TextureRegion[][] tmp = TextureRegion.split(blinkSheet, blinkSheet.getWidth() / 
        BLINK_FRAME_COLS, blinkSheet.getHeight() / BLINK_FRAME_ROWS);                                // #10
        blinkFrames = new TextureRegion[BLINK_FRAME_COLS * BLINK_FRAME_ROWS];
        idleFrames=new TextureRegion[0];
        int index = 0;
        for (int i = 0; i < BLINK_FRAME_ROWS; i++) {
                for (int j = 0; j < BLINK_FRAME_COLS; j++) {
                        blinkFrames[index++] = tmp[i][j];
                }
        }
       
        blinkAnimation = new Animation(0.1f, blinkFrames);              // #11                                 
      
        
	}
	
	public void render() {
		cam.position.x=eater.getPosition().x;
		cam.update();
		spriteBatch.setProjectionMatrix(cam.combined);
		//drawDebug();
		spriteBatch.begin();
	
		
        drawFood();                  
		drawEater();

		
		spriteBatch.end();
	
		fpslog.log();
		renderer.setView(cam);
        renderer.render();
	}
	
	public void drawEater(){
		    
        
        drawEaterAnimation();     
	}
	
	private void drawEaterAnimation() {
		Rectangle eaterBounds = eater.getBounds();
		
		if(currentAnimation==null){
			
			spriteBatch.draw(idleTexture, eater.getPosition().x, eater.getPosition().y,eater.getBounds().width,eater.getBounds().height);
		
			
		}
       else{

	        currentFrame = currentAnimation.getKeyFrame(eater.getTimeInState(), false);
	        spriteBatch.draw(currentFrame, eater.getPosition().x, eater.getPosition().y,eaterBounds.width,eaterBounds.height); 
	        if(currentAnimation.isAnimationFinished(eater.getTimeInState())){
	        	eater.setState(State.IDLE);
	        	currentAnimation=null;
	        	
	        }
        }
		
	}




	public void drawFood(){
		List<Food> foodList= world.getFood();
		if(!(foodList==null)){
			Iterator<Food> it=foodList.iterator();
			
			while(it.hasNext()){
				Food foodItem = it.next();
				drawFoodItem(foodItem);
			}
		
		}
	}
	
	public void drawFoodItem(Food food){
		FoodType foodType = food.getFoodType();
		switch(foodType){
		case COOKIE: spriteBatch.draw(cookieTexture, food.getBounds().x ,food.getBounds().y, food.getBounds().width, food.getBounds().height);
		break;
		
		case STRAWBERRY: spriteBatch.draw(strawBerryTexture, food.getBounds().x ,food.getBounds().y, food.getBounds().width, food.getBounds().height);
		break;
		
		case CHILLI: spriteBatch.draw(chilliTexture, food.getBounds().x ,food.getBounds().y, food.getBounds().width, food.getBounds().height);
		break;
		
		case HOTDOG: spriteBatch.draw(hotdogTexture, food.getBounds().x ,food.getBounds().y, food.getBounds().width, food.getBounds().height);
		break;
		
		case BURGER: spriteBatch.draw(burgerTexture, food.getBounds().x ,food.getBounds().y, food.getBounds().width, food.getBounds().height);
		break;
		
		case LEMON: spriteBatch.draw(lemonTexture, food.getBounds().x ,food.getBounds().y, food.getBounds().width, food.getBounds().height);
		break;
		
		case APPLE: spriteBatch.draw(appleTexture, food.getBounds().x ,food.getBounds().y, food.getBounds().width, food.getBounds().height);
		break;
		
		case PIZZA: spriteBatch.draw(pizzaTexture, food.getBounds().x ,food.getBounds().y, food.getBounds().width, food.getBounds().height);
		break;
		}
	}
	public void drawDebug(){
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeType.Line);

		// render Eater
		Eater eater = world.getEater();

		Rectangle eaterBounds = eater.getBounds();
		debugRenderer.setColor(new Color(0, 1, 0, 1));
		debugRenderer.rect(eater.getPosition().x, eater.getPosition().y, eaterBounds.width, eaterBounds.height);
		List<Food> foodList= world.getFood();
		Rectangle foodBounds;
		if(!(foodList==null)){
			Iterator<Food> it=foodList.iterator();
			while(it.hasNext()){
				Food foodItem = it.next();
				foodBounds=foodItem.getBounds();
				if(foodItem.getFoodType()==FoodType.COOKIE){
					debugRenderer.setColor(1, 0, 0, 1);
				}
				else{
					debugRenderer.setColor(new Color(0, 1, 0, 1));
				}
				debugRenderer.rect(foodItem.getBounds().x, foodItem.getBounds().y, foodBounds.width, foodBounds.height);
			}
		}
		debugRenderer.end();
	}
    

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		System.out.println("Changed property: " + event.getPropertyName() + " [old -> "
                + event.getOldValue() + "] | [new -> " + event.getNewValue() +"]");
		currentAnimation=blinkAnimation;
		
	}
}