package colin.test.newapp;

import colin.test.newapp.model.World;
import colin.test.newapp.util.Assets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LevelSelectScreen implements Screen {
	
	Game myGame;
	private Stage stage;
	private Table table;
	Texture backgroundImage;
	SpriteBatch spriteBatch;
	int CAMERA_HEIGHT;
	int CAMERA_WIDTH;
public LevelSelectScreen(Game game){
	myGame=game;
	CAMERA_WIDTH=Gdx.graphics.getWidth();
	CAMERA_HEIGHT=Gdx.graphics.getHeight();
}
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		spriteBatch.draw(backgroundImage,0,0,CAMERA_WIDTH, CAMERA_HEIGHT);
		spriteBatch.end();
		stage.draw();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Skin skin = new Skin(Gdx.files.internal("data/textbuttons.json"));
		backgroundImage=new Texture("images/LandingPage.png");
		int noOfLevels=Assets.instance.getLevelManager().getNoOfLevels();
		stage=new Stage();
		spriteBatch = stage.getSpriteBatch();
		Gdx.input.setInputProcessor(stage);
		table = new Table();
		table.setFillParent(true);
		for(int i=0;i<noOfLevels;i++){
			TextButton levelButton=new TextButton(Assets.instance.getLevelManager().getLevel(i).getLevelName(),skin);
			LevelListener levelListener =new LevelListener(i,levelButton);
			levelListener.createListener();
			table.add(levelListener.button).width(CAMERA_WIDTH/3).pad(10);
			table.row();
		}
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
	public class LevelListener{

		   int level;    
		   TextButton button;
		   public LevelListener(int level, TextButton button){
			   this.level=level;
			   this.button=button;
		   }
		   public void createListener(){
		      button.addListener(new ClickListener() {
		            @Override
		            public void clicked(InputEvent event, float x, float y) {
						super.clicked(event, x, y);
						
						;
					
						myGame.setScreen(new GameScreen(myGame,new World(level)));
		            };
		      });
		   }

		}
}
