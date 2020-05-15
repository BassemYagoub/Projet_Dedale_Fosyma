package eu.su.mas.dedaleEtu.mas.protocol;

import java.util.HashMap;

import jade.util.leap.Serializable;

public class MemberMovedMessage implements Serializable{

	private HashMap<String,String> agentsPositions;

	public HashMap<String, String> getAgentsPositions() {
		return agentsPositions;
	}

	public void setAgentsPositions(HashMap<String, String> agentsPositions) {
		this.agentsPositions = agentsPositions;
	}
	
}
