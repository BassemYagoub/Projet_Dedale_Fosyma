package eu.su.mas.dedaleEtu.mas.protocol;

import jade.util.leap.Serializable;

public class PongMessage implements Serializable{

	public Boolean getSynchronize() {
		return synchronize;
	}
	public void setSynchronize(Boolean synchronize) {
		this.synchronize = synchronize;
	}
	public Boolean synchronize;
}
