package com.swordbit.game.model;

import com.badlogic.gdx.audio.Music;

public class GameMusic {
	public static final GameMusic instance = new GameMusic();
	private Music currentmusic;
	public GameMusic(){
	}
	
	public void play(Music music){
		this.currentmusic = music;
		music.play();
		System.out.println("play soundtrack");
	}
	
	public void dispose(){
		currentmusic.dispose();
		System.out.println("dispose playsoundtrack");
	}
	
	public void stop(){
		currentmusic.stop();
		System.out.println("stop playsoundtrack");
	}
}
