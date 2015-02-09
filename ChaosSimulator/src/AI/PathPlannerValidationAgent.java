package AI;

import java.awt.Color;


/*
 * This class is only meant to a simple variant of an agent so that a user can test different pathPlanning algorithms with it.
 * 
 * !!! IT CAN ONLY GO FROM ONE CROSSING TO ANOTHER CROSSING  !!!
 */

public class PathPlannerValidationAgent extends Agent{

	public PathPlannerValidationAgent(Color color) {
		super(color);
	}

	@Override
	public void think() {
		//check whether or not the map has been corrupted
		pathFinder.getMap().checkCrossingCorruption();
		
		//if no target exist -> create a new one from the movement behavior class
		if(target == null){
			setTarget(movementBehavior.calculateTarget(location));
			
			//if no new target could be created, then shut down this agent since it has no more goals to validate
			if(getTarget() == null){
				isOnline = false;
				return;
			}
			
			pathFinder.resetLocalVariables();
			pathFinder.calculatePath(getLocation(), getTarget());
			
			setPath(pathFinder.getPath());
		}

		// if no valid path has been found
		else if(path == null)
			reportBadPath();
		
		//this could only happen in 1 scenario, and it is if the pathPlanner can return a partial plan
		else if(path.isEmpty()){
			System.out.println("path did not go all the way to the goal, tries to calculate a new path");
			pathFinder.calculatePath(location, target);
			setPath(pathFinder.getPath());
		}
		else{
			if(location.getId() == target.getId())
				setTarget(null);
			
			else if(isAtEndOfPath()){
				pathFinder.calculatePath(location, target);
				setPath(pathFinder.getPath());
			}
			
			// if there exist a path to the target -> move towards the target 
			else
				move();
		}		
	}

}
