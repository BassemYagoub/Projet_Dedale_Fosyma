package eu.su.mas.dedaleEtu.mas.behaviours;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.OneShotBehaviour;

public class CoalitionBehaviour extends OneShotBehaviour {

	
	private AgentInformations informations;


	public CoalitionBehaviour(final AbstractDedaleAgent myagent ,AgentInformations informations ) {
		super(myagent);
		this.informations = informations;
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		if(informations.coalitionId  != -1) {
			if(informations.currentConversation.getCoalitionId() != -1) { // ça sert   à rien de s'inviter chacun à son groupe
				
				//
			}else {
				// si l'autre agent appartient à aucune coalition
				eu.su.mas.dedaleEtu.mas.protocol.CoalitionInvitationMessage message = new eu.su.mas.dedaleEtu.mas.protocol.CoalitionInvitationMessage();
				message.setCoalitionId(informations.coalitionId);
				PacketManager.Send(myAgent,informations.currentConversation.getAgentName(), message,myAgent.getLocalName());
			}
		}else { // j'appartient à aucune coalition
			if(informations.currentConversation.getCoalitionId() != -1) { // l'autre agent à une coalition
				// demande une invitation
				eu.su.mas.dedaleEtu.mas.protocol.RequestJoinCoalitionMessage message = new eu.su.mas.dedaleEtu.mas.protocol.RequestJoinCoalitionMessage();
				PacketManager.Send(myAgent,informations.currentConversation.getAgentName(), message,myAgent.getLocalName());
				System.out.println("TRY REQUEST");
				
			}else { // aucun des agents a une coalition en créer notre propre coalition	
				synchronized(AgentInformations.lock) {
					informations.coalitionId = ++AgentInformations.CoalitionId;
				}
				informations.bossName =  myAgent.getLocalName(); // refactor after
				informations.coalitionInformations.addCoalition(informations.coalitionId, myAgent.getLocalName());
				// on demande si l'autre agent veut rejoindre notre coalition 
				eu.su.mas.dedaleEtu.mas.protocol.CoalitionInvitationMessage message = new eu.su.mas.dedaleEtu.mas.protocol.CoalitionInvitationMessage();
				message.setCoalitionId(informations.coalitionId);
				PacketManager.Send(myAgent,informations.currentConversation.getAgentName(), message,myAgent.getLocalName());
			}
		}
		informations.state = AgentState.Dispatcher;
		
	}
	public int onEnd() {
		return AgentState.Redirect.ordinal();
	}

}
