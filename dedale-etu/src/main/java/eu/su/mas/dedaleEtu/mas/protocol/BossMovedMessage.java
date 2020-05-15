package eu.su.mas.dedaleEtu.mas.protocol;

import java.util.ArrayList;
import java.util.HashMap;

import jade.util.leap.Serializable;
import javafx.util.Pair;

public class BossMovedMessage implements Serializable {

	private String newPosition;
	private String oldPosition;
	private ArrayList<String> nodes;
	private HashMap<String,Pair<String,Long>> agentsPosition;
	public HashMap<String, Pair<String,Long>> getAgentsPosition() {
		return agentsPosition;
	}
	public void setAgentsPosition(HashMap<String, Pair<String,Long>> agentsPosition) {
		this.agentsPosition = agentsPosition;
	}
	public ArrayList<String> getNodes() {
		return nodes;
	}
	public void setNodes(ArrayList<String> nodes) {
		this.nodes = nodes;
	}
	public String getNewPosition() {
		return newPosition;
	}
	public void setNewPosition(String newPosition) {
		this.newPosition = newPosition;
	}
	public String getOldPosition() {
		return oldPosition;
	}
	public void setOldPosition(String oldPosition) {
		this.oldPosition = oldPosition;
	}
}
