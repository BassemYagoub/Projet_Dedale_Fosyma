package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import eu.su.mas.dedaleEtu.mas.protocol.PingMessage;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;

public class SendingPingMessageBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 8567689731496787661L;

	
	private AgentInformations informations;


	public SendingPingMessageBehaviour(final AbstractDedaleAgent myagent ,AgentInformations informations ) {
		super(myagent);
		this.informations = informations;
	}

	@Override
	public void action() {
		PingMessage message = new PingMessage();
		message.setKey(informations.getAgentKey());
		message.setCoalitionId(informations.coalitionId);
		PacketManager.Send(this.myAgent, informations.getReceivers(),message);
		this.informations.updateCacheReceivers(informations.getReceivers());
		this.informations.state = AgentState.Dispatcher;

	}
	
	public int onEnd() {
		return informations.state.ordinal();
	}
}
