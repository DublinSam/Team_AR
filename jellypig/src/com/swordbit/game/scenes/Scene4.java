package com.swordbit.game.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Scene4 extends Cutscene {
float timeInScene=0;
boolean zoomIn=true;
 float timeTillZoom=0.5f;

	Scene4(Stage stage, String imageLocation) {
		super(stage, imageLocation);
	}
	@Override
	public boolean pan() {
		boolean end= false;
		timeInScene+=0.01;
		if(timeInScene>timeTillZoom){
			
			if(zoomIn){
			((OrthographicCamera)stage.getCamera()).zoom-=0.04;
			}
			if(((OrthographicCamera)stage.getCamera()).zoom<=0.1){
			zoomIn=false;
			end=true;
			((OrthographicCamera)stage.getCamera()).zoom=1;
			}
			/*
			if(!zoomIn){
				((OrthographicCamera)stage.getCamera()).zoom+=0.02;
				if(((OrthographicCamera)stage.getCamera()).zoom>=1){
					end=true;
				}
			}
		*/
		}
		return end;
	}
@Override
public Cutscene nextScene() {
	return new Scene5(stage,"images/intro-05.png");
}
}
