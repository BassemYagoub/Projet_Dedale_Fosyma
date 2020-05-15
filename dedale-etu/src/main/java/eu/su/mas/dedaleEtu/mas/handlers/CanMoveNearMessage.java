package eu.su.mas.dedaleEtu.mas.handlers;

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

		// si c'est l'agent avec la position que on doit bouger alors on bouge
		if (object.getKey().getPosition() == this.informations.myPosition) {
			List<Couple<String, List<Couple<Observation, Integer>>>> lobs = ((AbstractDedaleAgent) this.myAgent)
					.observe();
			Iterator<Couple<String, List<Couple<Observation, Integer>>>> iter = lobs.iterator();
			while (iter.hasNext()) {
				String nodeId = iter.next().getLeft();
				if (((AbstractDedaleAgent) this.myAgent).moveTo(nodeId)) {
					informations.myPosition = nodeId;
					break;
				}
			}

			informations.agentsPositions.put(myAgent.getLocalName(), new Pair<String,Long>(informations.myPosition,System.currentTimeMillis()));
			MemberMovedMessage message = new MemberMovedMessage();
			message.setAgentsPositions(informations.agentsPositions);
			PacketManager.Send(this.myAgent, informations.bossName, message);
		} else {
			if (informations.members.size() <= 0) {
				
				MemberMovedMessage message = new MemberMovedMessage();
				message.setAgentsPositions(informations.agentsPositions);
				PacketManager.Send(this.myAgent, informations.bossName, message);
			} else {
				// sinon on broadcast
				for (Map.Entry<String, Pair<CoalitionState, Integer>> iterator : informations.members.entrySet()) {
					eu.su.mas.dedaleEtu.mas.protocol.CanMoveNearMessage message = new eu.su.mas.dedaleEtu.mas.protocol.CanMoveNearMessage();
					message.setPosition(object.getKey().getPosition());
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