package com.swordbit.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class Main {
	
	//Texture packing variables
	private static boolean rebuildAtlas = true;
	private static boolean drawDebugOutline = false;
			
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "jellypig";
		cfg.useGL20 = false;
		cfg.width = 320;
		cfg.height = 480;
			
		// Texture packing is done here for convenience
		if (rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 2048;
			settings.maxHeight = 1024;
			settings.debug = drawDebugOutline;
			TexturePacker2.process(settings, 
					"assets-raw/images-ui", 
					"../jellypig-android/assets/images", 
					"jellypig-ui.pack");
			TexturePacker2.process(settings, 
					"assets-raw/loading-screen", 
					"../jellypig-android/assets/images", 
					"loading-screen.pack");
			TexturePacker2.process(settings, 
					"assets-raw/food-assets", 
					"../jellypig-android/assets/atlas", 
					"food-textures.pack");
		}
		
		new LwjglApplication(new GdxGame(), "jellpig", 800, 480, true);
	}
}
