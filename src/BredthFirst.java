import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class BredthFirst extends PathFinder {

	//must check for blockades later
	private ArrayList<CityObject> path = new ArrayList<CityObject>();
	
	public void calculatePath(CityObject location, CityObject target) {
		
		LinkedList<CityObject> queue = new LinkedList<CityObject>(); 
		ArrayList<CityObject> visited = new ArrayList<CityObject>();
		HashMap<CityObject, CityObject> parentMap = new HashMap<CityObject, CityObject>(); 
		CityObject current;
		boolean foundTarget = false;
		
		queue.add(location);
		visited.add(location);
		
		while(queue.size() > 0){
			CityObject t = queue.pollFirst();
			if(t == target){
				foundTarget = true;
				break;
			}else{
				for (CityObject child : t.getNeighbours()){
					if(child instanceof Road && !visited.contains(child)){
						parentMap.put(child, t);
						visited.add(child);
						queue.addLast(child);
					}
				}
			}
		}
		if(!foundTarget)
			path = null;
		
		current = target;
		while(true){
			if(current == location)
				break;
			path.add(0, current);
			current = parentMap.get(current);
			
		}	
	}

	@Override
	public ArrayList<CityObject> getPath() {
		return path;
	}

	@Override
	public void initialize(CityObject start, CityObject goal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<CityObject> getDebugData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleBlockade(CityObject blockedRoad, CityObject location) {
		// TODO Auto-generated method stub
		
	}

}
