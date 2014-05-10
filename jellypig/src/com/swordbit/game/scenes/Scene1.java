package com.swordbit.game.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.swordbit.game.utils.Assets;
import com.swordbit.game.utils.Constants;

public class Scene1 extends Cutscene {

	public Scene1(Stage stage) {
		super(stage,"images/intro-01.png");

	}

	@Override
	public boolean pan() {
		boolean end=false;
		((OrthographicCamera)stage.getCamera()).zoom-=0.002;
		((OrthographicCamera)stage.getCamera()).position.y-=0.4;
		stage.getCamera().update();
		if(((OrthographicCamera)stage.getCamera()).zoom<0.6)
			{((OrthographicCamera)stage.getCamera()).zoom=1;
			((OrthographicCamera)stage.getCamera()).position.y=Constants.VIEWPORT_GUI_HEIGHT/2;
			end=true;
			}
		return end;
	}
	@Override
	public Cutscene nextScene() {
		return new Scene2(stage);
	}

}
