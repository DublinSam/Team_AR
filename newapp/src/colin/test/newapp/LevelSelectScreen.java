package colin.test.newapp;

import colin.test.newapp.model.World;
import colin.test.newapp.util.Assets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
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
public LevelSelectScreen(Game game){
	myGame=game;
}
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.draw();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		int noOfLevels=Assets.instance.getLevelManager().getNoOfLevels();
		stage=new Stage();
		Gdx.input.setInputProcessor(stage);
		table = new Table();
		table.setFillParent(true);
		for(int i=0;i<noOfLevels;i++){
			TextButton levelButton=new TextButton(Assets.instance.getLevelManager().getLevel(i).getLevelName(),skin);
			LevelListener levelListener =new LevelListener(i,levelButton);
			levelListener.createListener();
			table.add(levelListener.button).pad(10);
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
						World world = new World();
						world.setLevel(level);
						myGame.setScreen(new GameScreen(myGame,world));
		            };
		      });
		   }

		}
}
