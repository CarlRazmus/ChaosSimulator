package WorldClasses;

import java.util.ArrayList;

public class Crossing {
	private CityObject crossing;
	
	private ArrayList<LongRoad> neighbourCrossings;
	
	
	public Crossing(CityObject crossing){
		this.crossing = crossing;
		neighbourCrossings = new ArrayList<LongRoad>();
	}
	
	public ArrayList<LongRoad> getNeighbourCrossings(){
		return neighbourCrossings;
	}
	
	public CityObject getRoad(){
		return crossing;
	}
	
	public int getId(){
		return crossing.getId();
	}
	
	public void printNeighbours(){
		for(LongRoad lr : neighbourCrossings){
			if(lr.getPath().size() == 0)
				System.out.println("SIZE OF LONGROAD WAS 0");
			else
				System.out.println("   " + lr.getEnd());
		}
	}
	
	
}
