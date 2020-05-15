package eu.su.mas.dedaleEtu.mas.handlers;

import java.util.Map;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.CoalitionState;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import javafx.util.Pair;

public class SynchronizeGroupMessage extends OneShotBehaviour {
	private AgentInformations informations;

	public SynchronizeGroupMessage(final AbstractDedaleAgent myagent, AgentInformations informations) {
		super(myagent);
		this.informations = informations;
	}

	@Override
	public void action() {

		Pair<eu.su.mas.dedaleEtu.mas.protocol.SynchronizeGroupMessage,ACLMessage> object = PacketManager.ReceiveByClassName(this.getClass().getSimpleName(), myAgent);
		// update agent sender key
		informations.addOrUpdate(object.getValue().getSender().getLocalName(), object.getKey().getKey());
		informations.mergeInformations(object.getKey().getClosedNodes(),object.getKey().getEdges(),object.getKey().getOpenNodes());
		
		for (Map.Entry<String, Pair<String,Long>> i : object.getKey().getAgentsPositions().entrySet()) {
			informations.agentsPositions.put(i.getKey(), i.getValue());
		}
		
		informations.state = AgentState.Dispatcher;


	}
	
	public int onEnd() {
		return AgentState.Redirect.ordinal();
	}

}
