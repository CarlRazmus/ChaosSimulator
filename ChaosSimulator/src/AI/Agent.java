package AI;
import java.awt.Color;
import java.util.ArrayList;

import PathPlanners.PathFinder;
import WorldClasses.CityObject;
import WorldClasses.Road;
import Behaviours.MovementBehavior;

public abstract class Agent extends CityObject implements Runnable{
	protected MovementBehavior movementBehavior;
	protected PathFinder pathFinder;
	protected CityObject target;
	protected CityObject location;
	protected ArrayList<CityObject> path;
	private int positionIndex = 1;
	protected long time;
	
	protected int nrTraversedNodes = 0;
	protected boolean isOnline = true;
	
	
	public abstract void think();

	
	public Agent(Color color){
		super(color);
	}
	

	/**
	 * as long as the agent is online, the function think() is run every 1 second
	 */
	@Override
	public void run() {
		while(true){
			if(isOnline){
				if(System.currentTimeMillis() > time + 1){
					think();
					time = System.currentTimeMillis();
				}
			}
		}
	}

	public void moveTo(CityObject nextLocation) {
		this.location = nextLocation;
	}
	
	public void move(){
		CityObject nextLocation = path.get(positionIndex);
		
		//TODO check if nextLocation is a neighbor to the currentLocation, if not, then throw an Exception
		
		/* check if the road that we want to move to is blocked (or occupied?) */ 
		if(nextLocation.isBlocked() && nextLocation instanceof Road){
			System.out.println("the road was blocked");
			
			getPathFinder().handleBlockade(nextLocation, getLocation());
			setPath(getPathFinder().getPath());
		}
		else{
			this.location = path.get(positionIndex);
			moveTo(path.get(positionIndex));
			positionIndex++;
			nrTraversedNodes++;
		}
	}
	
	public void reportBadPath(){
		setTarget(null);
		setPath(null);
	}
	
	public boolean isAtEndOfPath(){
//		System.out.println("atEndOfPath "+ positionIndex +"/"+path.size());
		if(positionIndex == path.size())
			return true;
		else return false;
	}
	

	//       getters & setters   //
	public MovementBehavior getMovementBehavior() {
		return movementBehavior;
	}

	public void setMovementBehavior(MovementBehavior movementBehavior) {
		this.movementBehavior = movementBehavior;
	}

	public CityObject getTarget() {
		return target;
	}

	public void setTarget(CityObject target) {
		this.target = target;
	}

	public CityObject getLocation() {
		return location;
	}

	public void setLocation(CityObject location) {
		this.location = location;
	}

	public PathFinder getPathFinder() {
		return pathFinder;
	}

	public void setPathFinder(PathFinder pathFinder) {
		this.pathFinder = pathFinder;
	}

	public ArrayList<CityObject> getPath() {
		return path;
	}

	public void setPath(ArrayList<CityObject> path) {
		this.path = path;
		positionIndex = 1;
	}

	public int getNrTraversedNodes() {
		return nrTraversedNodes;
	}

	public boolean getIsOnline() {
		return isOnline;
	}
}
