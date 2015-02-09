package Behaviours;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import WorldClasses.CityObject;
import WorldClasses.Road;

public class RandomCrossingMovement extends MovementBehavior {

	private final int NRCROSSINGS = 10;
	
	// all of these objects are unique and should be shared in all instantiated objects of this class
	private static ArrayList<Road> queue;
	private static ArrayList<Road> crossingsList;
	private static HashMap<Integer, Integer> idToQueueNrMap = new HashMap<Integer, Integer>();
	private int id;
	
	
	public RandomCrossingMovement(ArrayList<Road> crossingsList){
		
		if(RandomCrossingMovement.crossingsList == null)
			RandomCrossingMovement.crossingsList = crossingsList;
	
		if(queue == null){
			queue = new ArrayList<Road>();
			
			int size = crossingsList.size();
			int randomIndex;
			
			for(int i = 0; i < NRCROSSINGS; i++){
				Random rand = new Random();
				randomIndex = rand.nextInt(size); //NOTE rand.nextInt returns 0 < value < size
				queue.add(crossingsList.get(randomIndex));
			}
		}
	}
	
	public void addAgentID(int id){
		this.id = id;
		
		if(idToQueueNrMap.get(id) == null)
			idToQueueNrMap.put(id, 0);
		else
			System.out.println("tried to add an already existing agent! shuuu on u!");
	}
	
	private void increaseQueueNr(){
		try {
			
			int oldQueueNr = idToQueueNrMap.get(id);
			idToQueueNrMap.put(id, ++oldQueueNr);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public CityObject calculateTarget(CityObject location) {
		int queueNr = idToQueueNrMap.get(id);
		
		if(queueNr == queue.size())
			return null;
		
		CityObject target = queue.get(queueNr);
		increaseQueueNr();
		
		return target; 
	}

}
