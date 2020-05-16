package eu.su.mas.dedaleEtu.mas.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import jade.util.leap.Serializable;

import javafx.util.Pair;

public class SynchronizeGroupMessage implements Serializable {
	public ArrayList<String> getClosedNodes() {
		return closedNodes;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
	
	private  List<String> openNodes;
	
	public List<String> getOpenNodes() {
		return openNodes;
	}

	public void setOpenNodes(List<String> openNodes) {
		this.openNodes = openNodes;
	}

	public Boolean getSendMessage() {
		return SendMessage;
	}

	public void setSendMessage(Boolean SendMessage) {
		this.SendMessage = SendMessage;
	}
	
	private HashMap<String,Pair<String,Long>> agentsPositions;
	public HashMap<String, Pair<String, Long>> getAgentsPositions() {
		return agentsPositions;
	}

	public void setAgentsPositions(HashMap<String, Pair<String, Long>> agentsPositions) {
		this.agentsPositions = agentsPositions;
	}

	private  ArrayList<String> closedNodes;
	private  HashMap<String,ArrayList<String>> edges;
	private  String 		key;
	private  Boolean SendMessage;
}
