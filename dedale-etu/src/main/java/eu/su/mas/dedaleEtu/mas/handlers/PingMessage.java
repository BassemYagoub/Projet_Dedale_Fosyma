package eu.su.mas.dedaleEtu.mas.handlers;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.knowledge.ConversationInformations;
import eu.su.mas.dedaleEtu.mas.protocol.PongMessage;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;

public class PingMessage extends OneShotBehaviour{

	
	private String position;
	private AgentInformations informations;
	public PingMessage(final AbstractDedaleAgent myagent , AgentInformations informations ) {
		super(myagent);
		this.informations = informations;
	}
	
	@Override
	public void action() {

		Pair<eu.su.mas.dedaleEtu.mas.protocol.PingMessage,ACLMessage> object = PacketManager.ReceiveByClassName(this.getClass().getSimpleName(), myAgent);
		PongMessage message  = new PongMessage();
		//wasn't same key
		//if(!informations.addOrUpdate(object.getValue().getSender().getLocalName(), object.getKey().getKey())) {
		if(!informations.isSameKey(object.getValue().getSender().getLocalName(), object.getKey().getKey())) {
			// then we need synchronize
			message.setSynchronize(true);
		}else {
			message.setSynchronize(false);
		}
		message.setCoalitionId(informations.coalitionId);
		long timeSenderSendedMessage = object.getValue().getPostTimeStamp();
		long agentSentMessage  = informations.getCacheMessage(object.getValue().getSender().getLocalName());
		if(agentSentMessage == -1 || (agentSentMessage > timeSenderSendedMessage)) {
			PacketManager.Send(myAgent,object.getValue().getSender().getLocalName(), message,myAgent.getLocalName());
			//informations.currentConversation = object.getValue().getSender().getLocalName();
			informations.currentConversation  =  new ConversationInformations();
			informations.currentConversation.setAgentName(object.getValue().getSender().getLocalName());
			informations.currentConversation.setCoalitionId(object.getKey().getCoalitionId());
		}else {
			// si tout de même ils ont envoyer en même moment en fait une comparaison par nom
			if(agentSentMessage == timeSenderSendedMessage && this.myAgent.getLocalName().compareTo(object.getValue().getSender().getLocalName()) >= 1) {
				PacketManager.Send(myAgent,object.getValue().getSender().getLocalName(), message,myAgent.getLocalName());
				informations.currentConversation  =  new ConversationInformations();
				informations.currentConversation.setAgentName(object.getValue().getSender().getLocalName());
				informations.currentConversation.setCoalitionId(object.getKey().getCoalitionId());
			}else {
				//si les agents sont déjà synchroniser 
			}
		}
		

		informations.state = AgentState.Dispatcher;	
		//informations.AgentKnowing.add(object.getValue().getSender().getLocalName());
		
	}

	public int onEnd() {
		return AgentState.Redirect.ordinal();
	}

}