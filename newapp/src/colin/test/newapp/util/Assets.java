package colin.test.newapp.util;


import java.util.List;

import colin.test.newapp.model.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.tiled.TideMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

	public static final String TAG = Assets.class.getName();

	public static final Assets instance = new Assets();
	

	private AssetManager assetManager;

	public AssetFonts fonts;



	// singleton: prevent instantiation from other classes
	private Assets () {
	}

	public class AssetFonts {
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;

		public AssetFonts () {
			
			// create three fonts using Libgdx's built-in 15px bitmap font
			defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), false);
			defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), false);
			defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), false);
			// set font sizes
			defaultSmall.setScale(0.75f);
			defaultNormal.setScale(1.0f);
			//defaultNormal.usesIntegerPositions();
			defaultBig.setScale(2.0f);
			// enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

	}
	
	
	public void init (AssetManager manager) {
		this.assetManager = manager;
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		/*
		assetManager.load("maps/level0.tmx",TiledMap.class);
		assetManager.load("maps/level1.tmx",TiledMap.class);
		assetManager.load("maps/level2.tmx",TiledMap.class);
		*/
		assetManager.load("images/LandingPage.png",Texture.class);
		assetManager.load("images/Explosion48.png", Texture.class);
		assetManager.load("images/Eating.png", Texture.class);
		assetManager.load("images/JellyPig_Jump48.png",Texture.class);
		assetManager.load("images/HappyJellyPig-01.png",Texture.class);
		assetManager.load("images/LemonJellyPig-01.png",Texture.class);
		assetManager.load("images/FatJellyPig-01.png",Texture.class);
		assetManager.load("images/AcneJellyPig-01.png",Texture.class);
		assetManager.load("images/EnchiladoJellyPig-01.png",Texture.class);
		assetManager.load("images/JellyPig48.png", Texture.class);
		assetManager.load("atlas/textures.pack", TextureAtlas.class);
		assetManager.load("images/hunger.png", Texture.class);
		assetManager.load("images/JellyPigSprite.png",Texture.class);
		

		assetManager.load("images/Mountains.png", Texture.class);
		assetManager.load("images/Sky.png", Texture.class);
		assetManager.load("images/Clouds.png", Texture.class);
		assetManager.load("images/Fog.png", Texture.class);
		assetManager.load("data/textbuttons.json",Skin.class);
	
		// set asset manager error handler
		assetManager.setErrorListener(this);
		
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + a);
		}

		// create game resource objects
		fonts = new AssetFonts();
		
		
	}

public AssetManager getAssetManager(){
	return this.assetManager;
}

	@Override
	public void dispose () {
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
	}

	public void error (String filename, Class type, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'", (Exception)throwable);
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		// TODO Auto-generated method stub
		
	}

}