
public abstract class MovementBehavior {
	private Agent agent;
	
	public abstract CityObject calculateTarget(CityObject location);

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

}
