import java.util.ArrayList;
import java.util.HashMap;


public class CrossingsMap {
	
	/* NOTE!!  a crossing can be a building since sometimes the goal of a path might be a building   */ 
	
	HashMap<CityObject, ArrayList<LongRoad>> neighbourCrossings = new HashMap<CityObject, ArrayList<LongRoad>>();
	ArrayList<CityObject> crossings = new ArrayList<>();
	private ArrayList<CityObject> resetCrossingsList = new ArrayList<>();
	private ArrayList<CityObject> blockedRoads = new ArrayList<>();
	
	private void addCrossing(CityObject road){
		neighbourCrossings.put(road, new ArrayList<LongRoad>());
		crossings.add(road);
	}
	
	
	public void updateWithBlockedRoad(CityObject blockedRoad){

		System.out.println("updates the neighbourCrossings with a blocked road");
		/* check if the road is a crossing */
		if(crossings.contains(blockedRoad))
			System.out.println("the road that was blocked was a crossing");
		
		ArrayList<CityObject> visited = new ArrayList<>();
		
		//1. find the two crossings(can be a single crossing too) that the new node intercepts
		visited.add(blockedRoad);
		blockedRoads.add(blockedRoad);
		for(CityObject neighbour : blockedRoad.getNeighbours()){
			if(neighbour instanceof Road){
				CityObject crossing = checkForRoad(neighbour, visited);
				neighbourCrossings.put(crossing, new ArrayList<LongRoad>());
				findCrossingNeighbours(crossing);
			}
		}
	}
	
	public void updateCrossings(CityObject newRoad){
		System.out.println("updates the crossingsMap with a new crossing");
		addCrossing(newRoad);
		
		ArrayList<CityObject> visited = new ArrayList<>();
		
		//1. find the two crossings(can be a single crossing too) that the new node intercepts
		visited.add(newRoad);
		resetCrossingsList.add(newRoad);
		findCrossingNeighbours(newRoad);
		for(CityObject neighbour : newRoad.getNeighbours()){
			if(neighbour instanceof Road){
				CityObject crossing1 = checkForRoad(neighbour, visited);
				neighbourCrossings.put(crossing1, new ArrayList<LongRoad>());
				findCrossingNeighbours(crossing1);
			}
		}
	}

	private CityObject checkForRoad(CityObject crossing, ArrayList<CityObject> visited) {
		boolean deadEnd = false;
		
		while(deadEnd == false){
			deadEnd = true;
			
			for(CityObject o : crossings){
				if(o.getId() == crossing.getId())
					return o;
			}
			
			for(CityObject n : crossing.getNeighbours())
				if(n instanceof Road && !visited.contains(n)){
					deadEnd = false;
					visited.add(crossing);
					crossing = n;
					break;
				}
		}
		return null;
	}
		
	public void findCrossingsNeighbours(){
		for(CityObject crossing : crossings)
			findCrossingNeighbours(crossing);
	}

	private void findCrossingNeighbours(CityObject crossing){
		for(CityObject neighbour : crossing.getNeighbours())
			if(neighbour instanceof Road){
				//finds a longroad to the nearest crossing (if it exists)
				LongRoad longRoad = findNearestCrossing(crossing, neighbour);
				if(longRoad != null)
					addNeighbour(crossing, longRoad);
			}
	}
	
	private LongRoad findNearestCrossing(CityObject start, CityObject firstRoad){
		LongRoad longRoad = new LongRoad();
		CityObject recentlyVisited = start;
		CityObject currentRoad = firstRoad;
		boolean found;
		
		longRoad.addRoad(start);
		
		while(true){
			if(blockedRoads.contains(currentRoad))
				return null;
			
			found = false;
			longRoad.addRoad(currentRoad);
			for(CityObject o : crossings){
				if(o.getId() == currentRoad.getId())
					return longRoad;
			}
//			if(nodes.contains(currentRoad)){
//				return longRoad;
//			}
			for(CityObject neighbour : currentRoad.getNeighbours()){
				if(neighbour instanceof Road && neighbour != recentlyVisited){
					recentlyVisited = currentRoad;
					currentRoad = neighbour;
					found = true;
					break;
				}
			}
			if(!found){
//				System.out.println("Dead End");
				return null;
			}
		}
	}
	

	
	public LongRoad getLongRoad(Integer id1, Integer id2){
		CityObject crossing1 = getCrossing(id1);
		CityObject crossing2 = getCrossing(id2);
		
		if(crossing1 == null || crossing2 == null)
			System.out.println("nodes didnt contain node 1 or 2");
		for(LongRoad lr : neighbourCrossings.get(crossing1))
			if(lr.getEnd().getId() == crossing2.getId())
				return lr;
		return null;
	}
	
