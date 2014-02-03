package colin.test.newapp;

import colin.test.newapp.model.World;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class MainMenuScreen implements Screen {
	private Game myGame;
	SpriteBatch spriteBatch;
	private Stage stage;
	private Table table;
	int CAMERA_WIDTH;
	int CAMERA_HEIGHT;
	private TextButton playButton;
	Skin textButtonSkin;
	private TextButton levelSelectButton;
	private TextButton settingsButton;
	private Texture backgroundImage;
	
	public MainMenuScreen(Game game){
	myGame=game;
	CAMERA_HEIGHT=Gdx.graphics.getHeight();
	CAMERA_WIDTH=Gdx.graphics.getWidth();
	backgroundImage=new Texture(Gdx.files.internal("images/LandingPage.png"));
	spriteBatch=new SpriteBatch();
	}
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		spriteBatch.draw(backgroundImage,0,0,CAMERA_WIDTH,CAMERA_HEIGHT);
		spriteBatch.end();
		
		
		stage.act();
		stage.draw();
		//Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		CAMERA_WIDTH=width;
		CAMERA_HEIGHT=height;
		
	}

	@Override
	public void show() {
        //Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
        textButtonSkin = new Skin(Gdx.files.internal("data/textbuttons.json"));
		Label welcomeLabel = new Label( "EATER", textButtonSkin);
		createPlayButton();
		createSettingsButton();
		createLevelSelectButton();
		stage=new Stage();
		Gdx.input.setInputProcessor(stage);
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		//table.debug();
		table.top().padTop(10);
	
		table.left();
		//table.row();
		table.add(playButton).width(CAMERA_WIDTH/3);
		//table.row();
		table.add(settingsButton).width(CAMERA_WIDTH/3);
		//table.row();
		table.add(levelSelectButton).width(CAMERA_WIDTH/3);
		
		
	}

	private void createSettingsButton() {
		settingsButton = new TextButton( "SETTINGS", textButtonSkin);
	}
	private void createLevelSelectButton() {
		levelSelectButton = new TextButton("LEVEL SELECT", textButtonSkin);
		levelSelectButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				myGame.getScreen().dispose();
				myGame.setScreen(new LevelSelectScreen(myGame));
			}
		});
		
	}
	private void createPlayButton() {
		playButton = new TextButton( "PLAY", textButtonSkin);
		playButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				myGame.getScreen().dispose();
				myGame.setScreen(new GameScreen(myGame,new World(0)));
			}
		});
		
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
