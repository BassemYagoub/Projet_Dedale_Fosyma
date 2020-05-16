package eu.su.mas.dedaleEtu.mas.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jade.util.leap.Serializable;
import javafx.util.Pair;

public class MemberMovedMessage implements Serializable{

	private HashMap<String,Pair<String,Long>> agentsPositions;
	private ArrayList<String> blockedPositions;
	
	private  ArrayList<String> closedNodes;
	private  HashMap<String,ArrayList<String>> edges;
	private  List<String> openNodes;
	private String Key;

	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

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

	public ArrayList<String> getBlockedPositions() {
		return blockedPositions;
	}

	public void setBlockedPositions(ArrayList<String> blockedPositions) {
		this.blockedPositions = blockedPositions;
	}

	public HashMap<String,Pair<String,Long>> getAgentsPositions() {
		return agentsPositions;
	}

	public void setAgentsPositions(HashMap<String,Pair<String,Long>> agentsPositions) {
		this.agentsPositions = agentsPositions;
	}
	
}
