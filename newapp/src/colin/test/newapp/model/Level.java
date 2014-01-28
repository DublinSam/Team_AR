package colin.test.newapp.model;

import colin.test.newapp.util.Assets;

import com.badlogic.gdx.maps.tiled.TiledMap;
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
	levelName="Level "+i;
	map=Assets.instance.getAssetManager().get("maps/level"+i+".tmx",TiledMap.class);
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
}
