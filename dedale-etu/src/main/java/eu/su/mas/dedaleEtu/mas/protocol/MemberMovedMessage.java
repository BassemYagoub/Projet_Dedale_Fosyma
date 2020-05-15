package eu.su.mas.dedaleEtu.mas.protocol;

import java.util.HashMap;

import jade.util.leap.Serializable;
import javafx.util.Pair;

public class MemberMovedMessage implements Serializable{

	private HashMap<String,Pair<String,Long>> agentsPositions;

	public HashMap<String,Pair<String,Long>> getAgentsPositions() {
		return agentsPositions;
	}

	public void setAgentsPositions(HashMap<String,Pair<String,Long>> agentsPositions) {
		this.agentsPositions = agentsPositions;
	}
	
}
