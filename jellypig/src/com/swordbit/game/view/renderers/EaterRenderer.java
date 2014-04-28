package com.swordbit.game.view.renderers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.swordbit.game.model.eater.IdleState;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.swordbit.game.model.SoundEffects;
import com.swordbit.game.model.World;
import com.swordbit.game.model.eater.ActionState;
import com.swordbit.game.model.eater.Eater;
import com.swordbit.game.model.eater.Eater.HealthState;
import com.swordbit.game.model.eater.JumpingState;
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
		constructEatingAnimation();
		constructTransformationAnimation();
	}

	private void constructTransformationAnimation() {
		AtlasRegion[] explosionFrames = Assets.instance.explosionAnimation.explosionFrames;
		explosionAnimation = new Animation(0.1f, explosionFrames);
		explosionAnimation.setPlayMode(Animation.PlayMode.NORMAL);
	}

	private void constructEatingAnimation() {
		AtlasRegion[] eatingFrames = Assets.instance.eatingAnimation.eatingFrames;
		eatingAnimation = new Animation(0.1f, eatingFrames);
		eatingAnimation.setPlayMode(Animation.PlayMode.NORMAL);
	}

	private void constructBlinkAnimation() {
		AtlasRegion[] blinkingFrames = Assets.instance.blinkingAnimation.blinkingFrames;
		blinkingAnimation = new Animation(0.1f, blinkingFrames);
		blinkingAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
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
		TextureRegion currentFrame = currentAnimation.getKeyFrame(eater.getHealthTimer(),false);
		spriteBatch.draw(currentFrame, 
						eater.getPosition().x,
						eater.getPosition().y - 0.1f,
						eaterBounds.width,
						eaterBounds.height);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		
		// Listen to Eater ACTION state machine
		if (event.getPropertyName().equals("actionState")) {
			ActionState actionState = (ActionState) event.getNewValue();
			if(actionState.getClass().equals(JumpingState.class))
				currentStateTexture = Assets.instance.jellypigStates.jumping;
			else if(actionState.getClass().equals(IdleState.class))
				currentStateTexture = Assets.instance.jellypigStates.jellypig;
			//else if(actionState == "TRANSFORMING"){
			//	SoundEffects.instance.play(Assets.instance.sounds.transformation);
			//	currentAnimation = explosionAnimation;	
			}
		
		// Listen to Eater HEALTH state machine
		if (event.getPropertyName().equals("healthState")) {
			HealthState healthState = (HealthState) event.getNewValue();
			if(healthState == healthState.HAPPY)
				currentStateTexture = Assets.instance.jellypigStates.happy;
			else if(healthState == healthState.ACNE)
				currentStateTexture = Assets.instance.jellypigStates.acne;
			else if(healthState == healthState.SOUR)
				currentStateTexture = Assets.instance.jellypigStates.sour;
			else if(healthState == healthState.NEUTRAL)
				currentStateTexture = Assets.instance.jellypigStates.jellypig;
			else if(healthState == healthState.FAT)
				currentStateTexture = Assets.instance.jellypigStates.enchilado;
			else
				currentAnimation = eatingAnimation;
			}
			
		}
	
}
