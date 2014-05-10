package com.swordbit.game.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Scene6 extends Cutscene {
float t=0;
	Scene6(Stage stage, String imageLocation) {
		super(stage, imageLocation);
		((OrthographicCamera)stage.getCamera()).zoom=0.5f;
	}
	@Override
	public boolean pan() {
		boolean end= false;
		if(((OrthographicCamera)stage.getCamera()).zoom<=1)
		((OrthographicCamera)stage.getCamera()).zoom+=0.05;
		t+=0.01;
		if(t>5){end=true;}
		return end;
	}


}
