package Behaviours;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import Default.Simulator;
import WorldClasses.CityObject;
import WorldClasses.Road;

public class RandomMovement extends MovementBehavior {
	private final int NR_GOALS = 300;
	
	// all of these objects are unique and should be shared in all instantiated objects of this class
	private static ArrayList<Road> queue;
	private static HashMap<Integer, Integer> idToQueueNrMap = new HashMap<Integer, Integer>();
	private int id;
	
	
	public RandomMovement(){
		if(queue == null){
			generateRandomGoals();
//			queue = new ArrayList<Road>();
//			
//			int size = roadsList.size();
//			int randomIndex;
//			
//			for(int i = 0; i < NRCROSSINGS; i++){
//				Random rand = new Random();
//				randomIndex = rand.nextInt(size); //NOTE rand.nextInt returns 0 < value < size
//				queue.add(roadsList.get(randomIndex));
//			}
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
	
	private void generateRandomGoals(){
		queue = new ArrayList<Road>();
		
		Random random = new Random();
		for(int i = 0; i < NR_GOALS; i++){
			int value = random.nextInt(Simulator.model.getRoads().size());
			queue.add(Simulator.model.getRoads().get(value));
			if(Simulator.model.getRoads().get(value).getNeighbours().size() == 1){
				queue.remove(Simulator.model.getRoads().get(value));
				System.out.println("didnt add goal: goal only had 1 neighbour");
				continue;
			}
			for(CityObject o : Simulator.model.getRoads().get(value).getNeighbours()){
				//don't add the goal if it's a neighbor with a crossing
				if(Simulator.model.getCrossings().contains(o)){
					queue.remove(Simulator.model.getRoads().get(value));
					System.out.println("didnt add goal: goal was neighbor to a crossing");
					break;
				}
				//dont add the goal if it is neighbor to another existing goal
				else if(queue.contains(o)){
					queue.remove(Simulator.model.getRoads().get(value));
					System.out.println("didnt add goal: was neighbor to a already existing goal");
					break;
				}
			}
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
