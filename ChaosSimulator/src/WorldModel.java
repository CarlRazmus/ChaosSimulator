import java.util.ArrayList;
import java.util.HashMap;

public class WorldModel {	
	private HashMap<Integer, Road> roadMap;
	private HashMap<Integer, Building> buildingsMap;
	
	private ArrayList<Building> buildings = new ArrayList<>();
	private ArrayList<Road> roads = new ArrayList<>();
	private ArrayList<CityObject> crossings = new ArrayList<>(); // only here for camera debug purposes
	
	public WorldModel() {
		initialize();
	}
	
	public void updateScaleOnObjects(){
		for(Building b : buildings)
			b.updatePos();
		for(Road r : roads)
			r.updatePos();			
	}
	
	public void initialize(){
		roadMap = new HashMap<Integer, Road>();
		buildingsMap = new HashMap<Integer, Building>();
//		crossingsMap = new HashMap<Integer, CityObject>();
		setBuildings(new ArrayList<Building>());
		setRoads(new ArrayList<Road>());
	}

	

	public void setRoads(ArrayList<Road> roads) {
		this.roads = roads;
		for(Road road : roads)
			roadMap.put(road.getId(), road);
	}

	public void setBuildings(ArrayList<Building> buildings) {
		this.buildings = buildings;
		for(Building building : buildings)
			buildingsMap.put(building.getId(), building);
	}
	
	public ArrayList<Road> getRoads() {
		return roads;
	}
	public ArrayList<Building> getBuildings() {
		return buildings;
	}


	public CityObject getCityObject(int id) {
		CityObject o = null;
		o = roadMap.get(id);
		if(o != null)
			return o;
		
		o = buildingsMap.get(id);
		if(o != null)
			return o;
		return null;
	}

	public Road getRoad(int id) {
		return roadMap.get(id);
	}
	
	public Building getBuilding(int id){
		return buildingsMap.get(id);
	}

	public ArrayList<CityObject> getCrossings() {
		return crossings;
	}

	public void addCrossings(ArrayList<Road> crossings) {
		this.crossings.addAll(crossings);
	}
}
