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

public class CanMoveNearMessage extends OneShotBehaviour {
	private AgentInformations informations;

	public CanMoveNearMessage(final AbstractDedaleAgent myagent, AgentInformations informations) {
		super(myagent);
		this.informations = informations;
	}

	@Override
	public void action() {
		Pair<eu.su.mas.dedaleEtu.mas.protocol.CanMoveNearMessage, ACLMessage> object = PacketManager
				.ReceiveByClassName(this.getClass().getSimpleName(), myAgent);

		//informations.numberReceiveBossMessage = 0;
		// si c'est l'agent avec la position que on doit bouger alors on bouge
		if (object.getKey().getPosition().equals(this.informations.myPosition)) {
			List<Couple<String, List<Couple<Observation, Integer>>>> lobs = ((AbstractDedaleAgent) this.myAgent)
					.observe();
			Iterator<Couple<String, List<Couple<Observation, Integer>>>> iter = lobs.iterator();
			Boolean isMoved = false;
			String nodeId = "";
			ArrayList<String> blockedPositions = new ArrayList<String>();
			while (iter.hasNext()) {
				nodeId = iter.next().getLeft();
				if(nodeId == informations.myPosition)
					continue;
				isMoved = ((AbstractDedaleAgent) this.myAgent).moveTo(nodeId);
				if (isMoved) {
					break;
				}else {
					blockedPositions.add(nodeId);
				}
			}
			
			if(isMoved) {
				informations.myPosition = nodeId;

				informations.addOrUpdateAgentPosition(myAgent.getLocalName(),new Pair<String,Long>(informations.myPosition,System.currentTimeMillis()));
				
				if(myAgent.getLocalName() == informations.bossName)
					return;
				MemberMovedMessage message = new MemberMovedMessage();
				message.setKey(informations.getAgentKey());

				if(!informations.isSameKey(informations.getAgentKey(), informations.getAgentKey())) {
					/*message.setClosedNodes(informations.getClosedNodes());
					message.setEdges(informations.getEdges());
					message.setOpenNodes(informations.openNodes);*/
				}
				
				message.setAgentsPositions(informations.agentsPosition);
				PacketManager.Send(this.myAgent, informations.bossName, message);
			}else {
				informations.addOrUpdateAgentPosition(myAgent.getLocalName(),new Pair<String,Long>(informations.myPosition,System.currentTimeMillis()));
				if(myAgent.getLocalName() == informations.bossName)
					return;
				if(informations.members.size() <= 0) {
					MemberMovedMessage message = new MemberMovedMessage();
					informations.addOrUpdateAgentPosition(myAgent.getLocalName(),new Pair<String,Long>(informations.myPosition,System.currentTimeMillis()));
					message.setAgentsPositions(informations.agentsPosition);
					message.setBlockedPositions(blockedPositions);
					PacketManager.Send(this.myAgent, informations.bossName, message);
				}else {
					for (Map.Entry<String, Pair<CoalitionState, Integer>> iterator : informations.members
							.entrySet()) {
						eu.su.mas.dedaleEtu.mas.protocol.CanMoveNearMessage message = new eu.su.mas.dedaleEtu.mas.protocol.CanMoveNearMessage();
						message.setPosition(informations.agentsPosition.get(iterator.getKey()).getKey());
						
						PacketManager.Send(this.myAgent, iterator.getKey(), message);
						informations.members.put(iterator.getKey(),
								new Pair<CoalitionState, Integer>(CoalitionState.Waiting, 0));
					}
				}

			}
			
		} else {
			if (informations.members.size() <= 0) {
				
				MemberMovedMessage message = new MemberMovedMessage();
				informations.addOrUpdateAgentPosition(myAgent.getLocalName(),new Pair<String,Long>(informations.myPosition,System.currentTimeMillis()));
				message.setAgentsPositions(informations.agentsPosition);
				PacketManager.Send(this.myAgent, informations.bossName, message);
				if(myAgent.getLocalName() == informations.bossName)
					return;
			} else {
				if(myAgent.getLocalName() == informations.bossName)
					return;
				// sinon on broadcast
				for (Map.Entry<String, Pair<CoalitionState, Integer>> iterator : informations.members.entrySet()) {
					eu.su.mas.dedaleEtu.mas.protocol.CanMoveNearMessage message = new eu.su.mas.dedaleEtu.mas.protocol.CanMoveNearMessage();
					message.setPosition(object.getKey().getPosition());
					message.setAgentsPositions(informations.agentsPosition);
					PacketManager.Send(this.myAgent, iterator.getKey(), message);
					informations.members.put(iterator.getKey(),
							new Pair<CoalitionState, Integer>(CoalitionState.Waiting, 0));
				}
			}

		}

		informations.state = AgentState.Dispatcher;

	}

	public int onEnd() {
		return AgentState.Redirect.ordinal();
	}

}