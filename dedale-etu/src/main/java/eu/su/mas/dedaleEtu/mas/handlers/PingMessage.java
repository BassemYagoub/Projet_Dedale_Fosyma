package eu.su.mas.dedaleEtu.mas.handlers;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.protocol.PongMessage;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;

public class PingMessage extends CyclicBehaviour{

	
	private String position;
	private AgentInformations informations;
	public PingMessage(final AbstractDedaleAgent myagent , AgentInformations informations) {
		super(myagent);
		this.informations = informations;
	}
	@Override
	public void action() {
		
		Pair<eu.su.mas.dedaleEtu.mas.protocol.PingMessage,ACLMessage> object = PacketManager.ReceiveByClassName(this.getClass().getSimpleName(), myAgent);

		if(object != null )
		{
			PongMessage message  = new PongMessage();
			// if we did not found
			if(!informations.addOrUpdate(object.getValue().getSender().getLocalName(), object.getKey().getKey())) {

				message.setSynchronize(true);
			}else {
				message.setSynchronize(false);
			}
			
			informations.state = AgentState.Pending;
			PacketManager.Send(myAgent,object.getValue().getSender().getLocalName(), message);
		}else
		{
			block();
		}
		
	}

}