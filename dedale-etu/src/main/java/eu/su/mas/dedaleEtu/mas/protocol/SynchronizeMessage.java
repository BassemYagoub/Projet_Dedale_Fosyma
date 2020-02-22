package eu.su.mas.dedaleEtu.mas.protocol;

import java.util.List;
import java.util.Set;

import jade.util.leap.Serializable;

public class SynchronizeMessage implements Serializable{
	
	public Set<String> getClosedNodes() {
		return closedNodes;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setClosedNodes(Set<String> closedNodes) {
		this.closedNodes = closedNodes;
	}

	private  Set<String> closedNodes;
	private  String 		key;
	
}
