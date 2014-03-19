package com.swordbit.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/** used to save game score persistently **/
public class PreferencesHelper {
	private Preferences prefs;
	private static String PREFERENCES = "eater_game_preferences";
	private static String HIGH_SCORE = "high_score";

	public PreferencesHelper() {
		prefs = Gdx.app.getPreferences(PREFERENCES);
	}

	public void saveHighScore(int score) {
		prefs.putInteger(HIGH_SCORE, score);
	}

	public int getHighScore() {
		return prefs.getInteger(HIGH_SCORE);
	}

	public void save() {
		prefs.flush();
	}
}
