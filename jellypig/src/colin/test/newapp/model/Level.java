package colin.test.newapp.model;

import colin.test.newapp.util.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;

public class Level {
TiledMap map;
String LevelLocation;
Vector2 eaterPosition;
LevelStatus levelStatus;
Eater eater;
String levelName;
enum LevelStatus{
	COMPLETED, FAILED, INPROGRESS
}
public Level(int i){
	Assets.instance.getAssetManager().load("maps/level"+i+".tmx",TiledMap.class);
	Assets.instance.getAssetManager().finishLoading();
	int levelNumber=i+1;
	levelName="Level "+levelNumber;
	LevelLocation="maps/level"+i+".tmx";
	this.eater=new Eater(new Vector2(3.5f,2.2f));
}
protected TiledMap getMap(){
	return this.map;
}
protected boolean isLevelCompleted() {
	return levelStatus==LevelStatus.COMPLETED;
}
protected void levelCompleted(){
	this.levelStatus=LevelStatus.COMPLETED;
}
public void levelFailed() {
this.levelStatus=LevelStatus.FAILED;
	
}
public String getLevelName(){
	return this.levelName;
}
public void loadMap(String level) {

	map=Assets.instance.getAssetManager().get(level,TiledMap.class);
	
}

}
