package colin.test.newapp;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
/** The world where we place out eater and also the bits of food.**/

public class World {
	/** Our player controlled hero **/
	Eater eater;
	ArrayList<Food> foodInWorld;
	
	public World(){
		foodInWorld = new ArrayList<Food>();
		createDemoWorld();
	}
	
	/**World only consists of Eater on creation since food is created dynamically (food created in gamescreen in render())**/
	public void createDemoWorld(){
		this.eater=new Eater(new Vector2(3.5f,0.25f));
	}

	/**In current implementation the food array is never cleared, so once maxfood is reached no more items will be drawn in the game**/
	public void spawnFood(float posX, float posY){
		
			
			foodInWorld.add(new Food(new Vector2(posX,posY)));
			
			
		
	}
	
	/**Returns eater from world**/
	public Eater getEater() {
		return eater;
	}
	
	/**Returns the array of food Items in the world**/
	public ArrayList<Food> getFood(){
		return foodInWorld;
	}
}