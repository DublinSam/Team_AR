package colin.test.newapp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
/**Displays the Swordbit image, continues to GameScreen onClick**/
public class MenuScreen implements Screen
{
		int width;
		int height;
        private SpriteBatch spriteBatch;
        private TextureRegion splshreg;
        private Texture splsh;
        private Game myGame;
        
        /**
         * Constructor for the splash screen
         * @param g Game which called this splash screen.
         */
        public MenuScreen(Game g)
        {
                myGame = g;
        }

        @Override
        public void render(float delta)
        {
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                //Gdx.gl.glClearColor(1, 1,1, 1);
                spriteBatch.begin();
                spriteBatch.draw(splshreg,Gdx.graphics.getWidth() ,Gdx.graphics.getHeight(),0,0, width, height, 1f, 2f,180, false);

                spriteBatch.end();
                
                
                if(Gdx.input.justTouched())
                        myGame.setScreen(new GameScreen());
        }
        
        @Override
        public void show()
        {
        	Texture.setEnforcePotImages(false);
                spriteBatch = new SpriteBatch();
                splsh = new Texture(Gdx.files.internal("Header.png"));
                splshreg = new TextureRegion(splsh);
        }

		@Override
		public void resize(int width, int height) {
			 this.height=height;
	         this.width=width;
			
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