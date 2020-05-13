package eu.su.mas.dedaleEtu.mas.handlers;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ConversationFinishedMessage extends OneShotBehaviour{
	
	private String position;
	private AgentInformations informations;

	public ConversationFinishedMessage(final AbstractDedaleAgent myagent, AgentInformations informations) {
		super(myagent);
		this.informations = informations;
	}

	@Override
	public void action() {
		informations.state = AgentState.Dispatcher;
		informations.currentConversation = null;
	}

	@Override
	public int onEnd() {
		return informations.state.ordinal();
	}
}
