package colin.test.newapp;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class PauseButton extends Button {
	float height;
	float width;
	float posX;
	float posY;
	PauseButton(float height,float width, float posX,float posY){
		this.height=height;
		this.width=width;
		this.posX=posX;
		this.posY=posY;
		setSize(width, height);
		setPosition(posX, posY);
	}
	
@Override
public void draw(SpriteBatch batch, float parentAlpha) {
	batch.end();
	batch.begin();
	Assets.instance.fonts.defaultBig.draw(batch, "Pause ", posX, posY+height);
	batch.end();
	batch.begin();
}

}
