package eu.su.mas.dedaleEtu.mas.protocol;

import java.util.HashMap;

import jade.util.leap.Serializable;
import javafx.util.Pair;

public class CanMoveNearMessage implements Serializable{

	private String position;
	private HashMap<String,Pair<String,Long>> agentsPositions; 
	public HashMap<String, Pair<String, Long>> getAgentsPositions() {
		return agentsPositions;
	}

	public void setAgentsPositions(HashMap<String, Pair<String, Long>> agentsPositions) {
		this.agentsPositions = agentsPositions;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
}
