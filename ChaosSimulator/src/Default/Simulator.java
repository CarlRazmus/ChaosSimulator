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
import Behaviours.RandomCrossingMovement;
import Behaviours.RandomMovement;
import PathPlanners.AStar;
import PathPlanners.CrossingsMap;
import PathPlanners.KNAStar;
import PathPlanners.RTAStar;
import WorldClasses.CityObject;
import debug.InformationWindow;


//TODO should make this a singleton just for the sake of it. 
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
	
	
	
	
	public static void main(String[] args) {
		Simulator simulator = new Simulator();
		simulator.initialize();
	}
	
	
	
	/**
	 * creates all the agents that is used when the application is started (more can be added dynamically under runtime)
	 */
	private void createAgents(){		
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
				//don't add the goal if it's a neighbor with a crossing
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
			}
		}
	}
	

	private void generateRandomCrossingsGoals(){
		goals = new ArrayList<CityObject>();
		
		System.out.println(Simulator.model.getCrossings().size());
		
		Random random = new Random();
		for(int i = 0; i < ((int)Simulator.model.getCrossings().size()/4); i++){
			int value = random.nextInt(Simulator.model.getCrossings().size());
			
			goals.add(Simulator.model.getCrossings().get(value));
			
			if(Simulator.model.getCrossings().get(value).getNeighbours().size() == 1){
				goals.remove(Simulator.model.getCrossings().get(value));
				System.out.println("didnt add goal: goal only had 1 neighbour");
				continue;
			}
		}
	}
	
	
	private void startAgentThreads(){
		for(Agent agent : agents)
			(new Thread(agent)).start();
		
	}
	
	private void addPoliceCar(){
		PoliceCar pc = new PoliceCar();
		
		RandomCrossingMovement policeCarRandomMovement = new RandomCrossingMovement(reader.getCrossings());
		policeCarRandomMovement.addAgentID(pc.getId());
		pc.setMovementBehavior(policeCarRandomMovement);

		pc.setPathFinder(new KNAStar(70,50));
		pc.getPathFinder().setMap(generateCrossingsMap());
		
		pc.setLocation(model.getCrossings().get(0));
//		pc.setLocation(model.getRoads().get(0));

		agents.add(pc);
	}
	
	private void addFireBrigade(){
		FireFighter fb = new FireFighter();
		RandomCrossingMovement  fireBrigadeRandomMovement = new RandomCrossingMovement(reader.getCrossings());
		fireBrigadeRandomMovement.addAgentID(fb.getId());
		fb.setMovementBehavior(fireBrigadeRandomMovement);
		
		fb.setPathFinder(new KNAStar(70,50));
		fb.getPathFinder().setMap(generateCrossingsMap());
		
		fb.setLocation(model.getCrossings().get(0));
//		fb.setLocation(model.getRoads().get(0));
		
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
	
	private void initializeLocalVariables(){
		agents = new ArrayList<Agent>();
		reader = new CityDataReader();
		model = new WorldModel();
		camera = new Camera();
 
		model.setBuildings(reader.getBuildings());
		model.setRoads(reader.getRoads());
		model.addCrossings(reader.getCrossings());
	}
	
	private void initialize(){
		addKeyListener(this);
		
		initializeLocalVariables();
		
//		generateRandomGoals();
		generateRandomCrossingsGoals();
		
		createAgents();
		startAgentThreads();
		
		camera.setModel(model);
		camera.setAgents(agents);
		

		frameWidth = reader.getCityWidth()*Camera.SCALE + 100;
		frameHeight = reader.getCityHeight()*Camera.SCALE + 100;
		
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
		
		InformationWindow informationWindow = new InformationWindow("Memory Consumption", this.getLocation().x + frameHeight + 10, this.getLocation().y);
		
		// put up some blockades on the roads
//		blockRoadsInit();

		requestFocus();
		
		//keep the simulation running 
		while(true){
				this.repaint();	
		}
	}

	
	/**
	 * Handles all the procedures that needs to be updated in the simulator
	 */
	public void simulate(){
		//currently, nothing needs to be updated from the simulator class
	}

	private void blockRoadsInit(){
		model.getRoads().get(150).block();
		model.getRoads().get(190).block();
		model.getRoads().get(300).block();
		model.getRoads().get(400).block();
		model.getRoads().get(500).block();

		model.getRoads().get(650).block();
		model.getRoads().get(700).block();
		model.getRoads().get(750).block();
		model.getRoads().get(998).block();
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
