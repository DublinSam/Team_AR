package com.swordbit.game.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.swordbit.game.utils.Assets;
import com.swordbit.game.utils.Constants;


 public class Cutscene{
	Image scene;
	Stage stage;
	Cutscene(Stage stage,String imageLocation){
		this.stage=stage;
		stage.clear();
		Texture currentTexture=Assets.instance.getAssetManager().get(imageLocation, Texture.class);
		scene = new Image(currentTexture);
		Table backgroundLayer=new Table();
		backgroundLayer.add(scene);
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, 
					  Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(backgroundLayer);
	}
	public boolean pan(){
		return false;
	}
	public Cutscene nextScene() {
		return this;
	};
		
	
}
