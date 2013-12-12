package colin.test.newapp;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
/** The world where we place out eater and also the bits of food.**/
public class World {

	/** Our player controlled hero **/
	Eater eater;
	/**Might want to consider not using an array, not sure though**/
	Food[] food;
	public int nofood;
	int maxfood=5;
	//FoodSpawner foodSpawner;

public World(){
	nofood=0;
	food = new Food[maxfood];
	createDemoWorld();
}
/**World only consists of Eater on creation since food is created dynamically (food created in gamescreen in render())**/
public void createDemoWorld(){
	this.eater=new Eater(new Vector2(3.5f,0));
	
}
/**In current implementation the food array is never cleared, so once maxfood is reached no more items will be drawn in the game**/
public void spawnFood(float posX, float posY){
	if(!(nofood==maxfood)){
	this.food[nofood]=new Food(new Vector2(posX,posY));
	nofood++;
	}
}
/**Returns eater from world**/
public Eater getEater() {
	return eater;
}
/**Returns the array of food Items in the world**/
public Food[] getFood(){
	return food;
}


}