	public boolean checkCrossingCorruption(){
		for(CityObject o : crossings){
			for(LongRoad lr : neighbourCrossings.get(o)){
				if(lr.getPath().size() == 0)
					return true;
			}
		}
		return false;
	}
	
	private void addNeighbour(CityObject road, LongRoad crossingNeighbour){
		if(neighbourCrossings.containsKey(road))
			neighbourCrossings.get(road).add(crossingNeighbour);
	}
	
	public ArrayList<CityObject> getNeighbourCrossings(Integer crossingID){
		ArrayList<CityObject> roadList = new ArrayList<>();
		CityObject crossing = getCrossing(crossingID);
		
		if(crossing == null)
			return null;
		
		for(LongRoad longRoad : neighbourCrossings.get(crossing)){
			if(longRoad.getPath().size() > 0)
				roadList.add(longRoad.getEnd());
		}
		return roadList;
	}
	
	public int distanceBtwnCrossings(int startID, int goalID){
		CityObject start = getCrossing(startID);
		CityObject goal = getCrossing(goalID);
		
		if(start == null || goal == null){
			System.out.println("ERROR MESSAGE: one of the nodes wasn't a crossing");
			return 0;
		}
	
		ArrayList<LongRoad> longRoadList = neighbourCrossings.get(start);
		for(LongRoad longRoad : longRoadList)
			if(longRoad.getEnd() == goal){
				return longRoad.getDistance();
			}
		
		System.out.println("ErrorMessage: It didn't exist a longroad between the nodes");
		return 0;
	}
	
	public ArrayList<CityObject> getNodes(){
		return crossings;
	}
	
	public void setCrossings(ArrayList<Road> crossings){
		for(CityObject crossing : crossings){
			addCrossing(crossing);
		}
	}
	
	public void removeTempNodes(){
		for(CityObject crossing : resetCrossingsList){
			removeNode(crossing);	
		}
		resetCrossingsList.clear();
	}		
	
	/* remove the node and calculates values for the neigbours of that node again  */
	public void removeNode(CityObject node){	
//		nodes.remove(node);
		removeNode(node.getId());
		removeNeighbourCrossings(node.getId());
//		neighbourCrossings.remove(node);
		
		CityObject crossing1 = null;
		CityObject crossing2 = null;
		ArrayList<CityObject> visited = new ArrayList<>();
		
		//1. find the two crossings(can be a single crossing too) that the new node intercepts
		visited.add(node);
		for(CityObject neighbour : node.getNeighbours()){
			if(neighbour instanceof Road){
				if(crossing1 == null)
					crossing1 = neighbour;
				else
					crossing2 = neighbour;
			}
		}
		if(crossing1 != null)
			crossing1 = checkForRoad(crossing1, visited);
		
		if(crossing2 != null)
			crossing2 = checkForRoad(crossing2, visited);
		
		//2. remove the old values from the crossings and update them with new ones.
		if(crossing1 != null){
			neighbourCrossings.put(crossing1, new ArrayList<LongRoad>());
			findCrossingNeighbours(crossing1);
		}
		
		if(crossing2 != null){
			neighbourCrossings.put(crossing2, new ArrayList<LongRoad>());
			findCrossingNeighbours(crossing2);
		}
	}
	
	public CityObject getCrossing(int id){
		for(CityObject o : crossings){
			if(o.getId() == id)
				return o;
		}
		return null;
	}
	
	public void removeNode(int id){
		ArrayList<CityObject> remove = new ArrayList<>();
		for(CityObject o : crossings)
			if(o.getId() == id)
				remove.add(o);
		for(CityObject o : remove)
			crossings.remove(o);
	}
	
	public ArrayList<CityObject> getNeighbourCrossings(int id){
		for(CityObject o : neighbourCrossings.keySet())
			if(o.getId() == id){
				ArrayList<CityObject> list = new ArrayList<>();
				for(LongRoad lr : neighbourCrossings.get(o))
					list.add(lr.getEnd());
				return list;
			}
		return null;
	}
	
	public void removeNeighbourCrossings(int id){
		ArrayList<CityObject> remove = new ArrayList<>();
		for(CityObject o : neighbourCrossings.keySet())
			if(o.getId() == id)
				remove.add(o);
		for(CityObject o : remove)
			neighbourCrossings.remove(o);
	}
	
	
}
