package eu.su.mas.dedaleEtu.mas.protocol;

import java.util.ArrayList;

import jade.util.leap.Serializable;

public class BossMovedMessage implements Serializable {

	private String newPosition;
	private String oldPosition;
	private ArrayList<String> nodes;
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
