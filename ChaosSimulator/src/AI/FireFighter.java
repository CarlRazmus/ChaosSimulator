package AI;
import java.awt.Color;

import WorldClasses.CityObject;
import Behaviours.MovementBehavior;


public class FireFighter extends Agent{

	public FireFighter() {
		super(Color.red);
	}
	
	public FireFighter(Color color) {
		super(color);
	}

	@Override
	public void think() {
		if(pathFinder.getMap().checkCrossingCorruption())
			System.out.println("is corrupted ");
		
		if(target == null){
			setTarget(movementBehavior.calculateTarget(location));
			if(getTarget() == null){
				isOnline = false;
				return;
			}
			pathFinder.resetLocalVariables();
			pathFinder.calculatePath(getLocation(), getTarget());
			
			setPath(pathFinder.getPath());
		}

		/* if no valid path has been found */
		else if(path == null)
			reportBadPath();
		
		//TODO this has to be re-written, all planners should use a reset-function or something when this happens.
		//     for AStar this shouldnt be able to happen, but this might be handled differently for example with       
		//	   KNAStar and RTAStar etc. FIX THIS SHIT!!
		else if(path.isEmpty()){
			System.out.println("path did not go all the way to the goal, tries to calculate a new path");
			pathFinder.calculatePath(location, target);
			setPath(pathFinder.getPath());
		}
		else{
			if(location.getId() == target.getId())
				setTarget(null);
			
			else if(atEndOfPath()){
				pathFinder.calculatePath(location, target);
				setPath(pathFinder.getPath());
			}
			
			/* if there exist a path to the target -> move towards the target */
			else
				move();
		}
	}
}
