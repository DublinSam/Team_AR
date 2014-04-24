package com.swordbit.game.model.food;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.badlogic.gdx.math.Vector2;

public class FoodTest {

	@Test
	public void testFoodConstructor() {
		Food sampleFood = new Food();
		assertEquals("Food is initialized with zero lifetime", 0, sampleFood.timeAlive, 0);
	}
	
	@Test
	public void testSetPosition() {
		Food sampleFood = new Food();
		Vector2 samplePosition = new Vector2(0f, 0f);
		sampleFood.setPosition(samplePosition);
		Vector2 resultingBoundsPosition = new Vector2(sampleFood.bounds.x, sampleFood.bounds.y);
		assertEquals("Position is set correctly", samplePosition, sampleFood.position);
		assertEquals("Bounds at that position are set correctly", samplePosition, resultingBoundsPosition);
	}	
}
