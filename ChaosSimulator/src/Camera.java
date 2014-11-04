import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JComponent;

public class Camera extends JComponent {
	private static final long serialVersionUID = 1L;

	private WorldModel model;
	private ArrayList<Agent> agents;
	
	public static int scale = 7;
	private int x = 0;
	private int y = 0;
	long time;
	
	
	
	public Camera(){
		
	}
	
	public void zoomIn(){
		scale++;
		model.updateScaleOnObjects();
	}
	public void zoomOut(){
		scale--;
		model.updateScaleOnObjects();
	}
	
	public void moveRight(){
		x += scale;
		System.out.println("moving right");
	}
	public void moveLeft(){
		x -= scale;
		System.out.println("moving Left");
	}
	
	
	public Camera(ArrayList<Agent> agents){
		this.agents = agents;
	}
	
	public void paint(Graphics g){
		Color oldColor = g.getColor();
		g.setColor(Color.DARK_GRAY);
		for (Building o : model.getBuildings()){
			for(CityObject child : o.getBlocks())
				g.fillRect(x + child.getXPos(), y + child.getYPos(), scale, scale);
		}
		g.setColor(Color.LIGHT_GRAY);
		for (Road road : model.getRoads()){
				g.setColor(road.getColor());
				g.fillRect(x + road.getXPos(), y + road.getYPos(), scale, scale);
		}
		
		for(Agent agent : agents){
			
			if(agent.getTarget() != null){
				if(agent.getPath() != null){
					g.setColor(Color.orange);
					for(CityObject o : agent.getPath())
						g.fillRect(x + o.getXPos(), y + o.getYPos(), scale, scale);
					
					g.setColor(Color.MAGENTA);
					if(agent.getPathFinder().getDebugData() != null)
						for(CityObject o : agent.getPathFinder().getDebugData())
							g.fillRect(x + o.getXPos(), y + o.getYPos(), scale, scale);
				}
				g.setColor(Color.green);
				g.fillRect(x + agent.getTarget().getXPos(), y + agent.getTarget().getYPos(), scale, scale);
			}
			g.setColor(Color.RED);
			g.fillRect(x + agent.getLocation().getXPos(), y + agent.getLocation().getYPos(), scale, scale);
		}
		debug(g);
		g.setColor(oldColor);
	}
	
	private void debug(Graphics g){
//		g.drawString(Integer.toString(agent.getTarget().getId()), agent.getTarget().getX() * Simulator.SCALE + 5, agent.getTarget().getY() * Simulator.SCALE);
		debugCrossings(g);
//		debugCrossingsNeighbours(g);
	}

	/* show all crossings on the map */
	private void debugCrossings(Graphics g){
		g.setColor(Color.blue);
		for (CityObject cross : model.getCrossings())
			g.drawString(Integer.toString(cross.getId()), x + cross.getXPos() + 5, y + cross.getYPos());
	}

	/* show all crossings neighbour crossings */
//	private void debugCrossingsNeighbours(Graphics g){
//		if(count == model.getCrossings().size())
//			count = 0;
//		if(time == 0)
//			time = System.currentTimeMillis();
//		CityObject road = model.getCrossings().get(count);
//		
//		g.setColor(Color.blue);
//		g.fillRect(road.getX(), road.getY(), SCALE, SCALE);
//		
//		if(System.currentTimeMillis() > time + 2500){
//			count++;
//			time = System.currentTimeMillis();
//		}
//	}
	

	public void setModel(WorldModel model) {
		this.model = model;
	}

	public void setAgents(ArrayList<Agent> agents) {
		this.agents = agents;
	}

	
	public void handleKeyEvent(KeyEvent e){
		int key = e.getKeyCode();
	
	    if (key == KeyEvent.VK_LEFT) {
	        moveLeft();
	    }
	
	    if (key == KeyEvent.VK_RIGHT) {
	    	moveRight();
	    }
	
	    if (key == KeyEvent.VK_UP) {

	    }
	
	    if (key == KeyEvent.VK_DOWN) {

	    }
		
	    if (key == KeyEvent.VK_Z) {
	    	zoomIn();
	    }
	    
	    if (key == KeyEvent.VK_X) {
	    	zoomOut();
	    }	

	}
	
}
