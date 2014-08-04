import java.util.ArrayList;


public class Building extends CityObject{

	private ArrayList<BuildingBlock> buildingBlocks;
	
	public Building(){
		buildingBlocks = new ArrayList<BuildingBlock>();
	}
	
	public Building(ArrayList<BuildingBlock> buildingBlocks){
		this.buildingBlocks = buildingBlocks;
	}
	
	public void addBuildingBlock(BuildingBlock block){
		buildingBlocks.add(block);
	}
	
	public ArrayList<BuildingBlock> getBlocks () {
		return buildingBlocks;
	}
}
