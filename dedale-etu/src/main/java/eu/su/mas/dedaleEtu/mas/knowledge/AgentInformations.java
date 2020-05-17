package eu.su.mas.dedaleEtu.mas.knowledge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import eu.su.mas.dedaleEtu.mas.toolBox.CoalitionState;
import eu.su.mas.dedaleEtu.mas.toolBox.PacketManager;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Serializable;
import javafx.util.Pair;

/**
 * 
 * @author yamisaaf
 *
 */
public class AgentInformations implements Serializable {

	public static final long DefaultTimeOut = 300;
	public  static final Integer MaxTry = 15;
	public static Integer CoalitionId = 0;
	public static Object lock = new Object();
	public MapRepresentation myMap;
	public List<String> openNodes;

	public HashMap<String, String> agentsKey;
	// position : List<d'arêtes>
	private HashMap<String, ArrayList<String>> edges;

	private ArrayList<String> receivers;

	public ArrayList<String> AgentKnowing;

	// state agent working,pending ...
	public AgentState state = AgentState.Exploring;

	// todo : refactor after (encapsulate in 1 class and create own method)
	private ArrayList<String> closedNodes;

	// public String currentConversation = null;
	public ConversationInformations currentConversation = null;
	TreeSet<String> treeKey;
	private String customKey;

	private HashMap<String, Long> cacheMessages = new HashMap<String, Long>();

	public Integer coalitionId;
	public HashMap<String, Pair<CoalitionState, Integer>> members;
	public String bossName = null;

	public static CoalitionInformations coalitionInformations = new CoalitionInformations();
	public Boolean isHunter;
	public HashMap<String, Behaviour> statsMachine;
	public Behaviour currentBehaviour;
	public String nextNode;
	// les noeuds ou l'agent peut move ( ils sont open )
	public ArrayList<String> nodes;
	public String myPosition;
	public String oldPosition;
	// agent , position / heure modification
	public HashMap<String, Pair<String, Long>> agentsPosition;
	public String agentName = "";
	public ArrayList<String> nearNodes;
	public ArrayList<ACLMessage> messages = new ArrayList<ACLMessage>();
	public Integer numberReceiveBossMessage = 0;
	public Boolean isStench = false;
	public String positionStench = "";
	public ArrayList<String>  nodesStench;
	
	public Integer receivedBossMessage = 0;
	public Integer SyncMessage = 0;
	// getter
	public ArrayList<String> getReceivers() {
		return this.receivers;
	}

	public void setReceivers(ArrayList<String> receivers) {
		this.receivers = receivers;
	}

	public String getAgentKey() {
		return customKey;
	}

	public ArrayList<String> getClosedNodes() {
		return closedNodes;
	}

	public HashMap<String, ArrayList<String>> getEdges() {
		return this.edges;
	}

	public AgentInformations() {
		this(null);
	}

	public AgentInformations(ArrayList<String> receivers) {
		this.openNodes = new ArrayList<String>();
		this.closedNodes = new ArrayList<String>();
		this.agentsKey = new HashMap<String, String>();
		this.receivers = receivers;
		this.treeKey = new TreeSet<String>();
		this.customKey = "";
		this.edges = new HashMap<String, ArrayList<String>>();
		this.AgentKnowing = new ArrayList<String>();
		this.coalitionId = -1;
		this.isHunter = false;
		this.currentBehaviour = null;
		this.statsMachine = new HashMap<String, Behaviour>();
		this.members = new HashMap<String, Pair<CoalitionState, Integer>>();
		this.agentsPosition = new HashMap<String, Pair<String, Long>>();

	}

	/*
	 * add the agent position in closedNodes and create/update the key
	 */
	public void addCurrentPositon(String currentPosition) {
		if (!closedNodes.contains(currentPosition)) {
			closedNodes.add(currentPosition);

			treeKey.add(currentPosition);
			// for the moment just concatenate string position
			customKey = treeKey.toString();
		}

	}

