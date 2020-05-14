package eu.su.mas.dedaleEtu.mas.handlers;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.protocol.AcceptCoalitionMessage;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import javafx.util.Pair;

public class CoalitionInvitationMessage extends OneShotBehaviour  {
	
	private AgentInformations informations;

	public CoalitionInvitationMessage(final AbstractDedaleAgent myagent, AgentInformations informations) {
		super(myagent);
		this.informations = informations;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		Pair<eu.su.mas.dedaleEtu.mas.protocol.CoalitionInvitationMessage,ACLMessage> object = PacketManager.ReceiveByClassName(this.getClass().getSimpleName(), myAgent);
		//si l'agent accept
		informations.coalitionInformations.addMember(object.getKey().getCoalitionId(), myAgent.getLocalName());
		informations.coalitionId = object.getKey().getCoalitionId();
		informations.bossName = object.getValue().getSender().getLocalName();
		AcceptCoalitionMessage message  = new AcceptCoalitionMessage();
		PacketManager.Send(myAgent,object.getValue().getSender().getLocalName(), message,myAgent.getLocalName());

		informations.state = AgentState.Dispatcher;

	}
	
	public int onEnd() {
		return AgentState.Redirect.ordinal();
	}
}
