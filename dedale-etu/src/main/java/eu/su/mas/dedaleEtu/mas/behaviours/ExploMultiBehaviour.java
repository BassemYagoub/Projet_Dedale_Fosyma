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
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;

public class ExploMultiBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 8567689731496787661L;

	private boolean finished = false;
	
	private AgentInformations informations;

	private AbstractDedaleAgent customAgent; 

	public ExploMultiBehaviour(final AbstractDedaleAgent myagent ,AgentInformations informations ) {
		super(myagent);
		this.customAgent = myagent;
		this.informations = informations;
	}

	@Override
	public void action() {
		if(this.informations.myMap==null) {
			this.informations.myMap= new MapRepresentation();
			this.informations.setReceivers(this.informations.getTyeReceivers("exploration", this.myAgent));

		}
	
		// System.out.println("agent : "+myAgent.getLocalName() + " nbr : "+this.informations.getClosedNodes().size());
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		Boolean isHunter = false;
		if (myPosition!=null){
			List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();//myPosition
			for(Couple<String,List<Couple<Observation,Integer>>> m : lobs) {
				//System.out.println(m.getRight().toString());
				if(m.getRight().toString().toLowerCase().contains("stench")) {
					isHunter = true;
					break;
				}
			}
			try {
				this.myAgent.doWait(500);
			} catch (Exception e) {
				e.printStackTrace();
			}

			//this.informations.closedNodes.add(myPosition);
			this.informations.addCurrentPositon(myPosition);
			
	
			
			this.informations.openNodes.remove(myPosition);
			this.informations.myMap.addNode(myPosition,MapAttribute.closed);

			String nextNode=null;
			Iterator<Couple<String, List<Couple<Observation, Integer>>>> iter=lobs.iterator();
			ArrayList<String> nodes = new ArrayList<String>();
			while(iter.hasNext()){
				String nodeId=iter.next().getLeft();
				if (!this.informations.getClosedNodes().contains(nodeId)){
					this.informations.myMap.addNode(nodeId, MapAttribute.open);
					if (!this.informations.openNodes.contains(nodeId)){
						this.informations.openNodes.add(nodeId);
					    //this.informations.myMap.addEdge(myPosition, nodeId);	
					}
					this.informations.addEdge(myPosition, nodeId);
					nodes.add(nodeId);
					if (nextNode==null) nextNode=nodeId;
				}
			}

			// change after
			if (this.informations.openNodes.isEmpty()){
				finished=true;
				System.out.println("Exploration successufully done, behaviour removed.");
			}else{

				if (nextNode==null){
					nextNode=this.informations.myMap.getShortestPath(myPosition, this.informations.openNodes.get(0)).get(0);
					nodes.add(nextNode);
				}
				
				int index = ThreadLocalRandom.current().nextInt(0, nodes.size());
				 
				((AbstractDedaleAgent)this.myAgent).moveTo(nodes.get(index));	
				
				
			}
		}
		
		informations.state = AgentState.SendingPing;
		//System.out.println("EXPLORATION");
		
    //	this.customAgent.removeBehaviour(informations.statsMachine.get("Exploration"));
    //	this.customAgent.addBehaviour(informations.statsMachine.get("Hunter"));
			
		//this.customAgent.addBehaviour(new startMyBehaviours(this.customAgent,List.of(informations.statsMachine.get("Hunter"))));
	//	FSMBehaviour hunter = new FSMBehaviour(this.customAgent);
	//	hunter.registerFirstState(new SayHello(this.customAgent), "hello");
		//hunter.registerDefaultTransition("hello","hello");
    	
		//((AbstractDedaleAgent)this.myAgent).removeBehaviour(this);
		//this.informations.currentBehaviour = this.informations.statsMachine.get("Hunter");
		//((AbstractDedaleAgent)this.myAgent).addBehaviour(new startMyBehaviours(((AbstractDedaleAgent)this.myAgent),List.of(informations.currentBehaviour)));
	//	this.customAgent.removeBehaviour(this.informations.statsMachine.get("Exploration"));
		//	informations.currentBehaviour = new startMyBehaviours(((AbstractDedaleAgent)this.myAgent),List.of(informations.statsMachine.get("Hunter")));
	//	((AbstractDedaleAgent)this.myAgent).addBehaviour(informations.currentBehaviour);
	//	this.customAgent.addBehaviour(new startMyBehaviours(this.customAgent,List.of(informations.statsMachine.get("Hunter"))));
	//	informations.state = AgentState.SendingPing;
		//informations.state = AgentState.Nothing;
	//	((AbstractDedaleAgent)this.myAgent).addBehaviour(new startMyBehaviours(((AbstractDedaleAgent)this.myAgent),List.of(informations.currentBehaviour)));
	//	this.myAgent.removeBehaviour(new startMyBehaviours(((AbstractDedaleAgent)this.myAgent),List.of(informations.currentBehaviour))this.informations.statsMachine.get("Exploration"));
	//	((AbstractDedaleAgent)this.myAgent)isHunter.
	}
	
	public int onEnd() {
		return informations.state.ordinal();
	}
}
