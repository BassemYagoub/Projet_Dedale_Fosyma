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
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
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


	public static final long  DefaultTimeOut = 500;
	public MapRepresentation myMap;
	public List<String> openNodes;
	
	public HashMap<String,String> agentsKey;
	// position : List<d'arêtes>
	private HashMap<String,ArrayList<String>> edges;
	private ArrayList<String> receivers;
	
	public ArrayList<String> AgentKnowing;
	
	// state agent working,pending ...
	public AgentState state =  AgentState.Exploring;

	// todo : refactor after (encapsulate in 1 class  and create own method) 
	private Set<String> closedNodes;
	
	
	//public String currentConversation = null;
	public ConversationInformations currentConversation = null;
	TreeSet<String> treeKey;	
	private String customKey;
	
	private HashMap<String,Long> cacheMessages = new HashMap<String,Long> ();
	
	public Integer coalitionId;
	public Boolean isHunter;
	public HashMap<String,Behaviour> statsMachine;
	public Behaviour currentBehaviour;
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
		this.edges = new HashMap<String,ArrayList<String>>();
		this.AgentKnowing  = new ArrayList<String>();
		this.coalitionId = -1;
		this.isHunter = false;
		this.currentBehaviour = null;
		this.statsMachine = new HashMap<String,Behaviour>();

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
	
	public Boolean isSameKey(String agentName , String Key) {
		if(agentsKey.containsKey(agentName)) {

			if(agentsKey.get(agentName)== Key) {
				return true;
			}else {
				return false;
			}
		}else {
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
            	if(!i.getName().getLocalName().equals(myagent.getLocalName())) {
            		String a = i.getName().getLocalName();
            		String b = myagent.getLocalName();
            		results.add(i.getName().getLocalName());
            	}
            }

        } catch (FIPAException e) {
            e.printStackTrace();
        }
        
        return results;

	}	
	
	public void updateCacheReceivers(ArrayList<String> receivers) {
		for(String i : receivers) {
			this.addOrUpdateCacheMessage(i, System.currentTimeMillis());
		}
	}
	public void addOrUpdateCacheMessage(String agent , Long time) {
		
		if(cacheMessages.containsKey(agent)) {

			cacheMessages.replace(agent, time);
			
		}else {
			cacheMessages.put(agent, time);
		}
	}
	
	public long getCacheMessage(String agent) {
		
		if(cacheMessages.containsKey(agent)) {
	
			return cacheMessages.get(agent);
			
		}else {
			return -1;
		}
	}

}
