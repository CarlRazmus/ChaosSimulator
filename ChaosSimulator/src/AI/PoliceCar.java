package AI;

import java.awt.Color;

public class PoliceCar extends Agent{

	public PoliceCar() {
		super(Color.BLUE);
	}

	public PoliceCar(Color color) {
		super(color);
	}
//	
//	@Override
//	public void run() {
//		//only perform 1 think() every second
//		while(isOnline){
//			if(System.currentTimeMillis() > time + 1000){
//				think();
//				time = System.currentTimeMillis();
//			}
//		}
//	}
//	
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
		
		else if(path.isEmpty()){ //TODO remove this???
			System.out.println("path did not go all the way to the goal, tries to calculate a new path");
			pathFinder.calculatePath(location, target);
			setPath(pathFinder.getPath());
		}
		else{
			//if the goal has been reached
			if(location.getId() == target.getId())
				setTarget(null);
			
			else if(isAtEndOfPath()){
				pathFinder.calculatePath(location, target);
				setPath(pathFinder.getPath());
			}
			
			/* if there exist a path to the target -> move towards the target */
			else
				move();
		}
	}
}