	public void mergeInformations(ArrayList<String> _closedNodes, HashMap<String, ArrayList<String>> _edges,
			List<String> _openNodes) {
		if (_closedNodes == null)
			return;
	
		for (String node : _openNodes) {
			if (!this.closedNodes.contains(node) && !this.openNodes.contains(node)) {
				this.openNodes.add(node);
				this.myMap.addNode(node, MapAttribute.open);
				
				
			}
		}
		for (String node : _closedNodes) {
			if (!this.closedNodes.contains(node)) {
				// add closed nodes
				this.myMap.addNode(node, MapAttribute.closed);
				this.closedNodes.add(node);
				this.treeKey.add(node);
				if(this.openNodes.contains(node)) {
					this.openNodes.remove(node);
				}
				this.myMap.addNode(node,MapAttribute.closed);

				/*
				 * // add edges for(String edge : edges.get(node)) { this.myMap.addEdge(node,
				 * edge); }
				 */
			}
		}
		
		for(Map.Entry<String, ArrayList<String>> iterator : _edges.entrySet()) {
			for(String node : iterator.getValue()) {
				this.addEdge(iterator.getKey(), node);
			}
		}
		
		



		
		
		
	}

	/*
	 * if the key isn't the same then we change and return false
	 */
	public Boolean addOrUpdate(String agentName, String Key) {
		if (agentName == null)
			return false;
		if(Key == null)
			return false;
		if (agentsKey.containsKey(agentName)) {
			if (agentsKey.get(agentName) == Key) {
				return true;
			} else {
				agentsKey.replace(agentName, Key);
				return false;
			}
		} else {
			agentsKey.put(agentName, Key);
			return false;
		}
	}

	public Boolean isSameKey(String agentName, String Key) {
		if (agentsKey.containsKey(agentName)) {

			if (agentsKey.get(agentName) == Key) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public void addEdge(String position, String nodeId) {
		if (!this.edges.containsKey(position)) {
			this.edges.put(position, new ArrayList<String>());
		}
		Boolean state = true;
		
		for(String node : this.edges.get(position)) {
			if(node == nodeId) {
				state = false;
			}
		}
		
		if(state) {
			this.myMap.addEdge(position, nodeId);
			this.edges.get(position).add(nodeId);
		}
	
	}

	public ArrayList<String> getTyeReceivers(String type, Agent myagent) {
		ArrayList<String> results = new ArrayList<String>();
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd2 = new ServiceDescription();
			sd2.setType(type);
			dfd.addServices(sd2);
			for (DFAgentDescription i : DFService.search(myagent, dfd)) {
				if (!i.getName().getLocalName().equals(myagent.getLocalName())) {
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
		for (String i : receivers) {
			this.addOrUpdateCacheMessage(i, System.currentTimeMillis());
		}
	}

	public void addOrUpdateCacheMessage(String agent, Long time) {

		if (cacheMessages.containsKey(agent)) {

			cacheMessages.replace(agent, time);

		} else {
			cacheMessages.put(agent, time);
		}
	}

	public long getCacheMessage(String agent) {

		if (cacheMessages.containsKey(agent)) {

			return cacheMessages.get(agent);

		} else {
			return -1;
		}
	}

	public void removeCacheMessage(String agent) {
		if (cacheMessages.containsKey(agent)) {

			cacheMessages.remove(agent);

		}
	}

	public void clearCacheMessage() {
		cacheMessages.clear();
	}

	public void removeReceiver(String agentName) {
		this.receivers.remove(agentName);
	}

	public void addOrUpdateAgentPosition(String name, Pair<String, Long> position) {
		if (agentsPosition.containsKey(name)) {
			if (agentsPosition.get(name).getValue() < position.getValue()) { // ça veut dire que notre information est
																				// plus nouvelle
				// du coup on update
				agentsPosition.put(name, position);
			}
		} else {
			agentsPosition.put(name, position);
		}
	}

	public void addOrUpdateAgentPosition(HashMap<String, Pair<String, Long>> agents) {

		for (Map.Entry<String, Pair<String, Long>> iterator : agents.entrySet()) {
			this.addOrUpdateAgentPosition(iterator.getKey(), iterator.getValue());
		}
	}
	




}
