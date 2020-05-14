package eu.su.mas.dedaleEtu.mas.protocol;

import jade.util.leap.Serializable;

public class CoalitionInvitationMessage  implements Serializable{
	private Integer coalitionId;

	public Integer getCoalitionId() {
		return coalitionId;
	}

	public void setCoalitionId(Integer coalitionId) {
		this.coalitionId = coalitionId;
	}
	
}
