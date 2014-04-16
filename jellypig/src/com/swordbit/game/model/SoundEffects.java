package com.swordbit.game.model;

import com.badlogic.gdx.audio.*;

public class SoundEffects {
	public static final SoundEffects instance = new SoundEffects();
	
public SoundEffects(){
}

public void play(Sound sound){
	sound.play();
	System.out.println("play sound");
}


}
