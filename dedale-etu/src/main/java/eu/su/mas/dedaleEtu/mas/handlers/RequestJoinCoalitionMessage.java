package eu.su.mas.dedaleEtu.mas.handlers;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.protocol.AcceptCoalitionMessage;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import javafx.util.Pair;

public class RequestJoinCoalitionMessage extends OneShotBehaviour{
	private AgentInformations informations;

	public RequestJoinCoalitionMessage(final AbstractDedaleAgent myagent, AgentInformations informations) {
		super(myagent);
		this.informations = informations;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		Pair<eu.su.mas.dedaleEtu.mas.protocol.RequestJoinCoalitionMessage,ACLMessage> object = PacketManager.ReceiveByClassName(this.getClass().getSimpleName(), myAgent);
		
		eu.su.mas.dedaleEtu.mas.protocol.CoalitionInvitationMessage message = new eu.su.mas.dedaleEtu.mas.protocol.CoalitionInvitationMessage();
		message.setCoalitionId(informations.coalitionId);
		PacketManager.Send(myAgent,informations.currentConversation.getAgentName(), message,myAgent.getLocalName());
		informations.state = AgentState.Dispatcher;

	}
	
	public int onEnd() {
		return AgentState.Redirect.ordinal();
	}
}
