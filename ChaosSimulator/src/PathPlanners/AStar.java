package PathPlanners;
import java.util.ArrayList;
import java.util.HashMap;

import WorldClasses.CityObject;
import WorldClasses.LongRoad;


public class AStar extends PathFinder{

	private ArrayList<CityObject> path = new ArrayList<CityObject>();
//	private CityObject goal = null;
	private int totalExploredNodesForAllPaths = 0;
	
	public void calculatePath(CityObject start, CityObject goal) {
		nrExploredNodes = 0;
		this.goal = goal;
		path.clear();

		ArrayList<CityObject> closedSet = new ArrayList<>();
		ArrayList<CityObject> openSet = new ArrayList<>();
		HashMap<CityObject,CityObject> came_from = new HashMap<CityObject, CityObject>(); 
		HashMap<CityObject, Integer> g_score = new HashMap<CityObject, Integer>(); 
		HashMap<CityObject, Double> f_score = new HashMap<CityObject, Double>(); 
		CityObject current = null;
		
		/* the start and goal might be something other than a crossing, update the crossingsMap accordingly */
		if(!getMap().getNodes().contains(start)){
			getMap().updateCrossings(start);
		}
		if(!getMap().getNodes().contains(goal)){
			getMap().updateCrossings(goal);
		}
		
		/* -- here begins the real code of the A* algorithm -- */
		openSet.add(start);
		g_score.put(start, 0);
		f_score.put(start, g_score.get(start) + heuristic_cost(start,goal));
		
		while(!openSet.isEmpty()){
			double lowestScore = Double.MAX_VALUE;
			
			for(CityObject node : openSet){
				
				if(f_score.get(node) < lowestScore){
					lowestScore = f_score.get(node);
					current = node;
				}
			}

			/* reconstruct path */ 
			if(current.getId() == goal.getId()){
//				System.out.println("Found a optimal path to the goal-node");
				CityObject from = came_from.get(current);
				
				while(from != null){
					//get the path to current from its predeccessor, exluding the predeccessor.
					LongRoad pathToCurrent = getMap().getLongRoad(from.getId(), current.getId());
					path.addAll(0, pathToCurrent.getPath());
					current = from;
					from = came_from.get(current);
					path.remove(0);
				}
				System.out.println("AStar " + nrExploredNodes + " in openset for " + goal.getId());
				return;
			}
			
			openSet.remove(current);
			closedSet.add(current);
			
			for(LongRoad longRoad : getMap().neighbourCrossings.get(current)) {
				CityObject neighbourCrossing = longRoad.getEnd();
				int tentative_g_score = g_score.get(current) + getMap().distanceBtwnCrossings(current.getId(), neighbourCrossing.getId());
				double tentative_f_score = tentative_g_score + heuristic_cost(neighbourCrossing, goal);

				if((closedSet.contains(neighbourCrossing) && tentative_f_score >= f_score.get(neighbourCrossing))){
//					System.out.println("aborted with continue");
					continue;
				}
					
				if(!openSet.contains(neighbourCrossing) || tentative_f_score < f_score.get(neighbourCrossing)){
//					System.out.println("didnt abort");
					came_from.put(neighbourCrossing, current);
	                g_score.put(neighbourCrossing, tentative_g_score);
	                f_score.put(neighbourCrossing, tentative_f_score);
	                if(!openSet.contains(neighbourCrossing)){
	                		openSet.add(neighbourCrossing);
	        				nrExploredNodes++;
	        				totalExploredNodesForAllPaths++;
//	                		System.out.println("added a node to the openset");
	                }
				}
			}
		}
		
	}

	/**
	 * returns the bird-distance from start to goal
	 * @param start
	 * @param goal
	 * @return
	 */
	private double heuristic_cost(CityObject start, CityObject goal) {
		int xDiff = goal.getXPos() - start.getXPos();
		int yDiff = goal.getYPos() - start.getYPos();
		return Math.sqrt(yDiff*yDiff + xDiff*xDiff);
	}

	@Override
	public ArrayList<CityObject> getPath() {
		return path;
	}


	@Override
	public void resetLocalVariables() {
		path.clear();
	}


	@Override
	public ArrayList<CityObject> getDebugData() {
		return null;
	}


	@Override
	public void handleBlockade(CityObject blockedRoad, CityObject location) {
		getMap().updateWithBlockedRoad(blockedRoad);
		getMap().updateCrossings(location);
		calculatePath(location, goal);
	}

	@Override
	public void initialize(CityObject locationRef, CityObject goalRef) {
		// TODO Auto-generated method stub
		
	}
}
