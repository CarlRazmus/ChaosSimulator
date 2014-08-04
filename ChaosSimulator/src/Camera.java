import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JComponent;

public class Camera extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private int x;
	private int y;
	private WorldModel model;
	private ArrayList<Agent> agents;
	long time; 
	public static int SCALE = 5;
	
	public Camera(){
		
	}
	
	public Camera(ArrayList<Agent> agents){
		this.agents = agents;
	}
	
	public void paint(Graphics g){
		Color oldColor = g.getColor();
		g.setColor(Color.DARK_GRAY);
		for (Building o : model.getBuildings()){
				for(CityObject child : o.getBlocks())
					g.fillRect(child.getX(), child.getY(), SCALE, SCALE);
		}
		g.setColor(Color.LIGHT_GRAY);
		for (Road road : model.getRoads()){
			if(road.isBlocked()){
				g.setColor(Color.green);
				g.fillRect(road.getX(), road.getY(), SCALE, SCALE);
				g.setColor(Color.LIGHT_GRAY);
			}else
				g.fillRect(road.getX(), road.getY(), SCALE, SCALE);
		}
		
		for(Agent agent : agents){
			
			if(agent.getTarget() != null){
				if(agent.getPath() != null){
					g.setColor(Color.orange);
					for(CityObject o : agent.getPath())
						g.fillRect(o.getX(), o.getY(), SCALE, SCALE);
					
					g.setColor(Color.MAGENTA);
					if(agent.getPathFinder().getDebugData() != null)
						for(CityObject o : agent.getPathFinder().getDebugData())
							g.fillRect(o.getX(), o.getY(), SCALE, SCALE);
				}
				g.setColor(Color.BLACK);
				g.fillRect(agent.getTarget().getX(), agent.getTarget().getY(), SCALE, SCALE);
			}
			g.setColor(Color.RED);
			g.fillRect(agent.getLocation().getX(), agent.getLocation().getY(), SCALE, SCALE);
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
			g.drawString(Integer.toString(cross.getId()), cross.getX() + 5, cross.getY());
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
}
