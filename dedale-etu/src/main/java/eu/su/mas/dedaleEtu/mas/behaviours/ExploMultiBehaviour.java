package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import eu.su.mas.dedaleEtu.mas.protocol.PingMessage;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.CoalitionType;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import javafx.util.Pair;

public class ExploMultiBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 8567689731496787661L;

	private boolean finished = false;

	private AgentInformations informations;

	private AbstractDedaleAgent customAgent;

	public ExploMultiBehaviour(final AbstractDedaleAgent myagent, AgentInformations informations) {
		super(myagent);
		this.customAgent = myagent;
		this.informations = informations;
	}

	@Override
	public void action() {

		if (this.informations.openNodes.size() > 0) {
			if (informations.nextNode == null) {
				// update we need random here

				informations.nextNode = this.informations.myMap
						.getShortestPath(informations.myPosition, this.informations.openNodes.get(0)).get(0);
				informations.nodes.add(informations.nextNode);
			}
		}

		int index = ThreadLocalRandom.current().nextInt(0, informations.nodes.size());

		((AbstractDedaleAgent) this.myAgent).moveTo(informations.nodes.get(index));
		
		//update la current position
		informations.myPosition  = informations.nodes.get(index);
		informations.state = AgentState.SendingPing;

	}

	// System.out.println("EXPLORATION");

	// this.customAgent.removeBehaviour(informations.statsMachine.get("Exploration"));
	// this.customAgent.addBehaviour(informations.statsMachine.get("Hunter"));

	// this.customAgent.addBehaviour(new
	// startMyBehaviours(this.customAgent,List.of(informations.statsMachine.get("Hunter"))));
	// FSMBehaviour hunter = new FSMBehaviour(this.customAgent);
	// hunter.registerFirstState(new SayHello(this.customAgent), "hello");
	// hunter.registerDefaultTransition("hello","hello");

	// ((AbstractDedaleAgent)this.myAgent).removeBehaviour(this);
	// this.informations.currentBehaviour =
	// this.informations.statsMachine.get("Hunter");
	// ((AbstractDedaleAgent)this.myAgent).addBehaviour(new
	// startMyBehaviours(((AbstractDedaleAgent)this.myAgent),List.of(informations.currentBehaviour)));
	// this.customAgent.removeBehaviour(this.informations.statsMachine.get("Exploration"));
	// informations.currentBehaviour = new
	// startMyBehaviours(((AbstractDedaleAgent)this.myAgent),List.of(informations.statsMachine.get("Hunter")));
	// ((AbstractDedaleAgent)this.myAgent).addBehaviour(informations.currentBehaviour);
	// this.customAgent.addBehaviour(new
	// startMyBehaviours(this.customAgent,List.of(informations.statsMachine.get("Hunter"))));
	// informations.state = AgentState.SendingPing;
	// informations.state = AgentState.Nothing;
	// ((AbstractDedaleAgent)this.myAgent).addBehaviour(new
	// startMyBehaviours(((AbstractDedaleAgent)this.myAgent),List.of(informations.currentBehaviour)));
	// this.myAgent.removeBehaviour(new
	// startMyBehaviours(((AbstractDedaleAgent)this.myAgent),List.of(informations.currentBehaviour))this.informations.statsMachine.get("Exploration"));
	// ((AbstractDedaleAgent)this.myAgent)isHunter.
	

	public int onEnd() {
		return informations.state.ordinal();
	}
}
