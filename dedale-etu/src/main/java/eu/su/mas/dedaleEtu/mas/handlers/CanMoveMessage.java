package eu.su.mas.dedaleEtu.mas.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.protocol.MemberMovedMessage;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.CoalitionState;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import javafx.util.Pair;

public class CanMoveMessage extends OneShotBehaviour {
	private AgentInformations informations;

	public CanMoveMessage(final AbstractDedaleAgent myagent, AgentInformations informations) {
		super(myagent);
		this.informations = informations;
	}

	@Override
	public void action() {
		Pair<eu.su.mas.dedaleEtu.mas.protocol.CanMoveMessage, ACLMessage> object = PacketManager
				.ReceiveByClassName(this.getClass().getSimpleName(), myAgent);

		String nextNode = informations.myMap.getNearestNode(informations.myPosition, object.getKey().getPosition(),
				new ArrayList<Pair<String, Long>>(informations.agentsPosition.values()));
		if (nextNode != null) {

			if (((AbstractDedaleAgent) this.myAgent).moveTo(nextNode)) {
				informations.myPosition = nextNode;
				informations.addOrUpdateAgentPosition(myAgent.getLocalName(),
						new Pair<String, Long>(informations.myPosition, System.currentTimeMillis()));

			} else {
				informations.addOrUpdateAgentPosition(myAgent.getLocalName(),
						new Pair<String, Long>(informations.myPosition, System.currentTimeMillis()));
				if (myAgent.getLocalName() == informations.bossName)
					return;
				if (informations.members.size() <= 0) {
					ArrayList<String> blockedPositions = new ArrayList<String>();
					blockedPositions.add(nextNode);
					MemberMovedMessage message = new MemberMovedMessage();
					informations.addOrUpdateAgentPosition(myAgent.getLocalName(),
							new Pair<String, Long>(informations.myPosition, System.currentTimeMillis()));
					message.setAgentsPositions(informations.agentsPosition);
					message.setBlockedPositions(blockedPositions);
					PacketManager.Send(this.myAgent, informations.bossName, message);
				} else {
					for (Map.Entry<String, Pair<CoalitionState, Integer>> iterator : informations.members.entrySet()) {
						eu.su.mas.dedaleEtu.mas.protocol.CanMoveNearMessage message = new eu.su.mas.dedaleEtu.mas.protocol.CanMoveNearMessage();
						message.setPosition(informations.agentsPosition.get(iterator.getKey()).getKey());

						PacketManager.Send(this.myAgent, iterator.getKey(), message);
						informations.members.put(iterator.getKey(),
								new Pair<CoalitionState, Integer>(CoalitionState.Waiting, 0));
					}
				}
			}
		}
		informations.state=AgentState.Dispatcher;

	}



	public int onEnd() {
		return AgentState.Redirect.ordinal();
	}

}