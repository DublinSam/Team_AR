package colin.test.newapp;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Collision {
	private Eater eater;
	private ArrayList<Food> food;
	private World world;
	int bottomOfScreen=0;
	public Collision(World game){
		this.world = game;
		this.eater = world.getEater();
		this.food = world.getFood();
		
	}
	
	public void update(float delta){
		Iterator<Food> it=food.iterator();
		while(it.hasNext()){
			Food currentFoodItem = it.next(); 
			checkCollision(currentFoodItem);
			if(currentFoodItem.exists==false){
				it.remove();
			}
		}
	}
	/**checks for collisions between food items + eater and for food that goes past bottom of screen*/
	public void checkCollision(Food item){
		double topOfFoodPos=item.position.y+(item.getBounds().getHeight()/2);
		if(eater.isColliding(item.position, item.SIZE) && item.getExists()){
			item.setExists(false);
			eater.increaseScore();
		}
		else if(topOfFoodPos<bottomOfScreen){
			item.setExists(false);
		}
	}
}
