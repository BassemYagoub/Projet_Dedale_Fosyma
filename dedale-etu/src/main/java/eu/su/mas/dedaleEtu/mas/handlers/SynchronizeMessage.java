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
	
		//pour savoir si en renvoie le sync au sender ou pas
		if(object.getKey().getSendMessage()) {
			eu.su.mas.dedaleEtu.mas.protocol.SynchronizeMessage message = new eu.su.mas.dedaleEtu.mas.protocol.SynchronizeMessage();
			message.setClosedNodes(informations.getClosedNodes());
			message.setKey(informations.getAgentKey());
			message.setEdges(informations.getEdges());
			message.setOpenNodes(informations.openNodes);
			message.setSendMessage(false); // pour ne pas recevoir une deuxiéme synchro
			PacketManager.Send(myAgent, object.getValue().getSender().getLocalName(), message,myAgent.getLocalName());

		}else {
				
		}
			
		
		informations.state = AgentState.Dispatcher;
		//libére la conversation
		//informations.currentConversation = null;
	//	System.out.println("agent n = "+this.myAgent.getLocalName() + " nombres closed nodes : " + informations.getClosedNodes().size());
		
	}
	
	@Override
	public int onEnd() {
		return AgentState.Redirect.ordinal();
	}
}