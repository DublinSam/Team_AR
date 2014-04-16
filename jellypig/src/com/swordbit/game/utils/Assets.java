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
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.swordbit.game.model.GameMusic;

public class Assets implements Disposable, AssetErrorListener {
	public static final Assets instance = new Assets();
	public static final String TAG = Assets.class.getName();
	public AssetFoods foods;
	public AssetFonts fonts;
	public AssetJellypigStates jellypigStates;
	public AssetBackgroundLayers backgroundLayers;
	public AssetEatingAnimation eatingAnimation;
	public AssetBlinkingAnimation blinkingAnimation;
	public AssetExplosionAnimation explosionAnimation;
	
	public AssetGasMeter gasmeter;
	
	public AssetSounds sounds;
	public AssetMusic music;
	private AssetManager assetManager;

	// Assets is a singleton
	private Assets() {} 

	public void init(AssetManager manager) {
		this.assetManager = manager;
		assetManager.setErrorListener(this);
		
		assetManager.load("data/textbuttons.json", Skin.class);
		assetManager.load("images/hunger.png", Texture.class);
		
		//Animation Textures
		/*
		assetManager.load("images/Eating.png", Texture.class);
		assetManager.load("images/Explosion48.png", Texture.class);
		assetManager.load("images/JellyPigSprite.png", Texture.class);
		*/
		
		assetManager.load(Constants.TEXTURE_ATLAS_EATING_ANIMATION, TextureAtlas.class);
		assetManager.load(Constants.TEXTURE_ATLAS_BLINKING_ANIMATION, TextureAtlas.class);
		assetManager.load(Constants.TEXTURE_ATLAS_EXPLOSION_ANIMATION, TextureAtlas.class);
		
		assetManager.load(Constants.TEXTURE_ATLAS_FOOD, TextureAtlas.class);
		assetManager.load(Constants.TEXTURE_ATLAS_JELLYPIG_STATES, TextureAtlas.class);
		assetManager.load(Constants.TEXTURE_ATLAS_BACKGROUND_LAYERS, TextureAtlas.class);
		
		//Gas-meter
		assetManager.load(Constants.TEXTURE_ATLAS_GAS_METER, TextureAtlas.class);
		
		assetManager.load("sounds/jump.wav", Sound.class);
		assetManager.load("sounds/play.wav", Sound.class);
		assetManager.load("sounds/transformation.wav", Sound.class);
		assetManager.load("sounds/fart.wav", Sound.class);
		assetManager.load("music/soundtrack.mp3", Music.class);
		
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(
										new InternalFileHandleResolver()));
		
