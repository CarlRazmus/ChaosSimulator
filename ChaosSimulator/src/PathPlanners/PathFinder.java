package PathPlanners;
import java.util.ArrayList;

import WorldClasses.CityObject;


public abstract class PathFinder {
	protected int nrExploredNodes = 0;
	
	private CrossingsMap map;
	protected CityObject goal = null;
	protected CityObject start = null;
	
	public abstract void calculatePath(CityObject location, CityObject target);
	public abstract ArrayList<CityObject> getPath();
	public abstract void resetLocalVariables();
	public abstract void initialize(CityObject locationRef, CityObject goalRef);
	public abstract ArrayList<CityObject> getDebugData();
	public abstract void handleBlockade(CityObject blockedRoad, CityObject location);
	
	public CrossingsMap getMap() {
		return map;
	}
	
	public void setMap(CrossingsMap map) {
		this.map = map;
	}
	
	public int getNrExploredNodes(){
		return nrExploredNodes;
	}
}
