package eu.su.mas.dedaleEtu.mas.protocol;

import java.util.ArrayList;
import java.util.HashMap;

import jade.util.leap.Serializable;

public class BossMovedMessage implements Serializable {

	private String newPosition;
	private String oldPosition;
	private ArrayList<String> nodes;
	private HashMap<String,String> agentsPosition;
	public HashMap<String, String> getAgentsPosition() {
		return agentsPosition;
	}
	public void setAgentsPosition(HashMap<String, String> agentsPosition) {
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
