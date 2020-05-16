package eu.su.mas.dedaleEtu.mas.handlers;

import java.util.ArrayList;
import java.util.Map;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.protocol.AcceptCoalitionMessage;
import eu.su.mas.dedaleEtu.mas.protocol.BossMovedMessage;
import eu.su.mas.dedaleEtu.mas.protocol.MemberMovedMessage;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.CoalitionType;
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
		conditions = MessageTemplate.or(conditions, MessageTemplate.MatchProtocol(ConversationFinishedMessage.class.getSimpleName()));
		conditions = MessageTemplate.or(conditions, MessageTemplate.MatchProtocol(CoalitionInvitationMessage.class.getSimpleName()));
		conditions = MessageTemplate.or(conditions, MessageTemplate.MatchProtocol(AcceptCoalitionMessage.class.getSimpleName()));
		conditions = MessageTemplate.or(conditions, MessageTemplate.MatchProtocol(RequestJoinCoalitionMessage.class.getSimpleName()));
		//conditions = MessageTemplate.or(conditions, MessageTemplate.MatchProtocol(MemberMovedMessage.class.getSimpleName()));
		conditions = MessageTemplate.or(conditions, MessageTemplate.MatchProtocol(CanMoveNearMessage.class.getSimpleName()));
		conditions = MessageTemplate.or(conditions, MessageTemplate.MatchProtocol(SynchronizeGroupMessage.class.getSimpleName()));

		// messages à traiter plus tard
		conditions = MessageTemplate.or(conditions, MessageTemplate.MatchProtocol(BossMovedMessage.class.getSimpleName()));
		conditions = MessageTemplate.or(conditions, MessageTemplate.MatchProtocol(MemberMovedMessage.class.getSimpleName()));

	
		ACLMessage object = myAgent.blockingReceive(conditions, AgentInformations.DefaultTimeOut);
		
		if (object != null) {
			if(object.getProtocol() == BossMovedMessage.class.getSimpleName() 
					|| object.getProtocol() == MemberMovedMessage.class.getSimpleName()) {

				informations.messages.add(object);
				informations.state = AgentState.Dispatcher;
			}else {
				String sender = object.getSender().getLocalName();
				String message  = object.getProtocol();
				// si on est déjà en conversation avec un autre agent et on reçoie un message d'un tout autre agent en push le message et on re attend
				if (informations.currentConversation != null && !informations.currentConversation.getAgentName().equals(sender)) {
					//this.myAgent.putBack(object);
					informations.state = AgentState.Dispatcher;
					
					// si c'est une conversation entre groupe en met en priorité
					if(informations.coalitionId != -1 && object.getProtocol().equals(CanMoveNearMessage.class.getSimpleName())) {
						for(Pair<String , CoalitionType> entry  : informations.coalitionInformations.get(informations.coalitionId)) {
							if(entry.getKey().equals(sender)) {
								this.myAgent.putBack(object); // put first
								informations.state = AgentState.valueOf("Handler" + object.getProtocol());
								break;
							}
						}
					}

				}else {
					
					this.myAgent.putBack(object); // put first
					try {
						informations.state = AgentState.valueOf("Handler" + object.getProtocol());
					}catch(Exception e) {
						informations.state = AgentState.Dispatcher;
					}
				}
				
			}	

		} else {
			informations.state = AgentState.ExploringDispatch;
			informations.currentConversation = null;
			
			//vider  le cache des messages
			informations.clearCacheMessage();
			// l'agent enléve les membres de son groupe dans la liste des receivers
			if(informations.coalitionId != -1) {
				ArrayList<Pair<String,CoalitionType>> members = informations.coalitionInformations.get(informations.coalitionId);
				for(Pair<String,CoalitionType> member : members) {
					informations.removeReceiver(member.getKey());
				}
			}
		}
	}

	@Override
	public int onEnd() {
		return informations.state.ordinal();
	}

}
