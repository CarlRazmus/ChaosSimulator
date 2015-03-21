package PathPlanners;
import java.util.ArrayList;
import java.util.HashMap;

import org.omg.CosNaming.NamingContextPackage.NotFound;

import WorldClasses.CityObject;
import WorldClasses.Crossing;
import WorldClasses.LongRoad;


public class RTAStar extends PathFinder {
	private Crossing current;
	private ArrayList<Crossing> openSet = new ArrayList<>();
	private ArrayList<Crossing> visited = new ArrayList<>();
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
		start = startRef;
		goal = goalRef;
	}
	
	@Override
	public void resetLocalVariables() {
		openSet.clear();
		visited.clear();
		f_score.clear();
		travelledPath.clear();
		getMap().removeTempNodes();
	}
	
	
	
	@Override
	public void calculatePath(CityObject locationRef, CityObject goalRef) {
		
		
		/* the start and goal might be something other than a crossing, update the crossingsMap accordingly */
		if(!getMap().isCrossing(locationRef))
			getMap().addTemporaryCrossing(locationRef);
		if(!getMap().isCrossing(goal))
			getMap().addTemporaryCrossing(goal);
		
		try{
			double highscore = Double.MAX_VALUE;
			Crossing bestNode = null;
			boolean found = false;
			current = getMap().getCrossing(locationRef.getId());
			Crossing goal = getMap().getCrossing(goalRef.getId());
			
			path = new ArrayList<>();
			
			visited.add(current);
			openSet.remove(current);
	
	//		System.out.println("amount of neighbours " + getMap().getNeighbourCrossings(current.getId()).size());
			for(LongRoad neighbourCrossing : getMap().getCrossing(current.getRoad().getId()).getNeighbourCrossings()){
				
				if(neighbourCrossing.getEnd().getId() == goal.getRoad().getId()){
					/* return a path to the goal */
					path = neighbourCrossing.getPath();
	
					openSet.clear();
					visited.clear();
					f_score.clear();
					travelledPath.clear();
					getMap().removeTempNodes();
					current = null;
					return;
				}
				
				/* add the node to the openset and calculate its f_score */
				if(!visited.contains(neighbourCrossing) && !openSet.contains(neighbourCrossing)){
					
					nrExploredNodes++;
					
					double tentative_f_score = heuristic_cost(neighbourCrossing.getEnd(), goal.getRoad()); 
					f_score.put(neighbourCrossing.getEnd(), tentative_f_score);
					openSet.add(getMap().getCrossing(neighbourCrossing.getEnd().getId()));
					
					/* if the node is better than the last */
					if(tentative_f_score < highscore){
						highscore = tentative_f_score;
						bestNode = getMap().getCrossing(neighbourCrossing.getEnd().getId());
						found = true;
					}
				}
			}
			
			if(found){
				double bestNodevalue = Double.MAX_VALUE;
				
				/* check if there exist an older value in openNode that is better */
				for(Crossing openNode : openSet){
					nrExploredNodes++;
					if(f_score.get(openNode) < bestNodevalue){
						bestNodevalue = f_score.get(openNode.getRoad());
						bestNode = openNode;
					}
				}
				
				/* backtrack to the old node */
				if(bestNodevalue < highscore)
					backtrackTo(bestNode.getRoad());
				else{
					
					try {
						LongRoad lr = getMap().getLongRoad(current.getRoad().getId(), bestNode.getRoad().getId());
						path = lr.getPath();
						travelledPath.add(lr);
					} catch (NotFound e) {
						e.printStackTrace();
					} 
				}
			}
			
			/* if no neighbour was found to go to, then go to the best node in openSet */
			else{
				double bestNodevalue = Double.MAX_VALUE;
				
				/* check if there exist an older value in openNode that is better */
				for(Crossing openNode : openSet){
					if(f_score.get(openNode) < bestNodevalue){
						bestNodevalue = f_score.get(openNode);
						bestNode = openNode;
					}
				}
				
				/* backtrack to the old node */
				if(bestNode != null){
					backtrackTo(bestNode.getRoad());
				}
				else{
					System.out.println("no new valid node existed");
					path = null;
					return;
				}
			}
			current = bestNode;
		}
		catch(Exception e){
			System.out.println("aborted calculatePath in RTAStar");
		}
	}
	

	private void backtrackTo(CityObject goal){
		System.out.println("checking for possible backtrack");
		ArrayList<CityObject> backtrackVisited = new ArrayList<CityObject>();
		backtrackVisited.add(current.getRoad());
		path = backtrackCheck(current.getRoad(), goal, backtrackVisited, new ArrayList<CityObject>());
		if(path == null)
			System.out.println("didn't find a node to backtrack to");
	}
	
	private ArrayList<CityObject> backtrackCheck(CityObject currentNode, CityObject goal, ArrayList<CityObject> backtrackVisited, ArrayList<CityObject> visitedOrder){
		try{
			if(goal == null)
				return null;
			for(Crossing neighbour : getMap().getNeighbourCrossings(currentNode.getId())){
				if(!backtrackVisited.contains(neighbour) && visited.contains(neighbour)){
					nrExploredNodes++;
					
					backtrackVisited.add(neighbour.getRoad());
					
					ArrayList<CityObject> newVisitedOrder = new ArrayList<CityObject>();
					newVisitedOrder.addAll(visitedOrder);
					newVisitedOrder.addAll(getMap().getLongRoad(currentNode.getId(), neighbour.getRoad().getId()).getPath());
					newVisitedOrder.remove(newVisitedOrder.size() - 1);
					
					ArrayList<CityObject> answer = backtrackCheck(neighbour.getRoad(), goal, backtrackVisited, newVisitedOrder);
					if(answer != null)
						return answer;
				}
				else if(neighbour.getRoad().getId() == goal.getId())
				{
					nrExploredNodes++;
					visitedOrder.addAll(getMap().getLongRoad(currentNode.getId(), goal.getId()).getPath());
					return visitedOrder;
				}
			}
		}
		catch(Exception e){
			
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
		return null;
	}

	@Override
	public void handleBlockade(CityObject blockedRoad, CityObject location) {
		try {
			getMap().updateWithBlockedRoad(blockedRoad);
			getMap().addTemporaryCrossing(location);
			
			Crossing bestCrossing = null;
			Crossing secondBestCrossing = null;
			double bestScore = Double.MAX_VALUE;
			double secondBestScore = Double.MAX_VALUE;
			for(Crossing o : openSet){
				double score = f_score.get(o.getRoad());
				
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
			current = getMap().getCrossing(location.getId());
			backtrackTo(secondBestCrossing.getRoad());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
}
