package eu.su.mas.dedaleEtu.mas.protocol;

import jade.util.leap.Serializable;

public class CanMoveNearMessage implements Serializable{

	private String position;

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
}
