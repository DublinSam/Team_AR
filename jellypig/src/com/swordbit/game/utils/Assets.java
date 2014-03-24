package com.swordbit.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;

public class Assets implements Disposable, AssetErrorListener {
	public static final Assets instance = new Assets();
	public static final String TAG = Assets.class.getName();
	public AssetFoods foods;
	public AssetFonts fonts;
	public AssetBackgroundLayers backgroundLayers;
	private AssetManager assetManager;

	// Assets is a singleton
	private Assets() {} 

	public void init(AssetManager manager) {
		this.assetManager = manager;
		assetManager.setErrorListener(this);
		
		assetManager.load("data/textbuttons.json", Skin.class);
		assetManager.load("images/hunger.png", Texture.class);
		
		
		assetManager.load("images/Eating.png", Texture.class);
		assetManager.load("images/JellyPig48.png", Texture.class);
		assetManager.load("images/Explosion48.png", Texture.class);
		assetManager.load("images/JellyPigSprite.png", Texture.class);
		assetManager.load("images/FatJellyPig-01.png", Texture.class);
		assetManager.load("images/AcneJellyPig-01.png", Texture.class);	
		assetManager.load("images/JellyPig_Jump48.png", Texture.class);
		assetManager.load("images/HappyJellyPig-01.png", Texture.class);
		assetManager.load("images/LemonJellyPig-01.png", Texture.class);
		assetManager.load("images/EnchiladoJellyPig-01.png", Texture.class);
		
		assetManager.load(Constants.TEXTURE_ATLAS_FOOD, TextureAtlas.class);
		assetManager.load(Constants.TEXTURE_ATLAS_BACKGROUND_LAYERS, TextureAtlas.class);
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(
										new InternalFileHandleResolver()));
		
		Gdx.app.debug(TAG,"# of assets loaded: " 
						+ assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + a);
		}
	}
	
	// Make all fonts and textures available to the program,
	// (called from the loading screen)
	public void exposeAssetClasses() {
		TextureAtlas foodAtlas = 
				assetManager.get(Constants.TEXTURE_ATLAS_FOOD);
		TextureAtlas backgroundLayersAtlas = 
				assetManager.get(Constants.TEXTURE_ATLAS_BACKGROUND_LAYERS);
		
		fonts = new AssetFonts();
		foods = new AssetFoods(foodAtlas);
		backgroundLayers = new AssetBackgroundLayers(backgroundLayersAtlas);
	}
	
	public class AssetJellypig {
		public final AtlasRegion jellypig;
		public AssetJellypig (TextureAtlas atlas) {
			jellypig = atlas.findRegion("jellypig");
		}
	}
	
	public class AssetFoods {
		public final AtlasRegion apple;
		public final AtlasRegion chili;
		public final AtlasRegion lemon;
		public final AtlasRegion pizza;
		public final AtlasRegion burger;
		public final AtlasRegion cookie;
		public final AtlasRegion hotdog;
		public final AtlasRegion strawberry;
		
		public AssetFoods(TextureAtlas atlas) {
			apple = atlas.findRegion("Apple");
			chili = atlas.findRegion("Chili");
			lemon = atlas.findRegion("Lemon");
			pizza = atlas.findRegion("Pizza");
			burger = atlas.findRegion("Burger");
			cookie = atlas.findRegion("Cookie");
			hotdog = atlas.findRegion("Hotdog");
			strawberry = atlas.findRegion("Strawberry");
		}
	}
	
	public class AssetBackgroundLayers {
		public final AtlasRegion fog;
		public final AtlasRegion sky;
		public final AtlasRegion clouds;
		public final AtlasRegion mountains;
		
		public AssetBackgroundLayers(TextureAtlas atlas) {
			fog = atlas.findRegion("Fog");
			sky = atlas.findRegion("Sky");
			clouds = atlas.findRegion("Clouds");
			mountains = atlas.findRegion("Mountains");
		}
	}
	
	public class AssetFonts {
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;

		public AssetFonts() {
			// create three fonts using Libgdx's built-in 15px bitmap font
			defaultSmall = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), false);
			defaultNormal = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), false);
			defaultBig = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), false);
			// set font sizes
			defaultSmall.setScale(0.75f);
			defaultNormal.setScale(1.0f);
			// defaultNormal.usesIntegerPositions();
			defaultBig.setScale(2.0f);
			// enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}
	
	public AssetManager getAssetManager() {
		return this.assetManager;
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
	}

	public void error(String filename, Class type,
			Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '"
			+ filename + "'", (Exception) throwable);
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {}
}