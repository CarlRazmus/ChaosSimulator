import java.awt.BorderLayout;
import java.awt.Label;
import java.util.ArrayList;

import javax.swing.JFrame;



public class Simulator {
	private CityDataReader reader;
	static protected WorldModel model;
	private Camera camera;
	private int frameHeight;
	private int frameWidth;
	private ArrayList<Agent> agents;
	public static final int DEFAULT_SIZE = 5;
//	private final double FIRE_CHANCE_FACTOR = 0.5;
	
	public static void main(String[] args) {
		Simulator simulator = new Simulator();
		simulator.initialize();
	}	
	
	private void addAgents(CityDataReader reader){		
		for(int i = 0; i < 1; i ++)
			addFireBrigade();
	}
	
	private void addFireBrigade(){
		FireFighter fb = new FireFighter();
		fb.setMovementBehavior(new RandomMovement());
		fb.setPathFinder(new RTAStar());
		
		fb.getPathFinder().setMap(generateCrossingsMap());
		fb.setLocation(model.getRoads().get(0));
		
		agents.add(fb);
	}
	
	private CrossingsMap generateCrossingsMap(){
		CrossingsMap map = new CrossingsMap();
		map.setCrossings(reader.getCrossings());
		map.findCrossingsNeighbours();
		return map;
	}
	
	public static CityObject getCityObject(int id){
		return model.getCityObject(id);
	}
	
	public static CityObject getRoad(int id){
		return model.getRoad(id);
	} 
	
	public static CityObject getBuilding(int id){
		return model.getBuilding(id);
	}
	
	/*public static ArrayList<CityObject> getRoadNeighbours(int id){
		
	}*/
	
	private void initialize(){
		agents = new ArrayList<Agent>();
		reader = new CityDataReader();
		model = new WorldModel();
		camera = new Camera();
 
		model.setBuildings(reader.getBuildings());
		model.setRoads(reader.getRoads());
		model.addCrossings(reader.getCrossings());
		
		addAgents(reader);
		
		camera.setModel(model);
		camera.setAgents(agents);
		
		//1. Create the frame.
		JFrame frame = new JFrame("Chaos Simulator");
		
		//2. Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//3. Create components and put them in the frame.
		//...create emptyLabel...
		Label emptyLabel = new Label();
		frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
		
		//4. Size the frame.
		frame.pack();
		
		//5. Show it.
		frameWidth = 800;//reader.getCityWidth()*SCALE + 20;
		frameHeight = 600;//reader.getCityHeight()*SCALE + 20;
		
		frame.setVisible(true);
		frame.setSize(frameWidth, frameHeight);
		
		frame.add(camera);
		
		model.getRoads().get(150).setBlocked(true);
		model.getRoads().get(190).setBlocked(true);
		model.getRoads().get(300).setBlocked(true);
		model.getRoads().get(400).setBlocked(true);
		model.getRoads().get(500).setBlocked(true);

		model.getRoads().get(650).setBlocked(true);
		model.getRoads().get(700).setBlocked(true);
		model.getRoads().get(750).setBlocked(true);
		model.getRoads().get(998).setBlocked(true);
		
		long time = System.currentTimeMillis();
		while(true){
			if(System.currentTimeMillis() > time + 150){
				simulate();
				time = System.currentTimeMillis();
				frame.repaint();	
			}
		}
	}
	
	public void simulate()
	{
		for (Agent agent : agents){
			agent.think();
		}
	}
}
