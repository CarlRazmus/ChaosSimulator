import java.util.ArrayList;

public abstract class Agent extends CityObject {
	private MovementBehavior movementBehavior;
	private PathFinder pathFinder;
	private CityObject target;
	private CityObject location;
	private ArrayList<CityObject> path;
	private int positionIndex = 1;
	
	private int nrTraversedNodes = 0;

	// abstract methods
	public abstract void think();

	
	
	// Constructors
	public Agent() {
	}

	public Agent(MovementBehavior movementBehavior) {
		this.movementBehavior = movementBehavior;
	}

	public Agent(MovementBehavior movementBehavior, CityObject location) {
		this.movementBehavior = movementBehavior;
		this.location = location;
	}

	public void moveTo(CityObject nextLocation) {
		this.location = nextLocation;
	}
	
	public void move(){
		CityObject nextLocation = path.get(positionIndex);
		
		/* check if the road that we want to move to is blocked (or occupied?) */ 
		if(nextLocation.isBlocked() && nextLocation instanceof Road){
			System.out.println("the road was blocked");
			
			getPathFinder().handleBlockade(nextLocation, getLocation());
			setPath(getPathFinder().getPath());
		}
		else{
			this.location = path.get(positionIndex);
			positionIndex++;
			nrTraversedNodes++;
		}
	}
	
	public boolean atEndOfPath(){
//		System.out.println("atEndOfPath "+ positionIndex +"/"+path.size());
		if(positionIndex == path.size())
			return true;
		else return false;
	}
	

	// getters & setters
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
}
