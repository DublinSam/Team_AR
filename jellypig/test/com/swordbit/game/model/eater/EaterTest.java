package com.swordbit.game.model.eater;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.beans.PropertyChangeListener;

import org.easymock.EasyMock;
import org.junit.Test;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.swordbit.game.model.eater.Eater;
import com.swordbit.game.model.food.Apple;
import com.swordbit.game.model.food.Pizza;
import com.swordbit.game.view.screens.GameScreen;

public class EaterTest {

	@Test
	public void testEaterConstructor() {
		Vector2 samplePosition = new Vector2(0,0);
		Vector2 presetAcceleration = new Vector2(0, -30);
		Vector2 sampleSize = new Vector2(1,1);
		
		Eater eater = new Eater(samplePosition);
		
		assertEquals("Matches position", samplePosition, eater.position);
		assertEquals("Matches acceleration", presetAcceleration, eater.acceleration);
		assertEquals("Matches size", new Vector2(sampleSize.x, sampleSize.y), 
										new Vector2(eater.bounds.width, eater.bounds.height));
	}
	
	@Test 
	public void testUpdateScore_IncreaseScore() {
		Eater sampleEater = new Eater(new Vector2(0,0));
		Apple apple = EasyMock.createMock(Apple.class);
		apple.scoreValue += 100;
		EasyMock.replay(apple);
		sampleEater.updateScore(apple);
		assertEquals("Correctly calculate apple score", 100, sampleEater.getScore());	
	}
	
	@Test 
	public void testUpdateScore_DecreaseScore() {
		Eater sampleEater = new Eater(new Vector2(0,0));
		Pizza pizza = EasyMock.createMock(Pizza.class);
		pizza.scoreValue += -100;
		EasyMock.replay(pizza);
		sampleEater.updateScore(pizza);
		assertEquals("Correctly calculate pizza score", -100, sampleEater.getScore());	
	}
}
