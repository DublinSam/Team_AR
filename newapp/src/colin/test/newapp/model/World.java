package colin.test.newapp.model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
/** The world where we place out eater and also the bits of food.**/

public class World {
	/** Our player controlled hero **/
	
	Eater eater;
	List<Food> foodInWorld;
	int difficulty;
	int foodMissed;
	int foodCollected;
	
	public World(){
		foodInWorld = new ArrayList<Food>();
		createDemoWorld();
	}
	
	/**World only consists of Eater on creation since food is created dynamically (food created in gamescreen in render())**/
	public void createDemoWorld(){
		this.eater=new Eater(new Vector2(3.5f,0.0f));
	
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
	public List<Food> getFood(){
		return foodInWorld;
	}
	public int getDifficulty(){
		return difficulty;
	}

	public int getFoodCollected(){
		return foodCollected;
	}
	public void increaseFoodCollected(){
		this.foodCollected++;
	}
	public void setFoodCollected(int foodCollected){
		this.foodCollected=foodCollected;
	}
	public int getFoodMissed() {
		return foodMissed;
	}
	public void increaseFoodMissed(){
		this.foodMissed++;
	}
	public void setFoodMissed(int foodMissed) {
		this.foodMissed=foodMissed;
		
	}


}