package colin.test.newapp.ui;

import colin.test.newapp.util.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
	public PauseButton(float width,float height, float posX,float posY){
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
	Assets.instance.fonts.defaultNormal.draw(batch, "Pause ", this.getOriginX(), this.getOriginY()+height);
	batch.end();
	batch.begin();
}

}
