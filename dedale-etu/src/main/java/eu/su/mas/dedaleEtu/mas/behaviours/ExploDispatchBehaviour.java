package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import jade.core.behaviours.OneShotBehaviour;

public class ExploDispatchBehaviour extends OneShotBehaviour {
	private AgentInformations informations;

	public ExploDispatchBehaviour(final AbstractDedaleAgent myagent, AgentInformations informations) {
		super(myagent);
		this.informations = informations;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

		if (this.informations.myMap == null) {
			this.informations.myMap = new MapRepresentation();
			this.informations.myMap.informations = this.informations;
			this.informations.setReceivers(this.informations.getTyeReceivers("exploration", this.myAgent));
			this.informations.agentName = this.myAgent.getLocalName();
		}

		// System.out.println("agent : "+myAgent.getLocalName() + " nbr :
		// "+this.informations.getClosedNodes().size());
		informations.myPosition = ((AbstractDedaleAgent) this.myAgent).getCurrentPosition();
		informations.isStench = false;
		if (informations.myPosition != null) {
			List<Couple<String, List<Couple<Observation, Integer>>>> lobs = ((AbstractDedaleAgent) this.myAgent)
					.observe();// myPosition
			for (Couple<String, List<Couple<Observation, Integer>>> m : lobs) {
				// System.out.println(m.getRight().toString());
				if (m.getRight().toString().toLowerCase().contains("stench")) {
				//	informations.addOrUpdateAgentPosition("Wamp", position);
					if(m.getLeft().equals(informations.myPosition)) {
						informations.isStench = true;	
						break;
					}
						
				}
			}
			try {
				// this.myAgent.doWait(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// this.informations.closedNodes.add(myPosition);
			this.informations.addCurrentPositon(informations.myPosition);

			this.informations.openNodes.remove(informations.myPosition);
			this.informations.myMap.addNode(informations.myPosition, MapAttribute.closed);

			informations.nextNode = null;

			
			Iterator<Couple<String, List<Couple<Observation, Integer>>>> iter = lobs.iterator();
			informations.nodes = new ArrayList<String>();
			informations.nearNodes =  new ArrayList<String>();

			// informations.nodesAround.add(informations.myPosition);

			while (iter.hasNext()) {
				String nodeId = iter.next().getLeft();
				
				if(nodeId != informations.myPosition)
					informations.nearNodes.add(nodeId);
				
				if (!this.informations.getClosedNodes().contains(nodeId)) {
					this.informations.myMap.addNode(nodeId, MapAttribute.open);
					if (!this.informations.openNodes.contains(nodeId)) {
						this.informations.openNodes.add(nodeId);
						// this.informations.myMap.addEdge(myPosition, nodeId);
					}
					this.informations.addEdge(informations.myPosition, nodeId);
					informations.nodes.add(nodeId);
					if (informations.nextNode == null)
						informations.nextNode = nodeId;
				}

			}

			// ajouter les noeuds en fin de la liste
			if (informations.nodes.size() <= 0) {
				// on a parcouru toute la carte
				if(informations.openNodes.size() <= 0) {
					
					//informations.nodes = (ArrayList<String>)informations.nearNodes.clone();
					int index = ThreadLocalRandom.current().nextInt(0, informations.getClosedNodes().size());
					informations.nodes.clear();
					informations.nodes.add(informations.getClosedNodes().get(index));

				}else {
					for(int i = 0;i<informations.openNodes.size();i++) {
						List<String> nodes = informations.myMap.getShortestPath(informations.myPosition, informations.openNodes.get(i));
						nodes.remove(informations.myPosition);
						if(nodes.size() > 0) {
							informations.nodes.add(nodes.get(0));
							break;
						}
					
					}

				}
			}

			if(informations.isStench) {
				informations.nodes.clear();
				for (Couple<String, List<Couple<Observation, Integer>>> m : lobs) {
					// System.out.println(m.getRight().toString());
					if (m.getRight().toString().toLowerCase().contains("stench")) {
					//	informations.addOrUpdateAgentPosition("Wamp", position);
						informations.nodes.add(m.getLeft());
					}
				}
			}
			
			// appartient pas à une coalition
			if (informations.coalitionId == -1) {
				informations.state = AgentState.Exploring;
			} else {
				// appartient à une coalition
				informations.state = AgentState.ExploringCoalition;

			}

		}
	}

	public int onEnd() {
		return informations.state.ordinal();
	}

}
