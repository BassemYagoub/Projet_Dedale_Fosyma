package eu.su.mas.dedaleEtu.mas.handlers;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.protocol.SynchronizeGroupMessage;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.CoalitionState;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import javafx.util.Pair;

public class AcceptCoalitionMessage extends OneShotBehaviour{
	private AgentInformations informations;

	public AcceptCoalitionMessage(final AbstractDedaleAgent myagent, AgentInformations informations) {
		super(myagent);
		this.informations = informations;
	}

	@Override
	public void action() {

		Pair<eu.su.mas.dedaleEtu.mas.protocol.AcceptCoalitionMessage,ACLMessage> object = PacketManager.ReceiveByClassName(this.getClass().getSimpleName(), myAgent);
		// dorenavant l'agent B suit l'agent A
		informations.members.put(object.getValue().getSender().getLocalName(),new Pair<CoalitionState,Integer>(CoalitionState.Ready,0));
		
		informations.state = AgentState.SendingEndConversation;
		
		SynchronizeGroupMessage message = new SynchronizeGroupMessage();
		message.setClosedNodes(informations.getClosedNodes());
		message.setKey(informations.getAgentKey());
		message.setEdges(informations.getEdges());
		message.setOpenNodes(informations.openNodes);
		message.setAgentsPositions(informations.agentsPositions);
		PacketManager.Send(myAgent, object.getValue().getSender().getLocalName(), message,myAgent.getLocalName());

	}
	
	public int onEnd() {
		return AgentState.Redirect.ordinal();
	}

}
