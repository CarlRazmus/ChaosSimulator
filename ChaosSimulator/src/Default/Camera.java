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
	
	public static int SCALE = 5;
	public static final int BASIC_MODE = 1;
	public static final int ROUND_MODE = 2;

	private WorldModel model;
	private ArrayList<Agent> agents;
	
	private int x = 0;
	private int y = 0;
	private int mode = BASIC_MODE;
	
	
	
	public void zoomIn(){
		SCALE++;
		model.updateScaleOnObjects();
	}
	public void zoomOut(){
		SCALE--;
		model.updateScaleOnObjects();
	}
	
	public void moveRight(){
		x += 3*SCALE;
	}
	public void moveLeft(){
		x -= 3*SCALE;
	}
	public void moveUp(){
		y -= 3*SCALE;
	}
	public void moveDown(){
		y += 3*SCALE;
	}

	//TODO define different paintingModes (create classes for the different modes if you create new modes)
	public void paint(Graphics g){
		switch (mode) {
		case BASIC_MODE:
			paintSimpleGraphics(g);
			break;
		case ROUND_MODE:
			paintRoundGraphics(g);
			break;
		default:
			break;
		}
	}
	
	/**
	 * just a dummy function, only created to show the general idea behind the different painting modes
	 * @param g
	 */
	private void paintRoundGraphics(Graphics g) {
		
	}

	private void paintSimpleGraphics(Graphics g){
		Color oldColor = g.getColor();
		g.setColor(Color.DARK_GRAY);
		for (Building o : model.getBuildings()){
			for(CityObject child : o.getBlocks())
				g.fillRect(x + child.getXPos(), y + child.getYPos(), SCALE, SCALE);
		}
		g.setColor(Color.LIGHT_GRAY);
		for (Road road : model.getRoads()){
				g.setColor(road.getColor());
				g.fillRect(x + road.getXPos(), y + road.getYPos(), SCALE, SCALE);
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
				g.fillRect(x + agent.getTarget().getXPos(), y + agent.getTarget().getYPos(), SCALE, SCALE);
			}
			g.setColor(agent.getColor());
			g.fillRoundRect(x + agent.getLocation().getXPos(), y + agent.getLocation().getYPos(), SCALE + 10, SCALE + 10, 20, 20);
//			g.fillRect(x + agent.getXPos(), y + agent.getYPos(), scale, scale);
		}
		debug(g);
		g.setColor(oldColor);
	}
	
	
	private void debug(Graphics g){
//		g.drawString(Integer.toString(agent.getTarget().getId()), agent.getTarget().getX() * Simulator.SCALE + 5, agent.getTarget().getY() * Simulator.SCALE);
		debugCrossings(g);
	}

	/* show all crossings on the map */
	private void debugCrossings(Graphics g){
		g.setColor(Color.blue);
		for (CityObject cross : model.getCrossings())
			g.drawString(Integer.toString(cross.getId()), x + cross.getXPos() + 5, y + cross.getYPos());
	}


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
