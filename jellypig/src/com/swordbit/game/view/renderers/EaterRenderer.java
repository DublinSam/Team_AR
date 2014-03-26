package com.swordbit.game.view.renderers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.swordbit.game.model.Eater;
import com.swordbit.game.model.World;
import com.swordbit.game.utils.Assets;

public class EaterRenderer implements PropertyChangeListener{
	private World world;
	private Eater eater;
	private Animation currentAnimation;
	private Animation eatingAnimation;
	private Animation blinkingAnimation;
	private Animation explosionAnimation;
	private AtlasRegion currentStateTexture;
	
	
	public EaterRenderer(World world) {
		init(world);
		loadAnimations();
	}
	
	private void init(World world) {
		this.world = world;
		this.eater = this.world.getEater();
		currentStateTexture = Assets.instance.jellypigStates.jellypig;
	}
	
	private void loadAnimations() {
		constructBlinkAnimation();
		constructEatingAnimation();
		constructTransformationAnimation();
	}

	private void constructTransformationAnimation() {
		AtlasRegion[] explosionFrames = Assets.instance.explosionAnimation.explosionFrames;
		explosionAnimation = new Animation(0.1f, explosionFrames);
		explosionAnimation.setPlayMode(Animation.NORMAL);
	}

	private void constructEatingAnimation() {
		AtlasRegion[] eatingFrames = Assets.instance.eatingAnimation.eatingFrames;
		eatingAnimation = new Animation(0.1f, eatingFrames);
		eatingAnimation.setPlayMode(Animation.NORMAL);
	}

	private void constructBlinkAnimation() {
		AtlasRegion[] blinkingFrames = Assets.instance.blinkingAnimation.blinkingFrames;
		blinkingAnimation = new Animation(0.1f, blinkingFrames);
		blinkingAnimation.setPlayMode(Animation.LOOP_PINGPONG);
	}

	public void drawEater(SpriteBatch spriteBatch) {
			if (currentAnimation == null) 
				drawEaterWithoutAnimation(spriteBatch);
			else 
				drawEaterWithAnimation(spriteBatch);
		}
		
	private void drawEaterWithoutAnimation(SpriteBatch spriteBatch) {
			spriteBatch.draw(currentStateTexture, 
							eater.getPosition().x,
							eater.getPosition().y, 
							eater.getBounds().width,
							eater.getBounds().height);
	}
	
	private void drawEaterWithAnimation(SpriteBatch spriteBatch) {
		Rectangle eaterBounds = eater.getBounds();
		TextureRegion currentFrame = currentAnimation.getKeyFrame(
				eater.getTimeInState(),false);
		spriteBatch.draw(currentFrame, 
						eater.getPosition().x,
						eater.getPosition().y - 0.1f,
						eaterBounds.width,
						eaterBounds.height);
		
		if (currentAnimation.isAnimationFinished(eater.getTimeInState())) {
			if (eater.getState() == "EATING") {
				eater.forceState("TRANSFORMING");
			} else if (eater.getState() == "TRANSFORMING") {
				String finalState = eater.getFinalState();
				System.out.println(finalState);
				eater.forceState(finalState);
				currentAnimation = null;
			} else {
				eater.forceState("IDLE");
				currentAnimation = null;
			}
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// code listens for changes to eater, called in setState in eater class
		if (event.getPropertyName().equals("state")) {
			String state = (String) event.getNewValue();
			if (state == "BLINK")
				currentAnimation = blinkingAnimation;	
			else if(state == "JUMPING")
				currentStateTexture = Assets.instance.jellypigStates.jumping;
			else if(state == "IDLE")
				currentStateTexture = Assets.instance.jellypigStates.jellypig;
			else if(state == "TRANSFORMING")
				currentAnimation = explosionAnimation;
			else if(state == "FAT")
				currentStateTexture = Assets.instance.jellypigStates.fat;
			else if(state == "ACNE")
				currentStateTexture = Assets.instance.jellypigStates.acne;
			else if(state == "HOT")
				currentStateTexture = Assets.instance.jellypigStates.enchilado;
			else if(state == "SOUR")
				currentStateTexture = Assets.instance.jellypigStates.sour;
			else if(state == "HAPPY")
				currentStateTexture = Assets.instance.jellypigStates.happy;
			else if(state == "EATING")
				currentAnimation = eatingAnimation;
			else
				currentAnimation = eatingAnimation;
			}
		}
}
