package eu.su.mas.dedaleEtu.mas.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jade.util.leap.Serializable;
import javafx.util.Pair;

public class BossMovedMessage implements Serializable {

	private String newPosition;
	private String oldPosition;
	private ArrayList<String> nodes;
	private HashMap<String,Pair<String,Long>> agentsPosition;
	
	
	private  ArrayList<String> closedNodes;
	private  HashMap<String,ArrayList<String>> edges;
	private  List<String> openNodes;
	public String getKey() {
		return Key;
	}
	public void setKey(String key) {
		Key = key;
	}
	private String Key;

	public ArrayList<String> getClosedNodes() {
		return closedNodes;
	}
	public void setClosedNodes(ArrayList<String> closedNodes) {
		this.closedNodes = closedNodes;
	}
	public HashMap<String, ArrayList<String>> getEdges() {
		return edges;
	}
	public void setEdges(HashMap<String, ArrayList<String>> edges) {
		this.edges = edges;
	}
	public List<String> getOpenNodes() {
		return openNodes;
	}
	public void setOpenNodes(List<String> openNodes) {
		this.openNodes = openNodes;
	}
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
