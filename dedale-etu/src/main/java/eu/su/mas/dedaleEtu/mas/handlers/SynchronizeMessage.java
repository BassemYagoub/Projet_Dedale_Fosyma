package eu.su.mas.dedaleEtu.mas.handlers;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.ams;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;

public class SynchronizeMessage extends OneShotBehaviour{

	
	private String position;
	private AgentInformations informations;
	public SynchronizeMessage(final AbstractDedaleAgent myagent , AgentInformations informations) {
		super(myagent);
		this.informations = informations;
	}
	@Override
	public void action() {
		Pair<eu.su.mas.dedaleEtu.mas.protocol.SynchronizeMessage,ACLMessage> object = PacketManager.ReceiveByClassName(this.getClass().getSimpleName(), myAgent);
		// update agent sender key
		informations.addOrUpdate(object.getValue().getSender().getLocalName(), object.getKey().getKey());
		informations.mergeInformations(object.getKey().getClosedNodes(),object.getKey().getEdges(),object.getKey().getOpenNodes());
		informations.state = AgentState.Exploring;
		informations.currentConversation = null;
		
	}
	
	@Override
	public int onEnd() {
		return AgentState.Redirect.ordinal();
	}
}