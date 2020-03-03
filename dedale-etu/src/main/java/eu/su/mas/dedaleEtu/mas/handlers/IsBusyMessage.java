package eu.su.mas.dedaleEtu.mas.handlers;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
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

public class IsBusyMessage extends OneShotBehaviour{

	
	private String position;
	private AgentInformations informations;
	public IsBusyMessage(final AbstractDedaleAgent myagent , AgentInformations informations ) {
		super(myagent);
		this.informations = informations;
	}
	
	@Override
	public void action() {		
		informations.state = AgentState.Exploring;	
		System.out.println("IsBusy");
		
	}

	public int onEnd() {
		return AgentState.Redirect.ordinal();
	}

}