package Behaviours;

import java.util.ArrayList;
import java.util.Random;

import Default.Simulator;
import WorldClasses.CityObject;

public class RandomMovement extends MovementBehavior {
	private ArrayList<CityObject> list = new ArrayList<>();
	private int count = -1;
	
	@Override
	public CityObject calculateTarget(CityObject location) {
	
		if(list.isEmpty()){
//			list.add(Simulator.model.getRoads().get(293));
			list.add(Simulator.model.getRoads().get(562));
			list.add(Simulator.model.getRoads().get(134));
			list.add(Simulator.model.getRoads().get(222));
			list.add(Simulator.model.getRoads().get(771));
//			
//			list.add(Simulator.model.getRoads().get(726));
//			list.add(Simulator.model.getRoads().get(482));
//			list.add(Simulator.model.getRoads().get(134));
//			list.add(Simulator.model.getRoads().get(93));
//			list.add(Simulator.model.getRoads().get(555));
		}
		
//		ArrayList<Road> roads = Simulator.model.getRoads();
//		ArrayList<CityObject> roads = Simulator.model.getCrossings();
//		int max = roads.size();
//		Random rand = new Random();
		
		return list.get(++count);
//		return roads.get(rand.nextInt(max - 1));
	}
}
