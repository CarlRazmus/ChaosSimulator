package Default;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import WorldClasses.Building;
import WorldClasses.BuildingBlock;
import WorldClasses.CityObject;
import WorldClasses.Road;

public class CityDataReader {

	final String CITYCOLORFILE = "src/CityColor.png";
	final String CITYFILE = "src/City1000x1000.png";
	private ArrayList<Road> roads;
	private ArrayList<BuildingBlock> buildingBlocks;
	private ArrayList<Building> buildings;
	private HashMap<Integer, CityObject> cityColorMap;
	private ArrayList<BuildingBlock> visited;
	private ArrayList<Road> crossings; 
	private int cityWidth;
	private int cityHeight;
	public static int IDCount = 0;

	public CityDataReader() {
		initialize();
	}

	public void initialize() {
		IDCount = 0;
		cityColorMap = new HashMap<Integer, CityObject>();
		buildingBlocks = new ArrayList<BuildingBlock>();
		buildings = new ArrayList<Building>();
		roads = new ArrayList<Road>();
		visited = new ArrayList<BuildingBlock>();
		crossings = new ArrayList<Road>();

		initializeCityColors();
		getCityData();
		
		pairBuildingObjectsTogether();
		findAllRoadNeighbours();
		findCrossings();
	}

	private void initializeCityColors() {
		BufferedImage img;
		int[] pixels;
		int width;
		int height;
		int rgb;
		try {
			img = ImageIO.read(new File(CITYCOLORFILE));
			width = img.getWidth();
			height = img.getHeight();
			pixels = img.getRGB(0, 0, width, height, null, 0, width);

			rgb = pixels[0];
			cityColorMap.put(rgb, new Road(0, 0));
			rgb = pixels[1];
			cityColorMap.put(rgb, new BuildingBlock(0, 0));
		} catch (IOException e) {
			System.out.println("Couldnt read CityColors.png that contains the citys color-data");
		}
	}

	public void getCityData() {
		BufferedImage img;
		int[] pixels;
		int rgb;

		try {
			img = ImageIO.read(new File(CITYFILE));
			cityWidth = img.getWidth();
			cityHeight = img.getHeight();
			pixels = img.getRGB(0, 0, cityWidth, cityHeight, null, 0, cityWidth);

			for (int i = 0; i < cityHeight; i++)
				for (int j = 0; j < cityWidth; j++) {
					rgb = pixels[i * cityWidth + j];
					CityObject object = cityColorMap.get(rgb);

					if (object instanceof Road)
						roads.add(new Road(j, i));
					else if (object instanceof BuildingBlock)
						buildingBlocks.add(new BuildingBlock(j, i));
				}
		} catch (IOException e) {
			System.out.println("Couldnt read city.png that contains the city data");
		}
	}

	public void pairBuildingObjectsTogether() {
		for (BuildingBlock b : buildingBlocks) 
			if (!visited.contains(b)) {
				visited.add(b);
				Building building = new Building();
				building.addBuildingBlock(b);
				checkForAdjacentBuildingBlocks(b, building);
				buildings.add(building);
			}
	}

	private void checkForAdjacentBuildingBlocks(BuildingBlock b, Building building) {
		for (BuildingBlock neighbour : buildingBlocks) 
			if (!visited.contains(neighbour) && isNeighbour(b, neighbour)) {
				visited.add(neighbour);
				building.addBuildingBlock(neighbour);
				checkForAdjacentBuildingBlocks(neighbour, building);
			}
	}

	public void findAllRoadNeighbours() {
		for (Road road : roads) 
			for (Road neighbour : roads) 
				if(road != neighbour)
					findRoadNeighbours(road, neighbour);
	}

	private void findRoadNeighbours(Road road, Road neighbour) {
		if (road.getXPos() == neighbour.getXPos()) {
			if (neighbour.getYPos() == road.getYPos() - Camera.scale || neighbour.getYPos() == road.getYPos() + Camera.scale) {
				road.getNeighbours().add(neighbour);
			}
		} else if (road.getYPos() == neighbour.getYPos())
			if (neighbour.getXPos() == road.getXPos() - Camera.scale || neighbour.getXPos() == road.getXPos() + Camera.scale)
				road.getNeighbours().add(neighbour);
	}

	public boolean isNeighbour(BuildingBlock b, BuildingBlock neighbour) {
		if (b.getXPos() == neighbour.getXPos())
			return neighbour.getYPos() == b.getYPos() - Camera.scale || neighbour.getYPos() == b.getYPos() + Camera.scale;
		else if (b.getYPos() == neighbour.getYPos())
			return neighbour.getXPos() == b.getXPos() - Camera.scale || neighbour.getXPos() == b.getXPos() + Camera.scale;
		return false;
	}
	
	/*functions for handling crossings information*/
	private void findCrossings(){
		for (Road road : roads)
			if(isCrossing(road))
				crossings.add(road);
	}
	
	private boolean isCrossing(Road road) {
		int count = 0;
		for(CityObject neighbour : road.getNeighbours())
			if(neighbour instanceof Road)
				count++;
		return count > 2;
	}
	
	/*getters and setters */
	public ArrayList<Building> getBuildings() {
		return buildings;
	}

	public ArrayList<Road> getRoads() {
		return roads;
	}

	public int getCityWidth() {
		return cityWidth;
	}

	public int getCityHeight() {
		return cityHeight;
	}

	public ArrayList<Road> getCrossings() {
		return crossings;
	}
}
