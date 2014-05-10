package com.swordbit.game.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Scene5 extends Cutscene {
float t=0;
float angle=0;
boolean rotateRight=true;
float currentAngle=0;
private boolean zoomIn=true;
	public Scene5(Stage stage, String imageLocation) {
		super(stage, imageLocation);
		((OrthographicCamera)stage.getCamera()).zoom=0.8f;
		currentAngle=0;
	}
@Override
public boolean pan() {
	boolean end= false;
	zoomInAndOut();
	t+=0.01;
if(rotateRight){
	angle=5;
}else{angle=-5;}
System.out.println(currentAngle);
currentAngle+=angle;
if(currentAngle>20||currentAngle<-20){
	rotateRight=!rotateRight;
}

		stage.getCamera().rotate(new Vector3(0,0,1), angle);
		stage.getCamera().update();
		if(t>1){
			((OrthographicCamera)stage.getCamera()).zoom=1f;
			stage.getCamera().rotate(new Vector3(0,0,1), -currentAngle);
			
			
			end=true;}
	return end;
}
private void zoomInAndOut() {
	if(zoomIn){
	((OrthographicCamera)stage.getCamera()).zoom-=0.04;
	}
	if(((OrthographicCamera)stage.getCamera()).zoom<=0.6){
	zoomIn=false;

	}
	
	if(!zoomIn){
		((OrthographicCamera)stage.getCamera()).zoom+=0.04;
if(((OrthographicCamera)stage.getCamera()).zoom>=0.8){
	zoomIn=true;
}
	}
	
}
@Override
public Cutscene nextScene() {
return new Scene6(stage,"images/intro-06.png");
}
}
