package colin.test.newapp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Score extends Actor {
	Eater eater;
	int score=0;
	float height;
	float width;
	float posX;
	float posY;
	/**eater passed to constructor in order to give reference to score**/
	Score(Eater eater,float height,float width, float posX,float posY){
		this.eater=eater;
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
	
	float x = 5f;
	float y = 475;
	Assets.instance.fonts.defaultBig.draw(batch, "Score " + eater.getScore(), x, y);
	batch.end();
	batch.begin();
}
}
