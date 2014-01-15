package colin.test.newapp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import colin.test.newapp.model.Eater;
import colin.test.newapp.model.Eater.State;
import colin.test.newapp.model.Food;
import colin.test.newapp.model.Food.FoodType;
import colin.test.newapp.model.World;
import colin.test.newapp.util.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class WorldRenderer  implements PropertyChangeListener  {

	private World world;
	private Eater eater;
	private OrthographicCamera cam;
	private OrthographicCamera cameraGUI;
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
	

	/** for debug rendering **/
	ShapeRenderer debugRenderer = new ShapeRenderer();
	private TextureRegion chilliTexture;
	private TextureRegion hotdogTexture;
	private TextureRegion burgerTexture;
	private TextureRegion lemonTexture;
	private TextureRegion appleTexture;
	private TextureRegion pizzaTexture;

	public WorldRenderer(World world) {
		// Load assets
		fpslog = new FPSLogger();
		Assets.instance.init(new AssetManager());
		this.world = world;
		this.eater=this.world.getEater();
		this.cam = new OrthographicCamera(7, 10);
		this.cam.position.set(3.5f, 5, 0);
		
		this.cam.update();
		
		cameraGUI = new OrthographicCamera(7, 10);
		cameraGUI.position.set(3.5f, 5f, 0);
		cameraGUI.setToOrtho(false); // flip y-axis
		cameraGUI.update();

        loadTextures();
	}
	
	public void loadTextures(){
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("atlas/textures.pack"));
		spriteBatch = new SpriteBatch();
	    spriteBatch.setProjectionMatrix(cam.combined);    
		idleTexture = new Texture(Gdx.files.internal("images/JellyPig.png"));
		idleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion tr = new TextureRegion(idleTexture);
		cookieTexture = atlas.findRegion("Cookie");
		strawBerryTexture = atlas.findRegion("Cookie");
		chilliTexture =atlas.findRegion("Chilli");
		hotdogTexture = atlas.findRegion("Hotdog");
		burgerTexture = atlas.findRegion("Hamburger");
		lemonTexture = atlas.findRegion("Lemon");
		appleTexture = atlas.findRegion("Apple");
		pizzaTexture = atlas.findRegion("Pizza");
		blinkSheet = new Texture(Gdx.files.internal("images/JellyPigSprite.png"));     // #9
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
        defaultEaterTexture=new Texture(Gdx.files.internal("images/JellyPig.png"));
        
	}
	
	public void render() {
		
		//drawDebug();
		spriteBatch.begin();
        drawFood();                  
		drawEater();
		spriteBatch.end();
		fpslog.log();
	
	}
	
	public void drawEater(){
		    
        
        drawEaterAnimation();     
	}
	
	private void drawEaterAnimation() {
		Rectangle eaterBounds = eater.getBounds();
		float eaterBottomLeftX = eater.getPosition().x - eaterBounds.width/2;
		float eaterBottomLeftY = eater.getPosition().y;
		if(currentAnimation==null){
			
			spriteBatch.draw(idleTexture, eaterBottomLeftX, eaterBottomLeftY,eaterBounds.width,eaterBounds.height);
			
		}
       else{

	        currentFrame = currentAnimation.getKeyFrame(eater.getTimeInState(), false);
	        spriteBatch.draw(currentFrame, eaterBottomLeftX, eaterBottomLeftY,eaterBounds.width,eaterBounds.height); 
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
		Rectangle foodBounds = food.getBounds();
		float foodBottomLeftX = food.getPosition().x - foodBounds.width/2;
		float foodBottomLeftY = food.getPosition().y - foodBounds.height/2;
		switch(foodType){
		case COOKIE: spriteBatch.draw(cookieTexture, foodBottomLeftX ,foodBottomLeftY, food.getBounds().width, food.getBounds().height);
		break;
		
		case STRAWBERRY: spriteBatch.draw(strawBerryTexture, foodBottomLeftX ,foodBottomLeftY, food.getBounds().width, food.getBounds().height);
		break;
		
		case CHILLI: spriteBatch.draw(chilliTexture, foodBottomLeftX ,foodBottomLeftY, food.getBounds().width, food.getBounds().height);
		break;
		
		case HOTDOG: spriteBatch.draw(hotdogTexture, foodBottomLeftX ,foodBottomLeftY, food.getBounds().width, food.getBounds().height);
		break;
		
		case BURGER: spriteBatch.draw(burgerTexture, foodBottomLeftX ,foodBottomLeftY, food.getBounds().width, food.getBounds().height);
		break;
		
		case LEMON: spriteBatch.draw(lemonTexture, foodBottomLeftX ,foodBottomLeftY, food.getBounds().width, food.getBounds().height);
		break;
		
		case APPLE: spriteBatch.draw(appleTexture, foodBottomLeftX ,foodBottomLeftY, food.getBounds().width, food.getBounds().height);
		break;
		
		case PIZZA: spriteBatch.draw(pizzaTexture, foodBottomLeftX ,foodBottomLeftY, food.getBounds().width, food.getBounds().height);
		break;
		}
	}
	public void drawDebug(){
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeType.Line);

		// render Eater
		Eater eater = world.getEater();

		Rectangle eaterBounds = eater.getBounds();
		float eaterBottomLeftX = eater.getPosition().x - eaterBounds.width/2;
		float eaterBottomLeftY = eater.getPosition().y;
		debugRenderer.setColor(new Color(0, 1, 0, 1));
		debugRenderer.rect(eaterBottomLeftX, eaterBottomLeftY, eaterBounds.width, eaterBounds.height);
		List<Food> foodList= world.getFood();
		Rectangle foodBounds;
		if(!(foodList==null)){
			Iterator<Food> it=foodList.iterator();
			while(it.hasNext()){
				Food foodItem = it.next();
				foodBounds=foodItem.getBounds();
				float foodBottomLeftX = foodItem.getPosition().x - foodBounds.width/2;
				float foodBottomLeftY = foodItem.getPosition().y - foodBounds.height/2;
				if(foodItem.getFoodType()==FoodType.COOKIE){
					debugRenderer.setColor(1, 0, 0, 1);
				}
				else{
					debugRenderer.setColor(new Color(0, 1, 0, 1));
				}
				debugRenderer.rect(foodBottomLeftX, foodBottomLeftY, foodBounds.width, foodBounds.height);
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