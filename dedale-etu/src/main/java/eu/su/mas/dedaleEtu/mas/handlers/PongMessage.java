package eu.su.mas.dedaleEtu.mas.handlers;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.protocol.SynchronizeMessage;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;

public class PongMessage extends OneShotBehaviour{

	
	private String position;
	private AgentInformations informations;
	public PongMessage(final AbstractDedaleAgent myagent , AgentInformations informations) {
		super(myagent);
		this.informations = informations;
	}
	
	
	@Override
	public void action() {
		Pair<eu.su.mas.dedaleEtu.mas.protocol.PongMessage,ACLMessage> object = PacketManager.ReceiveByClassName(this.getClass().getSimpleName(), myAgent);
		if(object.getKey().getSynchronize()) {
			SynchronizeMessage message = new SynchronizeMessage();
			message.setClosedNodes(informations.getClosedNodes());
			message.setKey(informations.getAgentKey());
			message.setEdges(informations.getEdges());
			message.setOpenNodes(informations.openNodes);
			PacketManager.Send(myAgent, object.getValue().getSender().getLocalName(), message,myAgent.getLocalName());
		}
		
		informations.state	= AgentState.Dispatcher;	
	}
	
	@Override
	public int onEnd() {
		return AgentState.Redirect.ordinal();
	}


}