package PathPlanners;
import java.util.ArrayList;
import java.util.HashMap;

import org.omg.CosNaming.NamingContextPackage.NotFound;

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
		
		getMap().createDebugString();
//		getMap().printDebugString();
		
		/* the start and goal might be something other than a crossing, update the crossingsMap accordingly */
		if(!getMap().isCrossing(start))
			getMap().addTemporaryCrossing(start);
		if(!getMap().isCrossing(goal))
			getMap().addTemporaryCrossing(goal);
		
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

			/* reconstruct path if goal has been found */ 
			if(current.getId() == goal.getId()){
				backTrack(current, true);
//				System.out.println("KNAStar " + totalNrExploredNodes + " in openset for " + goal.getId());
				return;
			}
			
			openSet.remove(current);
			closedSet.add(current);
			
			try {
				for(LongRoad longRoad : getMap().getCrossing(current.getId()).getNeighbourCrossings()) {
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
			catch (NotFound e) {
				e.printStackTrace();
				resetLocalVariables();
			}
		}

		backTrack(current, false);
		
//		System.out.println("KNAStar (" + nrExploredNodes + ")");
		
	}
	
	public void backTrack(CityObject current, boolean goalFound){
		CityObject from = came_from.get(current);
		ArrayList<LongRoad> longRoads = new ArrayList<LongRoad>();
		
		try {
			while(from != null){
				longRoads.add(getMap().getLongRoad(from.getId(), current.getId()));
				current = from;
				from = came_from.get(current);	
			}
	
			int size = longRoads.size();
	
			//TODO return full longRoad path instead and handle the path in each planner instead
			for(int i = 1; i <= size; i++){
				for(int k = 1; k < longRoads.get(size - i).getPath().size(); k++){
					path.add(longRoads.get(size - i).getPath().get(k));
				}
				
				if(i >= n && !goalFound){
					getMap().removeTempNodes();
					return;
				}
			}	
			getMap().removeTempNodes();
		}
		
		catch (NotFound e) {
			e.printStackTrace();
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
		try{
			getMap().updateWithBlockedRoad(blockedRoad);
			resetLocalVariables();
			getMap().addTemporaryCrossing(location);
			calculatePath(location, goal);
		}
		catch(Exception e){
			e.printStackTrace();
			resetLocalVariables();
		}
	}

	@Override
	public void initialize(CityObject locationRef, CityObject goalRef) {
		// TODO Auto-generated method stub
	}
}
