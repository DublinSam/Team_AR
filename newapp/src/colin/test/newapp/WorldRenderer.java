package colin.test.newapp;

import java.util.ArrayList;
import java.util.Iterator;

import colin.test.newapp.model.Eater;
import colin.test.newapp.model.Food;
import colin.test.newapp.model.World;
import colin.test.newapp.util.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class WorldRenderer {

	private World world;
	private OrthographicCamera cam;
	private OrthographicCamera cameraGUI;
    private static final int        FRAME_COLS = 5;      
    private static final int        FRAME_ROWS = 1;      
    
    Texture cookieTexture;
    Texture strawBerryTexture;
    Texture defaultEaterTexture;
    Animation                       blinkAnimation;       
    Texture                         blinkSheet;              
    TextureRegion[]                 blinkFrames;          
    SpriteBatch                     spriteBatch;          
    TextureRegion                   currentFrame;          
    
    /**stateTime used to keep track of what stage eater is at the blinking animation**/
    float stateTime;                                        
    /**gameTime used to get blinking occurring periodically**/
    float timeNotBlinked;
	

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

        loadTextures();
	}
	
	public void loadTextures(){
		cookieTexture = new  Texture(Gdx.files.internal("images/Cookie.png"));
		strawBerryTexture = new  Texture(Gdx.files.internal("images/Strawberry.png"));
		blinkSheet = new Texture(Gdx.files.internal("images/JellyPigSprite.png"));     // #9
        TextureRegion[][] tmp = TextureRegion.split(blinkSheet, blinkSheet.getWidth() / 
        FRAME_COLS, blinkSheet.getHeight() / FRAME_ROWS);                                // #10
        blinkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
                for (int j = 0; j < FRAME_COLS; j++) {
                        blinkFrames[index++] = tmp[i][j];
                }
        }
        blinkAnimation = new Animation(0.1f, blinkFrames);              // #11
        spriteBatch = new SpriteBatch(); 
        spriteBatch.setProjectionMatrix(cam.combined);
        stateTime = 0f;                                                 
        timeNotBlinked=0f;
        defaultEaterTexture=new Texture(Gdx.files.internal("images/JellyPig.png"));
	}
	
	public void render() {
		//currently only gives boxes for objects, will call drawFood + drawBob in future
		
		drawDebug();
        drawFood();                  
		drawEater();
		
	}
	
	public void drawEater(){
		spriteBatch.begin();
		Eater eater = world.getEater();
		Rectangle eaterBounds = eater.getBounds();
		float eaterBottomLeftX = eater.getPosition().x - eaterBounds.width/2;
		float eaterBottomLeftY = eater.getPosition().y - eaterBounds.height/2;
        timeNotBlinked += Gdx.graphics.getDeltaTime(); 
        if(timeNotBlinked>5){
        //blink animation
        stateTime += Gdx.graphics.getDeltaTime(); 
        currentFrame = blinkAnimation.getKeyFrame(stateTime, false);
        spriteBatch.draw(currentFrame, eaterBottomLeftX, eaterBottomLeftY,eaterBounds.width,eaterBounds.height); 
        }
        else{
        	//default frame of eater to play when not blinking
        	spriteBatch.draw(defaultEaterTexture, eaterBottomLeftX, eaterBottomLeftY,eaterBounds.width,eaterBounds.height); 
        }

       
        if(blinkAnimation.isAnimationFinished(stateTime)){
        	timeNotBlinked=0;
        	stateTime=0;
        }
        
        spriteBatch.end();
	}
	
	public void drawFood(){
		ArrayList<Food> foodList= world.getFood();
		if(!(foodList==null)){
			Iterator<Food> it=foodList.iterator();
			spriteBatch.begin();
			while(it.hasNext()){
				Food foodItem = it.next();
				drawFoodItem(foodItem);
			}
			spriteBatch.end();
		}
	}
	
	public void drawFoodItem(Food food){
		int foodType = food.getFoodType();
		Rectangle foodBounds = food.getBounds();
		float foodBottomLeftX = food.getPosition().x - foodBounds.width/2;
		float foodBottomLeftY = food.getPosition().y - foodBounds.height/2;
		switch(foodType){
		case 0: spriteBatch.draw(cookieTexture, foodBottomLeftX ,foodBottomLeftY, food.getBounds().width, food.getBounds().height);
		break;
		
		case 1: spriteBatch.draw(strawBerryTexture, foodBottomLeftX ,foodBottomLeftY, food.getBounds().width, food.getBounds().height);
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
				if(foodItem.getFoodType()==0){
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
}