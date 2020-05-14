package eu.su.mas.dedaleEtu.mas.handlers;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javafx.util.Pair;

public class ConversationFinishedMessage extends OneShotBehaviour{
	
	private String position;
	private AgentInformations informations;

	public ConversationFinishedMessage(final AbstractDedaleAgent myagent, AgentInformations informations) {
		super(myagent);
		this.informations = informations;
	}

	@Override
	public void action() {
		Pair<eu.su.mas.dedaleEtu.mas.protocol.ConversationFinishedMessage,ACLMessage> object = PacketManager.ReceiveByClassName(this.getClass().getSimpleName(), myAgent);

		informations.currentConversation = null;
		informations.state = AgentState.Dispatcher;
	}

	@Override
	public int onEnd() {
		return AgentState.Redirect.ordinal();
	}
}
