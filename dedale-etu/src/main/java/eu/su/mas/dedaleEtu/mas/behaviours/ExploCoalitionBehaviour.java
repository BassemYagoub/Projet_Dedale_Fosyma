package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.handlers.IsBusyMessage;
import eu.su.mas.dedaleEtu.mas.handlers.PongMessage;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.protocol.BossMovedMessage;
import eu.su.mas.dedaleEtu.mas.protocol.CanMoveNearMessage;
import eu.su.mas.dedaleEtu.mas.protocol.MemberMovedMessage;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.CoalitionState;
import eu.su.mas.dedaleEtu.mas.toolBox.CoalitionType;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javafx.util.Pair;

public class ExploCoalitionBehaviour extends OneShotBehaviour {
	private AgentInformations informations;

	public ExploCoalitionBehaviour(final AbstractDedaleAgent myagent, AgentInformations informations) {
		super(myagent);
		this.informations = informations;
	}

	@Override
	public void action() {
		Boolean canMove = false;
		String nextNode = null;
		ArrayList<String> nodesArounds = null;
		if (this.informations.bossName == this.myAgent.getLocalName()) {
			canMove = true;
			/*
			 * if (this.informations.openNodes.size() > 0) { if (informations.nextNode ==
			 * null) { // update we need random here
			 * 
			 * informations.nextNode = this.informations.myMap
			 * .getShortestPath(informations.myPosition,
			 * this.informations.openNodes.get(0)).get(0);
			 * informations.nodes.add(informations.nextNode); } }
			 */
			// revoir
			nodesArounds = informations.nodes;

			// int index = ThreadLocalRandom.current().nextInt(0,
			// informations.nodes.size());
			// nextNode = informations.nodes.get(index);
		} else {
			// en vérifie si l'agent à déjà envoyer un message au autre agent si ce'st le
			// cas pas besoin d'attendre
			// un message de son boss
			for (Map.Entry<String, Pair<CoalitionState, Integer>> entry : informations.members.entrySet()) {
				if (entry.getValue().getKey() == CoalitionState.Waiting)
					canMove = true;
				break;
			}

			// si on reçoie un message du boss pour commencer à ce déplacer
			if (!canMove) {
				MessageTemplate condition = MessageTemplate.MatchProtocol(BossMovedMessage.class.getSimpleName());
				ACLMessage object = myAgent.blockingReceive(condition, AgentInformations.DefaultTimeOut);

				if (object != null) {
					this.myAgent.putBack(object); // put first
					Pair<eu.su.mas.dedaleEtu.mas.protocol.BossMovedMessage, ACLMessage> obj = PacketManager
							.ReceiveByClassName(eu.su.mas.dedaleEtu.mas.protocol.BossMovedMessage.class.getSimpleName(),
									myAgent);
					for (Map.Entry<String, Pair<String,Long>> i : obj.getKey().getAgentsPosition().entrySet()) {
						informations.agentsPositions.put(i.getKey(), new Pair<String,Long>(i.getValue().getKey(),i.getValue().getValue()));
					}
					nodesArounds = obj.getKey().getNodes();
					// informations.nextNode = t
					canMove = true;
				}
			}
		}

		// TODO Auto-generated method stub
		if (canMove) {
			Boolean isMemberWaiting = false;
			Boolean memberUpdated = false;
			// boucle d'attente de recpection du Member movedMessage
			for (Map.Entry<String, Pair<CoalitionState, Integer>> entry : informations.members.entrySet()) {
				MessageTemplate condition = MessageTemplate.MatchProtocol(MemberMovedMessage.class.getSimpleName());
				ACLMessage object = myAgent.blockingReceive(condition, AgentInformations.DefaultTimeOut);

				if (object != null) {
					this.myAgent.putBack(object); // put first
					Pair<eu.su.mas.dedaleEtu.mas.protocol.MemberMovedMessage, ACLMessage> obj = PacketManager
							.ReceiveByClassName(
									eu.su.mas.dedaleEtu.mas.protocol.MemberMovedMessage.class.getSimpleName(), myAgent);
					informations.members.put(obj.getValue().getSender().getLocalName(),
							new Pair<CoalitionState, Integer>(CoalitionState.Ready, 0));

					for (Map.Entry<String, Pair<String,Long>> i : obj.getKey().getAgentsPositions().entrySet()) {
						informations.agentsPositions.put(i.getKey(), i.getValue());
					}
					memberUpdated = true;
				}
			}
			// on vérifie ici si l'agent attend encore un des membres du groupe alors en
			// renvoie un message
			for (Map.Entry<String, Pair<CoalitionState, Integer>> entry : informations.members.entrySet()) {
				if (entry.getValue().getKey() == CoalitionState.Waiting) {
					isMemberWaiting = true;
					// on incrémente le nombre de fois que l'agent n'as pas répondu
					entry.setValue(
							new Pair<CoalitionState, Integer>(CoalitionState.Waiting, entry.getValue().getValue() + 1));

					// l'agent renvoie le même message BossMoveMessage message = new
					/*
					 * BossMovedMessage message = new BossMovedMessage();
					 * message.setNewPosition(informations.myPosition);
					 * informations.nodesAround.remove(informations.myPosition);
					 * message.setNodes(informations.nodesAround);
					 * message.setOldPosition(informations.oldPosition);
					 * message.setAgentsPosition(informations.agentsPositions);
					 * PacketManager.Send(this.myAgent, entry.getKey(), message);
					 */

				}
			}
			// si l'agent vient de recevoir un message ( memberMovedMessage) et que tous les
			// autres membres sont ready alors
			// l'agent peut envoyer à son boss un member move message
			if (!isMemberWaiting && memberUpdated && this.myAgent.getLocalName() != informations.bossName) {
				System.out
						.println(myAgent.getLocalName() + "Send Move message to his boss  : " + informations.bossName);
				MemberMovedMessage message = new MemberMovedMessage();
				informations.agentsPositions.put(myAgent.getLocalName(), new Pair<String,Long>(informations.myPosition,System.currentTimeMillis()));
				message.setAgentsPositions(informations.agentsPositions);
				PacketManager.Send(this.myAgent, informations.bossName, message);
			} else {
				// si on attend aucun membre alors
				if (!isMemberWaiting) {
					// si le chef à pu bouger du coup l'agent envoie un msg à tous les autres agents
					// Integer size = nodesArounds.size();
					/*
					 * if(nodesArounds.size() <= 0) { nodesArounds = informations.nodesAround; }
					 */

					// int index = ThreadLocalRandom.current().nextInt(0, nodesArounds.size());
					Integer tryMove = 0;
					informations.agentsPositions.remove(this.informations.myPosition);
					nextNode = nodesArounds.get(tryMove);
					nextNode = informations.myMap.getNearestNode(informations.myPosition, nextNode,
							new ArrayList<Pair<String,Long>>(informations.agentsPositions.values()));
					Boolean isMoved = ((AbstractDedaleAgent) this.myAgent).moveTo(nextNode);
					while (tryMove++ < nodesArounds.size()) {
						System.out.println("agent : " + myAgent.getLocalName() + "Boss : " + informations.bossName
								+ " try move : " + tryMove);
						if (isMoved) {
							informations.oldPosition = informations.myPosition;
							informations.myPosition = nextNode;
							// si on est chef de aucun membre l'agent notifie son boss
							if (informations.members.size() <= 0) {
								MemberMovedMessage message = new MemberMovedMessage();
								informations.agentsPositions.put(myAgent.getLocalName(), new Pair<String,Long>(informations.myPosition,System.currentTimeMillis()));
								message.setAgentsPositions(informations.agentsPositions);
								PacketManager.Send(this.myAgent, informations.bossName, message);

							} else {
								for (Map.Entry<String, Pair<CoalitionState, Integer>> entry : informations.members
										.entrySet()) {
									System.out.println(myAgent.getLocalName() + " Send : " + entry.getKey());
									BossMovedMessage message = new BossMovedMessage();
									message.setNewPosition(nextNode);
									// en supprime des noeuds pour que d'autre agent ne tente pas d'y accéder
									message.setNodes(nodesArounds);
									message.setOldPosition(informations.myPosition);
									informations.agentsPositions.put(myAgent.getLocalName(), new Pair<String,Long>(informations.myPosition,System.currentTimeMillis()));
									message.setAgentsPosition(informations.agentsPositions);
									PacketManager.Send(this.myAgent, entry.getKey(), message);
									entry.setValue(new Pair<CoalitionState, Integer>(CoalitionState.Waiting, 0));
								}
								// l'agent a bien move
							}
							break;
						} else {
							// à chaque iteration en teste un autre noeud
							// index = ThreadLocalRandom.current().nextInt(0, nodesArounds.size());
							if (tryMove >= nodesArounds.size()) {
								break;
							}
							nextNode = nodesArounds.get(tryMove);
							nextNode = informations.myMap.getNearestNode(informations.myPosition, nextNode,
									new ArrayList<Pair<String,Long>>(informations.agentsPositions.values()));
							isMoved = ((AbstractDedaleAgent) this.myAgent).moveTo(nextNode);
						}

					}

					// l'agent est bloqué et ne peut pas bouger
					if (tryMove >= nodesArounds.size()) {
						// si le member n'est un chef d'aucun groupe en envoie un no move
						if (informations.members.size() <= 0) {
							
							MemberMovedMessage message = new MemberMovedMessage();
							informations.agentsPositions.put(myAgent.getLocalName(),new Pair<String,Long>(informations.myPosition,System.currentTimeMillis()));
							message.setAgentsPositions(informations.agentsPositions);
							PacketManager.Send(this.myAgent, informations.bossName, message);

						} else {
							// l'agent vérifie si c'est un de ces membres de groupes le bloque
							String foundPosition = "";
							for (String i : nodesArounds) {
								for (Pair<String, CoalitionType> member : informations.coalitionInformations
										.get(informations.coalitionId)) {
									if (informations.agentsPositions.get(member.getKey()).getKey() == i) {
										foundPosition = i;
										break;
									}
								}

								if (foundPosition != "")
									break;

							}

							// si on trouve que quelqu'un est sur la position , on broadcast pour qu'il
							// décale
							if (foundPosition != "") {
								for (Map.Entry<String, Pair<CoalitionState, Integer>> iterator : informations.members
										.entrySet()) {
									CanMoveNearMessage message = new CanMoveNearMessage();
									message.setPosition(foundPosition);
									PacketManager.Send(this.myAgent, iterator.getKey(), message);
									informations.members.put(iterator.getKey(),
											new Pair<CoalitionState, Integer>(CoalitionState.Waiting, 0));
								}
							} else {
								// ça doit être le wampus ou bien une mauvaise sync
							}
						}

					}

				}
			}

		}
		informations.state = AgentState.Dispatcher;

	};

	public int onEnd() {
		return informations.state.ordinal();
	}
}
