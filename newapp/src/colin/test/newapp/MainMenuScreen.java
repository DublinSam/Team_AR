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
	
	public MainMenuScreen(Game game){
	myGame=game;
	}
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		Label welcomeLabel = new Label( "EATER", skin);
		TextButton playButton = new TextButton( "PLAY", skin);
		playButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				myGame.getScreen().dispose();
				myGame.setScreen(new GameScreen(myGame,new World()));
			}
		});
		TextButton settingsButton = new TextButton( "SETTINGS", skin);
		TextButton levelSelectButton = new TextButton("LEVEL SELECT", skin);
		levelSelectButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				myGame.getScreen().dispose();
				myGame.setScreen(new LevelSelectScreen(myGame));
			}
		});
		stage=new Stage();
		Gdx.input.setInputProcessor(stage);
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		table.debug();
		
		table.add(welcomeLabel).pad(10);
		table.row();
		table.add(playButton).pad(10);
		table.row();
		table.add(settingsButton).pad(10);
		table.row();
		table.add(levelSelectButton);
		
		
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
