package com.swordbit.game.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.swordbit.game.utils.Assets;
import com.swordbit.game.utils.Constants;

public class Scene2 extends Cutscene {

	public Scene2(Stage stage) {
		super(stage,"images/intro-02.png");		
		((OrthographicCamera)stage.getCamera()).zoom=0.5f;
		((OrthographicCamera)stage.getCamera()).position.x=(Constants.VIEWPORT_GUI_WIDTH*0.4f);
	}

	@Override
	public boolean pan() {
		boolean end=false;
		((OrthographicCamera)stage.getCamera()).position.x-=0.4;
		stage.getCamera().update();
		if(stage.getCamera().position.x<Constants.VIEWPORT_GUI_WIDTH/3){
			((OrthographicCamera)stage.getCamera()).position.x=Constants.VIEWPORT_GUI_WIDTH/2;
			((OrthographicCamera)stage.getCamera()).position.y=Constants.VIEWPORT_GUI_HEIGHT/2;
			((OrthographicCamera)stage.getCamera()).zoom=1;
			end=true;
			}
		return end;
	
	}
	@Override
	public Cutscene nextScene() {

return new Scene3(stage);
	}
	
}

