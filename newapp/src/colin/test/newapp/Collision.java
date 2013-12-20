package colin.test.newapp;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Collision {
	private Eater eater;
	private ArrayList<Food> food;
	private World world;
	
	public Collision(World game){
		world = game;
		eater = world.getEater();
		food = world.getFood();
	}
	
	public void update(float delta){
		Iterator it=food.iterator();
		while(it.hasNext()){
			checkCollision((Food)it.next());
		}
	}
	
	public void checkCollision(Food item){
		if(eater.isColliding(item.position, item.SIZE) && item.getExists()){
			item.setExists(false);
			eater.increaseScore();
		}
	}
}
