
public class FireFighter extends Agent{

	public FireFighter() {
		super();
	}
	
	public FireFighter(MovementBehavior movementBehavior){
		super(movementBehavior);
	}
	
	public FireFighter(MovementBehavior movementBehavior, CityObject location){
		super(movementBehavior, location);
	}

	@Override
	public void think() {
		if(getPathFinder().getMap().checkCrossingCorruption())
			System.out.println("is corrupted ");
		
		if(getTarget() == null)
		{
			setTarget(getMovementBehavior().calculateTarget(getLocation()));
			
			getPathFinder().initialize(getLocation(), getTarget());
			
			getPathFinder().calculatePath(getLocation(), getTarget());
			
			setPath(getPathFinder().getPath());
		}

		/* if no valid path has been found */
		else if(getPath() == null){
//			System.out.println("path was null, sets target to null");
			setTarget(null);
		}
		else if( getPath().isEmpty()){
//			System.out.println("path was empty, sets target to null");
			setTarget(null);
		}
		else{
			
			if(getLocation().getId() == getTarget().getId()){
				setTarget(null);
			}
			else if(atEndOfPath()){
				getPathFinder().calculatePath(getLocation(), getTarget());
				setPath(getPathFinder().getPath());
			}
			
			/* if there exist a path to the target -> move towards the target */
			else{
				move();
			}
		}
//		System.out.println("nr Explored Nodes " + ((NewRTAStar)getPathFinder()).nrExploredNodes);
//		System.out.println("nr Traversed Nodes " + getNrTraversedNodes());
//		System.out.println(); 
	}
}
