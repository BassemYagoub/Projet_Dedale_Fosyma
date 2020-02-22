package eu.su.mas.dedaleEtu.mas.knowledge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
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
	
	public String getAgentKey() {
		return customKey;
	}
	
	public Set<String> getClosedNodes() {
		return closedNodes;
	}
	
	public AgentInformations(ArrayList<String> receivers) {
		openNodes = new ArrayList<String>();
		closedNodes = new HashSet<String>();
		this.agentsKey = new HashMap<String,String>();
		this.receivers = receivers;
		treeKey =new TreeSet<String>();
		this.customKey = "";
		this.state = AgentState.Working;

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
	
	public void mergeClosedNodes(Set<String> nodes) {
		for (String node : nodes){
			   if (!closedNodes.contains(node)) {
				   myMap.addNode(node, MapAttribute.closed);
				   closedNodes.add(node);
				   treeKey.add(node); 
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



}
