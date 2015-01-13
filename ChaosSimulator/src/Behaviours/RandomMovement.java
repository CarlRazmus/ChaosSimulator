package Behaviours;

import java.util.ArrayList;

import Default.Simulator;
import WorldClasses.CityObject;

public class RandomMovement extends MovementBehavior {
	private ArrayList<CityObject> list = new ArrayList<>();
	private int count = 0;
	
	
	public RandomMovement(){
		fillList();
	}
	
	public void setList(ArrayList<CityObject> list){
		this.list = list;
	}
	
	private void fillList(){
		list.add(Simulator.model.getRoads().get(562));
		list.add(Simulator.model.getRoads().get(1234));
		list.add(Simulator.model.getRoads().get(2222));
		list.add(Simulator.model.getRoads().get(3771));
		list.add(Simulator.model.getRoads().get(726));
		list.add(Simulator.model.getRoads().get(282));
	}
	@Override
	public CityObject calculateTarget(CityObject location) {
	
		if(count == (list.size() - 1)) 
			return null;
		
		return list.get(count++);
//		return roads.get(rand.nextInt(max - 1));
		
//		KNAStar 21 in openset for 2187
//		AStar 21 in openset for 2187
//		KNAStar 12 in openset for 3853
//		AStar 12 in openset for 3853
//		KNAStar (78)
//		AStar 111 in openset for 2801
//		KNAStar 154 in openset for 2801
//		KNAStar 41 in openset for 733
//		AStar 41 in openset for 733
//		KNAStar 21 in openset for 2880
//		AStar 21 in openset for 2880
//		KNAStar 8 in openset for 3403
//		AStar 8 in openset for 3403
//		KNAStar 35 in openset for 177
//		AStar 36 in openset for 177
	}
}
