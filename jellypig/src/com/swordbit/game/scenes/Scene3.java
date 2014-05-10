package com.swordbit.game.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.swordbit.game.utils.Constants;

public class Scene3 extends Cutscene {
float t=0;
	Scene3(Stage stage) {
		super(stage,"images/intro-03.png");
		((OrthographicCamera)stage.getCamera()).zoom=0.5f;
		((OrthographicCamera)stage.getCamera()).position.x=(Constants.VIEWPORT_GUI_WIDTH*0.6f);
	    
	}
	@Override
	public boolean pan() {
		boolean end=false;
		((OrthographicCamera)stage.getCamera()).position.x+=0.4;
		stage.getCamera().update();
		if(stage.getCamera().position.x>(0.67)*Constants.VIEWPORT_GUI_WIDTH){
			((OrthographicCamera)stage.getCamera()).position.x=Constants.VIEWPORT_GUI_WIDTH/2;
			((OrthographicCamera)stage.getCamera()).position.y=Constants.VIEWPORT_GUI_HEIGHT/2;
			((OrthographicCamera)stage.getCamera()).zoom=1;
			end=true;
			}
		return end;
	
	}
@Override
public Cutscene nextScene() {
return new Scene4(stage, "images/intro-04.png");
}
}
