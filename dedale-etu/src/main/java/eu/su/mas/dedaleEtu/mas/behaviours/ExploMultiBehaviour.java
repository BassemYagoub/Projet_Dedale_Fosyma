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
import eu.su.mas.dedaleEtu.mas.agents.dummies.ExploreSoloAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import eu.su.mas.dedaleEtu.mas.protocol.PingMessage;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;

public class ExploMultiBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 8567689731496787661L;

	private boolean finished = false;
	
	private AgentInformations informations;


	public ExploMultiBehaviour(final AbstractDedaleAgent myagent ,AgentInformations informations ) {
		super(myagent);
		this.informations = informations;
	}

	@Override
	public void action() {
		if(this.informations.myMap==null)
			this.informations.myMap= new MapRepresentation();
		System.out.println(this.informations.getClosedNodes().size());

		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();

		if (myPosition!=null){
			List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();//myPosition

			try {
				this.myAgent.doWait(500);
			} catch (Exception e) {
				e.printStackTrace();
			}

			//this.informations.closedNodes.add(myPosition);
			this.informations.addCurrentPositon(myPosition);
			
			PingMessage message = new PingMessage();
			message.setKey(informations.getAgentKey());
			PacketManager.Send(this.myAgent, informations.getReceivers(),message);
			
			this.informations.openNodes.remove(myPosition);
			this.informations.myMap.addNode(myPosition,MapAttribute.closed);

			String nextNode=null;
			Iterator<Couple<String, List<Couple<Observation, Integer>>>> iter=lobs.iterator();
			while(iter.hasNext()){
				String nodeId=iter.next().getLeft();
				if (!this.informations.getClosedNodes().contains(nodeId)){
					if (!this.informations.openNodes.contains(nodeId)){
						this.informations.openNodes.add(nodeId);
						this.informations.myMap.addNode(nodeId, MapAttribute.open);
					    //this.informations.myMap.addEdge(myPosition, nodeId);	
						this.informations.addEdge(myPosition, nodeId);
					}else{
						//this.informations.myMap.addEdge(myPosition, nodeId);
						this.informations.addEdge(myPosition, nodeId);
					}
					if (nextNode==null) nextNode=nodeId;
				}
			}

			// change after
		/*	if (this.informations.openNodes.isEmpty()){
				finished=true;
				System.out.println("Exploration successufully done, behaviour removed.");
			}else{

				if (nextNode==null){
					nextNode=this.informations.myMap.getShortestPath(myPosition, this.informations.openNodes.get(0)).get(0);
				}
				
				((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
				System.out.println(nextNode);
				
			}*/
			if (nextNode==null){
				nextNode=this.informations.myMap.getShortestPath(myPosition, this.informations.openNodes.get(0)).get(0);
			}
			((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);


		}
	}
	
	public int onEnd() {
		return informations.state.ordinal();
	}
}
