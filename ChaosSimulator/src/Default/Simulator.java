package Default;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import AI.Agent;
import AI.FireFighter;
import AI.PoliceCar;
import Behaviours.RandomMovement;
import PathPlanners.AStar;
import PathPlanners.KNAStar;
import PathPlanners.RTAStar;
import WorldClasses.CityObject;
import WorldClasses.CrossingsMap;
import debug.InformationWindow;



public class Simulator extends JFrame implements KeyListener {
	
	private static final long serialVersionUID = 1L;
	
	private CityDataReader reader;
	public static WorldModel model;
	private Camera camera;
	private int frameHeight;
	private int frameWidth;
	private ArrayList<Agent> agents;

	private ArrayList<CityObject> goals;
	private final int NR_POLICE_CARS = 1;
	private final int NR_FIRE_BRIGADES = 1;
//	public static final int DEFAULT_SIZE = 5;
//	private final double FIRE_CHANCE_FACTOR = 0.5;
	
	public static void main(String[] args) {
		Simulator simulator = new Simulator();
		simulator.initialize();
	}	
	
	private void addAgents(){		
		for(int i = 0; i < NR_FIRE_BRIGADES; i ++)
			addFireBrigade();		
		for(int i = 0; i < NR_POLICE_CARS; i ++)
			addPoliceCar();
	}
	
	
	private void generateRandomGoals(){
		goals = new ArrayList<CityObject>();
		
		Random random = new Random();
		for(int i = 0; i < 100; i++){
			int value = random.nextInt(Simulator.model.getRoads().size());
			goals.add(Simulator.model.getRoads().get(value));
			if(Simulator.model.getRoads().get(value).getNeighbours().size() == 1){
				goals.remove(Simulator.model.getRoads().get(value));
				System.out.println("didnt add goal: goal only had 1 neighbour");
				continue;
			}
			for(CityObject o : Simulator.model.getRoads().get(value).getNeighbours()){
				//dont add the goal if is neigbor with a crossing
				if(Simulator.model.getCrossings().contains(o)){
					goals.remove(Simulator.model.getRoads().get(value));
					System.out.println("didnt add goal: goal was neighbor to a crossing");
					break;
				}
				//dont add the goal if it is neighbor to another existing goal
				else if(goals.contains(o)){
					goals.remove(Simulator.model.getRoads().get(value));
					System.out.println("didnt add goal: was neighbor to a already existing goal");
					break;
				}
			}//1674
		}
	}
	
	private void addPoliceCar(){
		PoliceCar pc = new PoliceCar();
		RandomMovement policeCarRandomMovement = new RandomMovement();
		policeCarRandomMovement.setList(goals);
		pc.setMovementBehavior(policeCarRandomMovement);
		pc.setPathFinder(new KNAStar(70,50));
		pc.getPathFinder().setMap(generateCrossingsMap());
		pc.setLocation(model.getRoads().get(0));

		pc.getPathFinder().getMap().checkCrossingCorruption();

		agents.add(pc);
	}
	
	private void addFireBrigade(){
		FireFighter fb = new FireFighter();
		RandomMovement fireBrigadeRandomMovement = new RandomMovement();
		fireBrigadeRandomMovement.setList(goals);
		fb.setMovementBehavior(fireBrigadeRandomMovement);
		fb.setPathFinder(new KNAStar(70,50));
		fb.getPathFinder().setMap(generateCrossingsMap());
		fb.setLocation(model.getRoads().get(0));

		fb.getPathFinder().getMap().checkCrossingCorruption();
		
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
		addKeyListener(this);
		
		agents = new ArrayList<Agent>();
		reader = new CityDataReader();
		model = new WorldModel();
		camera = new Camera();
 
		model.setBuildings(reader.getBuildings());
		model.setRoads(reader.getRoads());
		model.addCrossings(reader.getCrossings());
		
		generateRandomGoals();
		addAgents();
		
		camera.setModel(model);
		camera.setAgents(agents);
		

		frameWidth = reader.getCityWidth()*Camera.scale + 100;
		frameHeight = reader.getCityHeight()*Camera.scale + 100;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
		if(frameWidth > width)
			frameWidth = (int)width;
		if(frameHeight > height)
			frameHeight = (int)height;
		
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setSize(frameWidth, frameHeight);
		this.add(camera);
		
		InformationWindow informationWindow = new InformationWindow("hej ehj", this.getLocation().x + frameHeight + 10, this.getLocation().y);
		
		
		// put up some blockades on the roads
//		model.getRoads().get(150).block();
//		model.getRoads().get(190).block();
//		model.getRoads().get(300).block();
//		model.getRoads().get(400).block();
//		model.getRoads().get(500).block();

//		model.getRoads().get(650).block();
//		model.getRoads().get(700).block();
//		model.getRoads().get(750).block();
//		model.getRoads().get(998).block();
		

		requestFocus();
		
		long time = System.currentTimeMillis();
		while(true){
			if(System.currentTimeMillis() > time + 4){
				simulate();
				informationWindow.updateGraph();
				time = System.currentTimeMillis();
				this.repaint();	
			}
		}
	}

	ArrayList<Agent> removeList = new ArrayList<Agent>();
	public void simulate(){
		for (Agent agent : agents){
				agent.think();
				if(!agent.isOnline()){
					removeList.add(agent);
					System.out.println("agent is offline and is removed");
				}
		}
		
		if(!removeList.isEmpty()){
			for (Agent agent : removeList){
				agents.remove(agent);
			}
			removeList.clear();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		camera.handleKeyEvent(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}
