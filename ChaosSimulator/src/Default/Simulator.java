package Default;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import AI.Agent;
import AI.FireFighter;
import Behaviours.RandomMovement;
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
//	public static final int DEFAULT_SIZE = 5;
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
		addKeyListener(this);
		
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
		model.getRoads().get(150).block();
		model.getRoads().get(190).block();
		model.getRoads().get(300).block();
		model.getRoads().get(400).block();
		model.getRoads().get(500).block();

		model.getRoads().get(650).block();
		model.getRoads().get(700).block();
		model.getRoads().get(750).block();
		model.getRoads().get(998).block();
		

		requestFocus();
		
		long time = System.currentTimeMillis();
		while(true){
			if(System.currentTimeMillis() > time + 150){
				simulate();
				informationWindow.updateGraph();
				time = System.currentTimeMillis();
				this.repaint();	
			}
		}
	}
	
	public void simulate()
	{
		for (Agent agent : agents){
			agent.think();
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
