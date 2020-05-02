package eu.su.mas.dedaleEtu.mas.handlers;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;

public class DispatcherMessage extends OneShotBehaviour {

	private String position;
	private AgentInformations informations;

	public DispatcherMessage(final AbstractDedaleAgent myagent, AgentInformations informations) {
		super(myagent);
		this.informations = informations;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		MessageTemplate conditions = MessageTemplate.or(
				MessageTemplate.MatchProtocol(IsBusyMessage.class.getSimpleName()),
				MessageTemplate.MatchProtocol(PongMessage.class.getSimpleName()));
		conditions = MessageTemplate.or(conditions,
				MessageTemplate.MatchProtocol(SynchronizeMessage.class.getSimpleName()));


		conditions = MessageTemplate.or(conditions, MessageTemplate.MatchProtocol(PingMessage.class.getSimpleName()));
		ACLMessage object = myAgent.blockingReceive(null, AgentInformations.DefaultTimeOut);
		if (object != null) {
			String sender = object.getSender().getLocalName();
			String message  = object.getProtocol();
			// si on est déjà en conversation avec un autre agent et on reçoie un message d'un tout autre agent en push le message et on re attend
			if (informations.currentConversation != null && !informations.currentConversation.equals(sender)) {
				//this.myAgent.putBack(object);
				informations.state = AgentState.Dispatcher;

			}else {
				this.myAgent.putBack(object); // put first
				informations.state = AgentState.valueOf("Handler" + object.getProtocol());
			}

		} else {
			informations.state = AgentState.Exploring;
			informations.currentConversation = null;
		}
	}

	@Override
	public int onEnd() {
		return informations.state.ordinal();
	}

}
