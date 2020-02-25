package eu.su.mas.dedaleEtu.mas.knowledge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.util.leap.Serializable;

/**
 * 
 * @author yamisaaf
 *
 */
public class AgentInformations implements Serializable {


	public MapRepresentation myMap;
	public List<String> openNodes;
	
	public HashMap<String,String> agentsKey;
	// position : List<d'arêtes>
	private HashMap<String,ArrayList<String>> edges;
	private ArrayList<String> receivers;
	
	// state agent working,pending ...
	public AgentState state;

	// todo : refactor after (encapsulate in 1 class  and create own method) 
	private Set<String> closedNodes;
	
	
	TreeSet<String> treeKey;	
	private String customKey;

	// getter 
	public ArrayList<String> getReceivers(){
		return this.receivers;
	}
	
	public void setReceivers( ArrayList<String> receivers){
		 this.receivers = receivers;
	}
	
	public String getAgentKey() {
		return customKey;
	}
	
	public Set<String> getClosedNodes() {
		return closedNodes;
	}
	
	public HashMap<String,ArrayList<String>> getEdges(){
		return this.edges;
	}
	
	public AgentInformations() {
		this(null);
	}
	
	public AgentInformations(ArrayList<String> receivers) {
		this.openNodes = new ArrayList<String>();
		this.closedNodes = new HashSet<String>();
		this.agentsKey = new HashMap<String,String>();
		this.receivers = receivers;
		this.treeKey =new TreeSet<String>();
		this.customKey = "";
		this.state = AgentState.Working;
		this.edges = new HashMap<String,ArrayList<String>>();

	}
	
	/*
	 * add the agent position in closedNodes and create/update the key
	 */
	public void addCurrentPositon(String currentPosition) {
		if(!closedNodes.contains(currentPosition)) {
			closedNodes.add(currentPosition);
			
			
			treeKey.add(currentPosition);
			//for the moment just concatenate string position
			customKey = treeKey.toString();
		}
		
	}
	
	public void mergeInformations(Set<String> _closedNodes , HashMap<String,ArrayList<String>> edges , List<String> _openNodes) {
		for (String node : _closedNodes){
			   if (!this.closedNodes.contains(node)) {
				  // add closed nodes
				   this.myMap.addNode(node, MapAttribute.closed);
				   this.closedNodes.add(node);
				   this.treeKey.add(node);
				   
				   // add edges
				   if(edges.containsKey(node)) {
					   for(String edge : edges.get(node)) {
						   this.myMap.addNode(edge, MapAttribute.open);
						   this.myMap.addEdge(node, edge);
					   }
				   } 
			   }
			}
		
		for(String node : _openNodes) {
		   if (!this.openNodes.contains(node)) {
			   this.openNodes.add(node);
		   }
		}
	}
	/*
	 * if the key isn't the same then we change and return false
	 */
	public Boolean addOrUpdate(String agentName , String Key) {
		
		if(agentsKey.containsKey(agentName)) {
			if(agentsKey.get(agentName)== Key) {
				return true;
			}else {
				agentsKey.replace(agentName, Key);
				return false;
			}
		}else {
			agentsKey.put(agentName, Key);
			return false;
		}
	}
	
	public void addEdge(String position , String nodeId) {
		this.myMap.addEdge(position, nodeId);
		if(!this.edges.containsKey(position)) {
			this.edges.put(position, new ArrayList<String>());
		}
		
		this.edges.get(position).add(nodeId);
	}
	
	public ArrayList<String> getTyeReceivers(String type , Agent myagent) {
		ArrayList<String> results = new ArrayList<String>();
        try {
            DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd2 = new ServiceDescription();
            sd2.setType(type);
            dfd.addServices(sd2);
            for(DFAgentDescription i :  DFService.search(myagent, dfd)) {
            	results.add(i.getName().getLocalName());
            }

        } catch (FIPAException e) {
            e.printStackTrace();
        }
        
        return results;

	}	

}
