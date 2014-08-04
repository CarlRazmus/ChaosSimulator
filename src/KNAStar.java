import java.util.ArrayList;



public class KNAStar extends PathFinder{
	//choose a parameter K which defines how many crossings the algorithm will include
	//choose a parameter N which defines how many crossings the path will return
	private int k;
	private int n;
	ArrayList<CityObject> path = new ArrayList<CityObject>();
	
	public KNAStar(int k, int n){
		this.k = k;
		this.n = n;
	}


	@Override
	public void calculatePath(CityObject location, CityObject target) {
		/* iterate K times 
		 * 		build up a  list of available paths with the length of the paths and also a value for the bird-distance to the goal
		 * 		return N steps the path with best G-score	
		 */
	}


	@Override
	public ArrayList<CityObject> getPath() {
		return path;
	}


	@Override
	public void initialize(CityObject start, CityObject goal) {
		
	}


	@Override
	public ArrayList<CityObject> getDebugData() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void handleBlockade(CityObject blockedRoad, CityObject location) {
		// TODO Auto-generated method stub
		
	}
	
	
}
