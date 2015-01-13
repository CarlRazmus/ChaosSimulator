package Default;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JComponent;

import AI.Agent;
import WorldClasses.Building;
import WorldClasses.CityObject;
import WorldClasses.Road;

public class Camera extends JComponent {
	private static final long serialVersionUID = 1L;

	private WorldModel model;
	private ArrayList<Agent> agents;
	
	public static int scale = 3;
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
	}
	public void moveLeft(){
		x -= scale;
	}
	public void moveUp(){
		y -= scale;
	}
	public void moveDown(){
		y += scale;
	}
	public Camera(ArrayList<Agent> agents){
		this.agents = agents;
	}
	
	public void paint(Graphics g){
		//*
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
			/*if(agent.getPath() != null){
//				g.setColor(Color.orange);
				g.setColor(new Color(10, 150, 150, 40));
				for(CityObject o : agent.getPath())
					g.fillRect(x + o.getXPos(), y + o.getYPos(), scale, scale);
				
				if(agent.getPathFinder().getDebugData() != null){
					g.setColor(Color.MAGENTA);
					for(CityObject o : agent.getPathFinder().getDebugData())
						g.fillRect(x + o.getXPos(), y + o.getYPos(), scale, scale);
				}
			}*/
			if(agent.getTarget() != null){	
				g.setColor(Color.green);
				g.fillRect(x + agent.getTarget().getXPos(), y + agent.getTarget().getYPos(), scale, scale);
			}
			g.setColor(agent.getColor());
			g.fillRoundRect(x + agent.getLocation().getXPos(), y + agent.getLocation().getYPos(), scale + 10, scale + 10, 20, 20);
//			g.fillRect(x + agent.getXPos(), y + agent.getYPos(), scale, scale);
		}
		debug(g);
		g.setColor(oldColor);
		//*/
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

	/* show all crossings neighbour crossings 
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
	*/

	public void setModel(WorldModel model) {
		this.model = model;
	}

	public void setAgents(ArrayList<Agent> agents) {
		this.agents = agents;
	}

	
	public void handleKeyEvent(KeyEvent e){
		int key = e.getKeyCode();

		switch (key) {
		case KeyEvent.VK_LEFT:
			moveRight();
			break;
		case KeyEvent.VK_RIGHT:
			moveLeft();
			break;
		case KeyEvent.VK_UP:
	    	moveDown();
	    	break;
		case KeyEvent.VK_DOWN:
		    moveUp();
			break;
		case KeyEvent.VK_Z:
	    	zoomIn();
		    break;
		case KeyEvent.VK_X:
	    	zoomOut();
			break;
		default:
			break;
		}
	}
	
}
