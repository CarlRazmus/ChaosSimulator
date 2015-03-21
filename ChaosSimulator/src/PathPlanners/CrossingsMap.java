package PathPlanners;
import java.util.ArrayList;
import java.util.HashMap;

import org.omg.CosNaming.NamingContextPackage.NotFound;

import Default.Helper;
import WorldClasses.CityObject;
import WorldClasses.Crossing;
import WorldClasses.LongRoad;
import WorldClasses.Road;


public class CrossingsMap {
	
	/* NOTE!!  a crossing can be a building since sometimes the goal of a path might be a building   */ 
	
	public ArrayList<Crossing> crossings = new ArrayList<Crossing>();
	private ArrayList<Crossing> fakeCrossings = new ArrayList<Crossing>();

	
	
	
	private String debugString = "";
	
	public void createDebugString(){
		String lastDebugString = debugString;
		debugString = "";
		for(Crossing c : crossings){
			debugString += "Crossing " + c.getRoad().getId() + "\n   ";
			for(LongRoad lr : c.getNeighbourCrossings()){
				if(!isCrossing(lr.getEnd()))
					System.out.println("crossing " + c.getRoad().getId() + " has a corrupted LongRoad now");
				if(!isCrossing(lr.getStart()))
					System.out.println("crossing " + c.getRoad().getId() + " has a corrupted LongRoad now");
				debugString += lr.getEnd().getId() + ", ";
			}
			debugString += "\n";	
		}
		
		if(lastDebugString!= "")
			if(!lastDebugString.equals(debugString))
				System.out.println("Map is not the same as before!!");
	}
	
	public void printDebugString(){
		System.out.println(debugString);
	}
	
	
	
	

	private void addCrossing(Crossing crossing){
//		System.out.println("adding a new road to the crossings map " + road.getId());
		crossings.add(crossing);
	}
	
	
	public boolean isCrossing(CityObject road){
		for(Crossing c : crossings)
			if(c.getRoad().getId() == road.getId())
				return true;
		return false;
	}
	
	public void updateWithBlockedRoad(CityObject blockedRoad) throws Exception{
		System.out.println("road (id=" + blockedRoad.getId() + ") was blocked, updates neighbour Crossings with new neighbourArrayLists");
		
		if(!blockedRoad.isBlocked())
			throw new Exception();
		
		/* check if the road is a crossing, if yes then reset its neighbor list */
		if(isCrossing(blockedRoad))
			getCrossing(blockedRoad.getId()).getNeighbourCrossings().clear();
		
		recalculateAllNeighborCrossings(blockedRoad);
	}
	
	public Crossing getCrossing(int id) throws NotFound{
		for(Crossing c : crossings)
			if(c.getRoad().getId() == id)
				return c;
		
		throw new NotFound();
	}
	
	public void addTemporaryCrossing(CityObject road){
//		System.out.println("updates the crossingsMap with a new crossing " + road.getId());
		
		if(!Helper.assertRoad(road)) return;
		
		if(isCrossing(road)){
//			System.out.println("tried to add an already existing crossing");
			return;
		}
		
		Crossing fakeCrossing = new Crossing(road);
		addCrossing(fakeCrossing);

		fakeCrossings.add(fakeCrossing);
		findCrossingNeighbours(fakeCrossing);
		
		recalculateAllNeighborCrossings(road);
		
		checkCrossingCorruption();
	}
	
	
	private void recalculateAllNeighborCrossings(CityObject road){
				
		//update neighbor-lists of all crossings that are connected to the road/crossing
		for(CityObject neighbourRoad : road.getNeighbours()){
			
			if(neighbourRoad instanceof Road){
				try{
					Crossing neighborCrossing = checkForCrossing(road, neighbourRoad);				
					findCrossingNeighbours(neighborCrossing);
				}
				catch(Exception e){
				}
			}
		}
	}

	private Crossing checkForCrossing(CityObject road, CityObject neighbour ) throws NotFound {
		if(neighbour.isBlocked()) throw new NotFound();
		
		if(isCrossing(neighbour))
			return getCrossing(neighbour.getId());
		
		for(CityObject o : neighbour.getNeighbours())
			if(o instanceof Road && o != road)
				return checkForCrossing(neighbour, o);	
		
		throw new NotFound();
	}
		
