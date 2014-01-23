package colin.test.newapp;

import colin.test.newapp.model.Eater;
import colin.test.newapp.ui.HighScore;
import colin.test.newapp.ui.Score;
import colin.test.newapp.util.PreferencesHelper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LevelCompletedScreen implements Screen{
	private static final float CAMERA_WIDTH = 10;
	private static final float CAMERA_HEIGHT = 7;
	private Game myGame;
	Stage stage;
	Eater eater;
	Table table;
	PreferencesHelper phelp = new PreferencesHelper();
	private OrthographicCamera cam;
public LevelCompletedScreen(Game myGame,Eater eater){
	this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
	this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
	this.cam.update();
	this.myGame=myGame;
	this.eater=eater;
}
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 1f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		int highScore =phelp.getHighScore();
		table=new Table();
		stage=new Stage();
		HighScore highscore = new HighScore(highScore,5,3.5f,Gdx.graphics.getWidth()/2-100,Gdx.graphics.getHeight()/2);
		Score score = new Score(eater,3,1,Gdx.graphics.getWidth()/2-100,Gdx.graphics.getHeight()/2+50);
		table.add(highscore);
		table.add(score);
		stage.addActor(table);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
