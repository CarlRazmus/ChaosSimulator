import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;



public class Simulator extends JFrame implements KeyListener {
	private CityDataReader reader;
	static protected WorldModel model;
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
		

		frameWidth = 800;//reader.getCityWidth()*SCALE + 20;
		frameHeight = 600;//reader.getCityHeight()*SCALE + 20;
		
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setSize(frameWidth, frameHeight);
		this.add(camera);
		
		
		// put up some blockades on the roads
		model.getRoads().get(150).setBlocked(true);
		model.getRoads().get(190).setBlocked(true);
		model.getRoads().get(300).setBlocked(true);
		model.getRoads().get(400).setBlocked(true);
		model.getRoads().get(500).setBlocked(true);

		model.getRoads().get(650).setBlocked(true);
		model.getRoads().get(700).setBlocked(true);
		model.getRoads().get(750).setBlocked(true);
		model.getRoads().get(998).setBlocked(true);
		

		requestFocus();
		
		long time = System.currentTimeMillis();
		while(true){
			if(System.currentTimeMillis() > time + 150){
				simulate();
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
		System.out.println("HAHAHAHAHAHAHAHAH");
	}
}