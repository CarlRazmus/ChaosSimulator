package WorldClasses;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import Default.Camera;
import Default.CityDataReader;


public class CityObject {
	private int x;
	private int y;
	private int xPos;
	private int yPos;
	private ArrayList<CityObject> neighbours;
	private FierynessLevel fierynessLevel;
	private int id;
	private boolean blocked = false;
	private Color color;
	private Color originalColor;
	
	//private static final int FIERYNESS_CYCLE_COUNT_UP = 10;
	
	enum FierynessLevel{
		none,
		low,
		medium,
		high
	};
	
	public CityObject(){
		initializeStuff();
	}
	
	public CityObject(Color color){
		this.color = color;
		this.originalColor = color;
		initializeStuff();
	}
	
	public CityObject(int x, int y, Color color){
		this.x = x;
		this.y = y;
		this.color = color;
		this.originalColor = color;
		xPos = x*Camera.scale;
		yPos = y*Camera.scale;
		initializeStuff();
	}
	
	private void initializeStuff(){
		neighbours = new ArrayList<CityObject>();
		this.id = CityDataReader.IDCount++;
		fierynessLevel = FierynessLevel.values()[0];
	}
	
	public int getXPos(){
		return this.xPos;
	}
	
	public int getYPos(){
		return this.yPos;
	}
	
	public void setX(int x){
		this.x = x;
		this.xPos = x*Camera.scale;
	}
	
	public void setY(int y){
		this.y = y;
		this.yPos = y*Camera.scale;
	}
	
	public void updatePos(){
		xPos = x*Camera.scale;
		yPos = y*Camera.scale;
	}
	
	public ArrayList<CityObject> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(ArrayList<CityObject> neighbours) {
		this.neighbours = neighbours;
	}

	public FierynessLevel getFierynessLevel() {
		return fierynessLevel;
	}

	public void setFierynessLevel(FierynessLevel fierynessLevel) {
		this.fierynessLevel = fierynessLevel;
	}
	
	public void increaseFierynessLevel(){
		int index = Arrays.asList(FierynessLevel.values()).indexOf(fierynessLevel);
		if(index < FierynessLevel.values().length - 1)
			fierynessLevel = FierynessLevel.values()[index + 1];
	}

	public int getId() {
		return id;
	}


	public boolean isBlocked() {
		return blocked;
	}

	public void block() {
		this.blocked = true;
		color = Color.darkGray;
	}
	
	public void unBlock(){
		blocked = false;
		color = originalColor;
	}

	public Color getColor() {
		return color;
	}
	
	public void exposeToFire(double fireynessLevel){
		
	}
	
//
//	public void setColor(Color color) {
//		this.color = color;
//	}
	
}
