package com.swordbit.game.util;

public class Benchmarker {
	public long startTime;
	public long endTime;
	
	public void startTimer(long startTime) {
		this.startTime = startTime;
	}
	
	public void endTimer(long endTime) {
		this.endTime = endTime;
		System.out.println("Total render execution time: " + (endTime-startTime) + "ms"); 
	}
}
