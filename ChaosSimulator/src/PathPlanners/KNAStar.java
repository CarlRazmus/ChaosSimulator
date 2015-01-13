package PathPlanners;
import java.util.ArrayList;
import java.util.HashMap;

import WorldClasses.CityObject;
import WorldClasses.LongRoad;



public class KNAStar extends PathFinder{
	//choose a parameter K which defines how many crossings the algorithm will calculate over
	//choose a parameter N which defines the max amount of crossings the returned path will include (if the goal hasn't been found)
	private int k;
	private int n;
	private ArrayList<CityObject> path = new ArrayList<CityObject>();
//	private CityObject goal;
	
	ArrayList<CityObject> closedSet = new ArrayList<>();
	ArrayList<CityObject> openSet = new ArrayList<>();
	HashMap<CityObject,CityObject> came_from = new HashMap<CityObject, CityObject>(); 
	HashMap<CityObject, Integer> g_score = new HashMap<CityObject, Integer>(); 
	HashMap<CityObject, Double> f_score = new HashMap<CityObject, Double>(); 
	
	private int iterationCounter = 0;
	private int totalNrExploredNodes = 0;
	
	
	public KNAStar(int k, int n){
		this.k = k;
		this.n = n;
	}

	@Override
	public void resetLocalVariables(){
		iterationCounter = 0;
		closedSet.clear();
		openSet.clear();
		came_from.clear();
		g_score.clear();
		f_score.clear();
		path.clear();
		getMap().removeTempNodes();
		totalNrExploredNodes = 0;
		nrExploredNodes = 0;
	}
	
	public void softReset(){
		iterationCounter = 0;
		nrExploredNodes = 0;
		
		path.clear();
		closedSet.clear();
		openSet.clear();
		came_from.clear();
		g_score.clear();
		f_score.clear();
	}
	
	/**
	 * analyzes the map structure to define best values for the variables k and n
	 */
	private void analyzeMapStructure(){
		
	}
	
	//TODO change this later when implementing analyzeMapStructure
	public void generateKNValues(Object map){
		analyzeMapStructure();
	}

	private void printOpenSet(){
		System.out.println("OPENSET");
		for(CityObject o : openSet)
			System.out.println(" " + o.getId());
	}
	private void printFScore(){
		System.out.println("F-SCORE");
		for(CityObject o : f_score.keySet())
			System.out.println(" " + o.getId());
	}

	
	@Override
	public void calculatePath(CityObject start, CityObject goal) {
		this.goal = goal;
		CityObject current = null;

		softReset();
		
		/* the start and goal might be something other than a crossing, update the crossingsMap accordingly */
		if(!getMap().getNodes().contains(start))
			getMap().updateCrossings(start);
		if(!getMap().getNodes().contains(goal))
			getMap().updateCrossings(goal);
		
		openSet.add(start);
		g_score.put(start, 0);
		f_score.put(start, g_score.get(start) + heuristic_cost(start,goal));
		
		while(!openSet.isEmpty() && iterationCounter++ < k){
//			System.out.println("iteration #" + iterationCounter);
//			printOpenSet();
//			printFScore();
//			System.out.println();
			double lowestScore = Double.MAX_VALUE;
			
			for(CityObject node : openSet){
				if(f_score.get(node) < lowestScore){
					lowestScore = f_score.get(node);
					current = node;
				}
			}

			/* reconstruct path if goal has been found*/ 
			if(current.getId() == goal.getId()){
				backTrack(current, true);
//				System.out.println("found the goal! returns the whole path to it");
				System.out.println("KNAStar " + totalNrExploredNodes + " in openset for " + goal.getId());
				return;
			}
			
			openSet.remove(current);
			closedSet.add(current);
			
			for(LongRoad longRoad : getMap().neighbourCrossings.get(current)) {
				CityObject neighbourCrossing = longRoad.getEnd();
				int tentative_g_score = g_score.get(current) + getMap().distanceBtwnCrossings(current.getId(), neighbourCrossing.getId());
				double tentative_f_score = tentative_g_score + heuristic_cost(neighbourCrossing, goal);
	
				if((closedSet.contains(neighbourCrossing) && tentative_f_score >= f_score.get(neighbourCrossing)))
					continue;
					
				if(!openSet.contains(neighbourCrossing) || tentative_f_score < f_score.get(neighbourCrossing)){
					came_from.put(neighbourCrossing, current);
	                g_score.put(neighbourCrossing, tentative_g_score);
	                f_score.put(neighbourCrossing, tentative_f_score);
	                if(!openSet.contains(neighbourCrossing)){
	                		openSet.add(neighbourCrossing);
	                		nrExploredNodes++;
	                		totalNrExploredNodes++;
	                }
				}
			}
		}

		backTrack(current, false);
		System.out.println("KNAStar (" + nrExploredNodes + ")");
	}
	
	private void reconstruct_path(HashMap<CityObject,CityObject> came_from, CityObject current){
		CityObject from = came_from.get(current);
		
		while(from != null){
			//get the path to current from its predeccessor, exluding the predeccessor.
			LongRoad pathToCurrent = getMap().getLongRoad(from.getId(), current.getId());
			path.addAll(0, pathToCurrent.getPath());
			current = from;
			from = came_from.get(current);
			path.remove(0);
		}
	}
	
	public void backTrack(CityObject current, boolean goalFound){
		CityObject from = came_from.get(current);
		ArrayList<LongRoad> longRoads = new ArrayList<LongRoad>();
		
		while(from != null){
			//gets the path to current from its predeccessor, exluding the predeccessor.
			longRoads.add(getMap().getLongRoad(from.getId(), current.getId()));
			current = from;
			from = came_from.get(current);
		}

		int size = longRoads.size();

		for(int i = 1; i <= size; i++){
			ArrayList<CityObject> subPath = longRoads.get(size - i).getPath();
			subPath.remove(0);
			path.addAll(subPath);
			if(i >= n && !goalFound){
//				System.out.println("returns a shorter path");
				return;
			}
		}
//		System.out.println("returns the complete path");
	}
	
	/**
	 * gets the bird-distance from start to goal
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
