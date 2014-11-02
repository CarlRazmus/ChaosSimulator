import java.util.ArrayList;
import java.util.Arrays;


public class CityObject {
	private int x;
	private int y;
	private int xPos;
	private int yPos;
	private ArrayList<CityObject> neighbours;
	private FierynessLevel fierynessLevel;
	private int id;
	private boolean blocked = false;
	
	private static final int FIERYNESS_CYCLE_COUNT_UP = 10;
	
	enum FierynessLevel{
		low,
		medium,
		high
	};
	
	public CityObject(){
		neighbours = new ArrayList<CityObject>();
		this.id = CityDataReader.IDCount++;
	}
	
	public CityObject(int x, int y){
		this.x = x;
		this.y = y;
		xPos = x*Camera.scale;
		yPos = y*Camera.scale;
		neighbours = new ArrayList<CityObject>();
		this.id = CityDataReader.IDCount++;
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
		if(fierynessLevel != null){
			int index = Arrays.asList(FierynessLevel.values()).indexOf(fierynessLevel);
			if(index < FierynessLevel.values().length - 1)
				fierynessLevel = FierynessLevel.values()[index + 1];
		}
		else
			fierynessLevel = FierynessLevel.values()[0];
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	
}
