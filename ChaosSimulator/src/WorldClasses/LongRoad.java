package WorldClasses;
import java.util.ArrayList;

public class LongRoad {

	private ArrayList<CityObject> path;
	
	
	public LongRoad() {
		path = new ArrayList<CityObject>();
	}
	
	public LongRoad(ArrayList<CityObject> path){
		this.path = path;
	}
	
	public ArrayList<CityObject> getPath(){
		return path;
	}
	
	public CityObject getStart() {
		return path.get(0);
	}

	public CityObject getEnd() {
		return path.get(path.size() - 1);
	}

	public int getDistance() {
		return path.size();
	}
	
	public void addRoad(CityObject road){
		path.add(road);
	}
}