		Gdx.app.debug(TAG,"# of assets loaded: " 
						+ assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + a);
		}
	}
	
	// Make all fonts, textures, sounds and music available to the program,
	// (called from the loading screen)
	public void exposeAssetClasses() {
		TextureAtlas foodAtlas = 
				assetManager.get(Constants.TEXTURE_ATLAS_FOOD);
		TextureAtlas jellypigAtlas =
				assetManager.get(Constants.TEXTURE_ATLAS_JELLYPIG_STATES);
		TextureAtlas backgroundLayersAtlas = 
				assetManager.get(Constants.TEXTURE_ATLAS_BACKGROUND_LAYERS);
		TextureAtlas explosionAnimationAtlas = 
				assetManager.get(Constants.TEXTURE_ATLAS_EXPLOSION_ANIMATION);
		TextureAtlas eatingAnimationAtlas = 
				assetManager.get(Constants.TEXTURE_ATLAS_EATING_ANIMATION);
		TextureAtlas blinkingAnimationAtlas = 
				assetManager.get(Constants.TEXTURE_ATLAS_BLINKING_ANIMATION);
		
		//Gas-meter
		TextureAtlas gasMeterAtlas = 
				assetManager.get(Constants.TEXTURE_ATLAS_GAS_METER);
		
		fonts = new AssetFonts();
		foods = new AssetFoods(foodAtlas);
		jellypigStates = new AssetJellypigStates(jellypigAtlas);
		backgroundLayers = new AssetBackgroundLayers(backgroundLayersAtlas);
		eatingAnimation = new AssetEatingAnimation(eatingAnimationAtlas);
		blinkingAnimation = new AssetBlinkingAnimation(blinkingAnimationAtlas);
		explosionAnimation = new AssetExplosionAnimation(explosionAnimationAtlas);		
		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
		
		gasmeter = new AssetGasMeter(gasMeterAtlas);
	}
	
	public class AssetJellypigStates {
		
		public final AtlasRegion fat;
		public final AtlasRegion sour;
		public final AtlasRegion acne;
		public final AtlasRegion happy;
		public final AtlasRegion jumping;
		public final AtlasRegion jellypig;
		public final AtlasRegion enchilado;
		
		
		public AssetJellypigStates (TextureAtlas atlas) {	
			fat = atlas.findRegion("Fatty");
			acne = atlas.findRegion("Acne");
			sour = atlas.findRegion("Lemon");
			happy = atlas.findRegion("Happy");
			jumping = atlas.findRegion("JellyPigJumping");
			jellypig = atlas.findRegion("JellyPig48");
			enchilado = atlas.findRegion("Enchilado");
		}
	}
	
	public class AssetGasMeter{
		public final AtlasRegion loadingFrame;
		public final AtlasRegion loadingBg;
		public final AtlasRegion loadingBar;
		
		
		public AssetGasMeter (TextureAtlas atlas) {	
			loadingFrame = atlas.findRegion("loading-frame");
			loadingBg = atlas.findRegion("loading-frame-bg");
			loadingBar = atlas.findRegion("loading-bar");
		}
	}
	
	/*Jesus - Classes for sound effects and music*/
	public class AssetSounds{
		public final Sound jump;
		public final Sound play;
		public final Sound transformation;
		public final Sound fart;
		public AssetSounds(AssetManager assetmanager){
			jump = assetmanager.get("sounds/jump.wav", Sound.class);
			play = assetmanager.get("sounds/play.wav", Sound.class);
			fart = assetmanager.get("sounds/fart.wav", Sound.class);
			transformation = assetmanager.get("sounds/transformation.wav", Sound.class);
		}
	}
	
	public class AssetMusic{
		public final Music soundtrack;
		public AssetMusic(AssetManager assetmanager){
			soundtrack = assetmanager.get("music/soundtrack.mp3", Music.class);
		}
	}
	
 	public class AssetExplosionAnimation {
		public final AtlasRegion[] explosionFrames;
		
		public AssetExplosionAnimation(TextureAtlas atlas) {
			explosionFrames = new AtlasRegion[3];
			explosionFrames[0] = atlas.findRegion("Explosion", 1);
			explosionFrames[1] = atlas.findRegion("Explosion", 2);
			explosionFrames[2] = atlas.findRegion("Explosion", 3);
		}
	}
	
	public class AssetEatingAnimation {
		public final AtlasRegion[] eatingFrames;
		
		public AssetEatingAnimation(TextureAtlas atlas) {
			eatingFrames = new AtlasRegion[6];
			eatingFrames[0] = atlas.findRegion("Eating", 1);
			eatingFrames[1] = atlas.findRegion("Eating", 2);
			eatingFrames[2] = atlas.findRegion("Eating", 3);
			eatingFrames[3] = atlas.findRegion("Eating", 4);
			eatingFrames[4] = atlas.findRegion("Eating", 5);
			eatingFrames[5] = atlas.findRegion("Eating", 6);
		}
	}
	
	public class AssetBlinkingAnimation {
		public final AtlasRegion[] blinkingFrames;
		
		public AssetBlinkingAnimation(TextureAtlas atlas) {
			blinkingFrames = new AtlasRegion[5];
			blinkingFrames[0] = atlas.findRegion("Blinking", 1);
			blinkingFrames[1] = atlas.findRegion("Blinking", 2);
			blinkingFrames[2] = atlas.findRegion("Blinking", 3);
			blinkingFrames[3] = atlas.findRegion("Blinking", 4);
			blinkingFrames[4] = atlas.findRegion("Blinking", 5);
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
		public final AtlasRegion hills;
		
		public AssetBackgroundLayers(TextureAtlas atlas) {
			fog = atlas.findRegion("Fog");
			sky = atlas.findRegion("Sky");
			clouds = atlas.findRegion("Clouds");
			mountains = atlas.findRegion("Mountains");
			hills = atlas.findRegion("Hills");
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