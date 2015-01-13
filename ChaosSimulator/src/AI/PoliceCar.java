package AI;

import java.awt.Color;

public class PoliceCar extends Agent {

	public PoliceCar() {
		super(Color.BLUE);
	}

	public PoliceCar(Color color) {
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
		
		else if(path.isEmpty()){
//			setTarget(null);
			System.out.println("path was empty, tries to calculate a new path");
			pathFinder.calculatePath(location, target);
			setPath(pathFinder.getPath());
			
			//only report badPath when no path has been returned at all, a path could end up as bad but still exist
			//reportBadPath();
			
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
