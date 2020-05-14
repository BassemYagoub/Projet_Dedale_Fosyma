package eu.su.mas.dedaleEtu.mas.behaviours;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.protocol.ConversationFinishedMessage;
import eu.su.mas.dedaleEtu.mas.protocol.PingMessage;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.OneShotBehaviour;

public class SendingEndConversationBehaviour extends OneShotBehaviour{

	
	private AgentInformations informations;


	public SendingEndConversationBehaviour(final AbstractDedaleAgent myagent ,AgentInformations informations ) {
		super(myagent);
		this.informations = informations;
	}

	@Override
	public void action() {
		ConversationFinishedMessage message = new ConversationFinishedMessage();
		if(informations.currentConversation != null)
			PacketManager.Send(this.myAgent,informations.currentConversation.getAgentName(),message);
		informations.currentConversation = null;
		this.informations.state = AgentState.Dispatcher;

	}
	
	public int onEnd() {
		return informations.state.ordinal();
	}
}
