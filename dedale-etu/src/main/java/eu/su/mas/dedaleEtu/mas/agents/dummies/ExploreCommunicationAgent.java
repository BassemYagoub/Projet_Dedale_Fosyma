package eu.su.mas.dedaleEtu.mas.agents.dummies;

import java.util.ArrayList;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploSoloBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.MessagingBehaviour;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.behaviours.Behaviour;

public class ExploreCommunicationAgent extends AbstractDedaleAgent{
	
	private MapRepresentation myMap;
	
	protected void setup(){
		super.setup();
		List<Behaviour> behaviours = new ArrayList<Behaviour>();
		
		behaviours.add(new ExploSoloBehaviour(this, this.myMap));
		behaviours.add(new MessagingBehaviour(this, this.myMap));
		addBehaviour(new startMyBehaviours(this, behaviours));
		
		System.out.println("the  agent "+this.getLocalName()+ " is started");

	}
}
