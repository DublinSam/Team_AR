package colin.test.newapp;

import colin.test.newapp.model.Eater;
import colin.test.newapp.model.World;
import colin.test.newapp.ui.HighScore;
import colin.test.newapp.ui.Score;
import colin.test.newapp.util.PreferencesHelper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class GameOverScreen implements Screen{
	private static final float CAMERA_WIDTH = 10;
	private static final float CAMERA_HEIGHT = 7;
	private Game myGame;
	Stage stage;
	Eater eater;
	Table table;
	PreferencesHelper phelp = new PreferencesHelper();
	private OrthographicCamera cam;
public GameOverScreen(Game myGame,Eater eater){
	this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
	this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
	this.cam.update();
	this.myGame=myGame;
	this.eater=eater;
}
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1f, 0f, 0f, 1);
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
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		BitmapFont buttonFont = new BitmapFont();
		table=new Table();
		table.setFillParent(true);
		stage=new Stage();
		Label highScoreLabel = new Label( "Highscore "+highScore, skin);
		Label scoreLabel = new Label( "Score "+eater.getScore(), skin);
		table.add(highScoreLabel).pad(10);
		table.row();
		table.add(scoreLabel).pad(10);
		table.row();
		Texture grey = new Texture(Gdx.files.internal("images/newgreybutton.png"));
		Texture black = new Texture(Gdx.files.internal("images/newblackbutton.png"));
		TextureRegion greyRegion = new TextureRegion(grey);
		TextureRegion blackRegion = new TextureRegion(black);
		TextureRegion upRegion =greyRegion;
		TextureRegion downRegion =blackRegion;
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = new TextureRegionDrawable(upRegion);
		style.down = new TextureRegionDrawable(downRegion);
		style.font = buttonFont;
		TextButton restartButton = new TextButton("Restart", style);
		restartButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				myGame.getScreen().dispose();
				myGame.setScreen(new GameScreen(myGame,new World()));
					}
			
	});
		TextButton mainMenuButton = new TextButton("Main Menu", style);
		mainMenuButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				super.clicked(event, x, y);
				myGame.getScreen().dispose();
				myGame.setScreen(new MainMenuScreen(myGame));
			}
		});
		table.add(restartButton).pad(10);
		table.row();
		table.add(mainMenuButton);
		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);
		
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
