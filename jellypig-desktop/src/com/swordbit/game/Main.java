package com.swordbit.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;
import com.swordbit.game.init.GdxGame;

public class Main {
	
	//Texture packing variables
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;
			
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "jellypig";
		cfg.width = 320;
		cfg.height = 480;
			
		// Texture packing is done here for convenience
		if (rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 2048;
			settings.maxHeight = 2048;
			settings.filterMag = TextureFilter.Linear;
		    settings.filterMin = TextureFilter.Linear;
			settings.debug = drawDebugOutline;
			TexturePacker2.process(settings, 
					"assets-raw/ui-images", 
					"../jellypig-android/assets/images", 
					"jellypig-ui.pack");
			TexturePacker2.process(settings, 
					"assets-raw/loading-screen", 
					"../jellypig-android/assets/images", 
					"loading-screen.pack");
			TexturePacker2.process(settings, 
					"assets-raw/food", 
					"../jellypig-android/assets/atlas", 
					"food-textures.pack");
			TexturePacker2.process(settings, 
					"assets-raw/background-layers", 
					"../jellypig-android/assets/atlas", 
					"background-layers.pack");
			TexturePacker2.process(settings, 
					"assets-raw/jellypig", 
					"../jellypig-android/assets/atlas", 
					"jellypig-states.pack");
			
			//Animations
			TexturePacker2.process(settings, 
					"assets-raw/animation-explosion", 
					"../jellypig-android/assets/atlas", 
					"animation-explosion.pack");
			TexturePacker2.process(settings, 
					"assets-raw/animation-blinking", 
					"../jellypig-android/assets/atlas", 
					"animation-blinking.pack");
			TexturePacker2.process(settings, 
					"assets-raw/animation-eating", 
					"../jellypig-android/assets/atlas", 
					"animation-eating.pack");
			
			//Gas-meter
			TexturePacker2.process(settings, 
					"assets-raw/gas-meter", 
					"../jellypig-android/assets/atlas", 
					"gas-meter.pack");
		}
		
		new LwjglApplication(new GdxGame(), "jellpig", 800, 480);
	}
}
