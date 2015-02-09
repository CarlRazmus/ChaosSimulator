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
	}
}
