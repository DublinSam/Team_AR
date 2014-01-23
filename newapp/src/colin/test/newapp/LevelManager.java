package colin.test.newapp;

public class LevelManager {
	boolean levelCompleted=false;
	public enum Level{
		LEVEL1("maps/longerTestMap.tmx");
		String mapName;
		Level(String location){
			mapName=location;
		}
	 public  String getFileString(){
			return this.mapName;
		}
	}
public LevelManager(){
	currentLevelIndex=0;
	level=Level.values()[currentLevelIndex];
}
	int currentLevelIndex;
	Level level;
public void loadMap(){

}
public Level getCurrentLevel(){
return level;
}
public void nextLevel(){
	level= Level.values()[currentLevelIndex++];
	this.levelCompleted=false;
}
public void levelSuccesfull() {
	this.levelCompleted=true;
	
}
public boolean getLevelSuccessfull(){
	return this.levelCompleted;
}
}