	public void initializeCrossingsNeighbours(){
		for(Crossing c : crossings)
			findCrossingNeighbours(c);
	}

	private void findCrossingNeighbours(Crossing crossing){
		CityObject road = crossing.getRoad();
		
		crossing.getNeighbourCrossings().clear();
		
		for(CityObject neighbour : road.getNeighbours())
			if(neighbour instanceof Road){
				//finds a longroad to the nearest crossing (if it exists)
				try{
					LongRoad longRoad = checkForLongRoad(road, neighbour);
					addNeighbour(crossing, longRoad);
				}
				catch(Exception e){
					
				}
			}
	}
	
	private LongRoad checkForLongRoad(CityObject start, CityObject firstRoad) throws Exception{
		LongRoad longRoad = new LongRoad();
		CityObject recentlyVisited = start;
		CityObject currentRoad = firstRoad;
		boolean found;
		
		longRoad.addRoad(start);
		
		while(true){
			if(currentRoad.isBlocked()) throw new Exception();
			
			found = false;
			longRoad.addRoad(currentRoad);
			
			for(Crossing c : crossings)
				if(c.getRoad().getId() == currentRoad.getId())
					return longRoad;

			for(CityObject neighbour : currentRoad.getNeighbours()){
				if(neighbour instanceof Road && neighbour != recentlyVisited){
					recentlyVisited = currentRoad;
					currentRoad = neighbour;
					found = true;
					break;
				}
			}
			
			if(!found) throw new Exception();				
		}
	}
	

	
	public LongRoad getLongRoad(int id1, int id2) throws NotFound{
		Crossing crossing1 = getCrossing(id1);
		Crossing crossing2 = getCrossing(id2);
		
		for(LongRoad lr : crossing1.getNeighbourCrossings())
			if(lr.getEnd().getId() == crossing2.getRoad().getId())
				return lr;
		
		throw new NotFound();
	}
	
	public boolean checkCrossingCorruption(){
		for(Crossing o : crossings){
			for(LongRoad lr : o.getNeighbourCrossings()){
				if(lr.getPath().size() == 0){
					System.out.print("corrupted crossing " + o.getRoad().getId());
					return true;
				}
			}
		}
		return false;
	}
	
	
	private void addNeighbour(Crossing crossing, LongRoad neighbourPath){
		crossing.getNeighbourCrossings().add(neighbourPath);
	}
	
	
	public int distanceBtwnCrossings(int startID, int goalID) throws NotFound{
		Crossing start = getCrossing(startID);
		CityObject goal = getCrossing(goalID).getRoad();
		
	
		ArrayList<LongRoad> longRoadList = start.getNeighbourCrossings();
		for(LongRoad longRoad : longRoadList)
			if(longRoad.getEnd() == goal){
				return longRoad.getDistance();
			}
		
		System.out.println("ErrorMessage: It didn't exist a longroad between the nodes");
		return 0;
	}
	
	public ArrayList<Crossing> getNodes(){
		return crossings;
	}
	
	public void setCrossings(ArrayList<Road> crossings){
		for(CityObject road : crossings){
			Crossing crossing = new Crossing(road);
			addCrossing(crossing);
		}
		System.out.println("done initializing the CrossingsMap\n");
	}
	
	public void removeTempNodes(){
//		System.out.println("removes temporary nodes");
		for(Crossing crossing : fakeCrossings){
			removeNode(crossing);	
		}
		fakeCrossings.clear();
	}		
	
	/* removes the node and calculates values for the neigbours of that node again  */
	private void removeNode(Crossing node){	
		CityObject road = node.getRoad();

		crossings.remove(node);
		
		recalculateAllNeighborCrossings(road);
		
		checkCrossingCorruption();
	}
	
	
	
	public ArrayList<Crossing> getNeighbourCrossings(int id) throws NotFound{
		ArrayList<Crossing> output = new ArrayList<Crossing>();

		for(LongRoad lr : getCrossing(id).getNeighbourCrossings())
			output.add(getCrossing(lr.getEnd().getId()));
		
		return output;
	}
	
}

