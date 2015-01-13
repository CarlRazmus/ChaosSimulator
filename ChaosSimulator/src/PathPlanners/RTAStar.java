package PathPlanners;
import java.util.ArrayList;
import java.util.HashMap;

import WorldClasses.CityObject;
import WorldClasses.LongRoad;


public class RTAStar extends PathFinder {
	private CityObject current;
	private ArrayList<CityObject> openSet = new ArrayList<>();
	private ArrayList<CityObject> visited = new ArrayList<>();
	private HashMap<CityObject, Double> f_score = new HashMap<>();
	private ArrayList<CityObject> path = new ArrayList<>();
	private ArrayList<LongRoad> travelledPath = new ArrayList<>();
	
	public int nrExploredNodes = 0;
	
//	add current node's neighbours to openset
//	Search openNodes for the best node to move to
//		if the node isnt a neigbhbour of the current node, then backtrack
//			Backtrack
//				if the current node has the backtrackgoal as neighbour -> return the path to that neighbour
//		if the node is neighbour to current node
//			remove the node from openset and add its neigbours in openset;
//			if one of the neighbours is the goal, then add that path to the return-path
//		set current to the selected node
	
	public void initialize(CityObject startRef, CityObject goalRef){
		CityObject start = getMap().getCrossing(startRef.getId());
		CityObject goal = getMap().getCrossing(goalRef.getId());
		
		current = start;
		
		if(start == null){
			current = startRef;
			getMap().updateCrossings(startRef);
		}
		
		if(goal == null){
			getMap().updateCrossings(goalRef);
		}

		openSet.clear();
		visited.clear();
		f_score.clear();
		travelledPath.clear();
	}
	
	@Override
	public void calculatePath(CityObject locationRef, CityObject goalRef) {
		double highscore = Double.MAX_VALUE;
		CityObject bestNode = null;
		boolean found = false;
		current = getMap().getCrossing(locationRef.getId());
		CityObject goal = getMap().getCrossing(goalRef.getId());
		
		path = new ArrayList<>();
		
		visited.add(current);
		openSet.remove(current);

//		System.out.println("amount of neighbours " + getMap().getNeighbourCrossings(current.getId()).size());
		for(CityObject neighbour : getMap().getNeighbourCrossings(current.getId())){
			if(neighbour.getId() == goal.getId()){
				/* return a path to the goal */
				path = getMap().getLongRoad(current.getId(), neighbour.getId()).getPath();

				openSet.clear();
				visited.clear();
				f_score.clear();
				travelledPath.clear();
				getMap().removeTempNodes();
				current = null;
				return;
			}
			
			/* add the node to the openset and calculate its f_score */
			if(!visited.contains(neighbour) && !openSet.contains(neighbour)){
				
				nrExploredNodes++;
				
				double tentative_f_score = heuristic_cost(neighbour, goal); 
				f_score.put(neighbour, tentative_f_score);
				openSet.add(neighbour);
				
				/* if the node is better than the last */
				if(tentative_f_score < highscore){
					highscore = tentative_f_score;
					bestNode = neighbour;
					found = true;
				}
			}
		}
		
		if(found){
			double bestNodevalue = Double.MAX_VALUE;
			
			/* check if there exist an older value in openNode that is better */
			for(CityObject openNode : openSet){
				nrExploredNodes++;
				if(f_score.get(openNode) < bestNodevalue){
					bestNodevalue = f_score.get(openNode);
					bestNode = openNode;
				}
			}
			
			/* backtrack to the old node */
			if(bestNodevalue < highscore)
				backtrackTo(bestNode);
			else{
				LongRoad lr = getMap().getLongRoad(current.getId(), bestNode.getId()); 
				path = lr.getPath();
				travelledPath.add(lr);
			}
		}
		
		/* if no neighbour was found to go to, then go to the best node in openSet */
		else{
			double bestNodevalue = Double.MAX_VALUE;
			
			/* check if there exist an older value in openNode that is better */
			for(CityObject openNode : openSet){
				if(f_score.get(openNode) < bestNodevalue){
					bestNodevalue = f_score.get(openNode);
					bestNode = openNode;
				}
			}
			
			/* backtrack to the old node */
			if(bestNode != null){
				backtrackTo(bestNode);
			}
			else{
				System.out.println("no new valid node existed");
				path = null;
				return;
			}
		}
		current = bestNode;
	}
	

	private void backtrackTo(CityObject goal){
		System.out.println("checking for possible backtrack");
		ArrayList<CityObject> backtrackVisited = new ArrayList<CityObject>();
		backtrackVisited.add(current);
		path = backtrackCheck(current, goal, backtrackVisited, new ArrayList<CityObject>());
		if(path == null)
			System.out.println("didn't find a node to backtrack to");
	}
	
	private ArrayList<CityObject> backtrackCheck(CityObject currentNode, CityObject goal, ArrayList<CityObject> backtrackVisited, ArrayList<CityObject> visitedOrder){
		if(goal == null)
			return null;
		for(CityObject neighbour : getMap().getNeighbourCrossings(currentNode.getId())){
			if(!backtrackVisited.contains(neighbour) && visited.contains(neighbour)){
				nrExploredNodes++;
				
				backtrackVisited.add(neighbour);
				
				ArrayList<CityObject> newVisitedOrder = new ArrayList<CityObject>();
				newVisitedOrder.addAll(visitedOrder);
				newVisitedOrder.addAll(getMap().getLongRoad(currentNode.getId(), neighbour.getId()).getPath());
				newVisitedOrder.remove(newVisitedOrder.size() - 1);
				
				ArrayList<CityObject> answer = backtrackCheck(neighbour, goal, backtrackVisited, newVisitedOrder);
				if(answer != null)
					return answer;
			}
			else if(neighbour.getId() == goal.getId())
			{
				nrExploredNodes++;
				visitedOrder.addAll(getMap().getLongRoad(currentNode.getId(), goal.getId()).getPath());
				return visitedOrder;
			}
		}
		return null;
	}
	
	@Override
	public ArrayList<CityObject> getPath() {
		return path;
	}
	
	private double heuristic_cost(CityObject start, CityObject goal) {
		// gets the bird-distance from start to goal
		int xDiff = goal.getXPos() - start.getXPos();
		int yDiff = goal.getYPos() - start.getYPos();
		return Math.sqrt(yDiff*yDiff + xDiff*xDiff);
	}

	@Override
	public ArrayList<CityObject> getDebugData() {
		return openSet;
	}

	@Override
	public void handleBlockade(CityObject blockedRoad, CityObject location) {
		getMap().updateWithBlockedRoad(blockedRoad);
		getMap().updateCrossings(location);
		
		CityObject bestCrossing = null;
		CityObject secondBestCrossing = null;
		double bestScore = Double.MAX_VALUE;
		double secondBestScore = Double.MAX_VALUE;
		for(CityObject o : openSet){
			double score = f_score.get(o);
			
			if(score < bestScore){
				secondBestScore = bestScore;
				secondBestCrossing = bestCrossing;
				bestScore = score;
				bestCrossing = o;
			}
			else if(score < secondBestScore){
				secondBestScore = score;
				secondBestCrossing = o;
			}
		}
		openSet.remove(bestCrossing);
		current = location;
		backtrackTo(secondBestCrossing);

	}

	@Override
	public void resetLocalVariables() {
		// TODO Auto-generated method stub
		
	}
	
}
